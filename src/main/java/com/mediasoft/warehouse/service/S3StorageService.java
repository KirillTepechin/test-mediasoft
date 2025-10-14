package com.mediasoft.warehouse.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class S3StorageService {

    private final AmazonS3 amazonS3;

    public void uploadFileToBucket(String bucketName, String key, MultipartFile file) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket(bucketName);
        }

        final PutObjectRequest putObjectRequest = buildPutObjectRequest(file, bucketName, key);

        amazonS3.putObject(putObjectRequest);
    }

    public void downloadFilesFromBucket(String bucketName, List<String> keys, OutputStream outputStream) {
        final List<S3Object> s3ObjectList = keys.stream()
                .map(key -> amazonS3.getObject(bucketName, key))
                .toList();

        try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
            final HashMap<String, Integer> fileNameCount = new HashMap<>();

            for (S3Object s3Object : s3ObjectList) {
                final S3ObjectInputStream s3InputStream = s3Object.getObjectContent();
                final String decodedFilename = URLDecoder.decode(
                        s3Object.getObjectMetadata().getUserMetaDataOf("filename"),
                        StandardCharsets.UTF_8
                );

                final String uniqueFilename = getUniqueFilename(decodedFilename, fileNameCount);
                zipOut.putNextEntry(new ZipEntry(uniqueFilename));

                byte[] buffer = new byte[1024];
                int length;
                while ((length = s3InputStream.read(buffer)) >= 0) {
                    zipOut.write(buffer, 0, length);
                }

                zipOut.closeEntry();
                s3InputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating ZIP archive from S3 files", e);
        }
    }

    private String getUniqueFilename(String filename, Map<String, Integer> fileNameCount) {
        if (!fileNameCount.containsKey(filename)) {
            fileNameCount.put(filename, 1);
            return filename;
        }

        int count = fileNameCount.get(filename);
        fileNameCount.put(filename, count + 1);

        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return filename + " (" + count + ")";
        }

        final String name = filename.substring(0, lastDotIndex);
        final String extension = filename.substring(lastDotIndex);
        return name + " (" + count + ")" + extension;
    }

    private PutObjectRequest buildPutObjectRequest(MultipartFile file, String bucketName, String key) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        final String urlEncodedUTF8Filename = URLEncoder.encode(file.getOriginalFilename(),
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
