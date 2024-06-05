package com.mediasoft.warehouse.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class S3StorageService {

    private final AmazonS3 amazonS3;
    private final String ARCHIVE_PATH = "src/main/resources/archive.zip";

    public void uploadFileToBucket(String bucketName, String key, MultipartFile file) {
        if(!amazonS3.doesBucketExistV2(bucketName)){
            amazonS3.createBucket(bucketName);
        }

        PutObjectRequest putObjectRequest = getPutObjectRequest(file, bucketName, key);

        amazonS3.putObject(putObjectRequest);
    }

    public byte[] downloadFilesFromBucket(String bucketName, List<String> keys) {
        List<S3Object> s3ObjectList = keys.stream()
                .map(key -> amazonS3.getObject(bucketName, key))
                .toList();

        try(FileOutputStream fos = new FileOutputStream(ARCHIVE_PATH);
            ZipOutputStream zipOut = new ZipOutputStream(fos)){
            HashMap<String, Integer> names = new HashMap<>();

            for (S3Object s3Object : s3ObjectList) {
                S3ObjectInputStream s3InputStream = s3Object.getObjectContent();
                String decodeFilename = URLDecoder.decode(s3Object.getObjectMetadata().getUserMetaDataOf("filename"), StandardCharsets.UTF_8);
                if(names.containsKey(decodeFilename)){
                    int lastIndex = decodeFilename.lastIndexOf(".");
                    String duplicateDecodeFilename = decodeFilename.substring(0, lastIndex) +
                            " (" + names.get(decodeFilename) + ")" +
                            decodeFilename.substring(lastIndex);
                    zipOut.putNextEntry(new ZipEntry(duplicateDecodeFilename));
                }
                else{
                    zipOut.putNextEntry(new ZipEntry(decodeFilename));
                }
                names.merge(decodeFilename, 1, Integer::sum);

                byte[] bytes = new byte[1024];
                int length;
                while((length = s3InputStream.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(InputStream in = new FileInputStream(ARCHIVE_PATH)) {
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PutObjectRequest getPutObjectRequest(MultipartFile file, String bucketName, String key) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        String urlEncodedUTF8Filename = URLEncoder.encode(file.getOriginalFilename(),
                StandardCharsets.UTF_8);
        metadata.addUserMetadata("filename", urlEncodedUTF8Filename);

        PutObjectRequest putObjectRequest;
        try {
            putObjectRequest = new PutObjectRequest(
                    bucketName, key,
                    file.getInputStream(), metadata
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return putObjectRequest;
    }
}
