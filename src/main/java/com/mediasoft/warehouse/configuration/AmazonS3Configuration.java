package com.mediasoft.warehouse.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Getter
public class AmazonS3Configuration {
    private final S3ConfigurationProperties s3ConfigurationProperties;
    @Bean
    public AmazonS3 amazonS3(){
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                s3ConfigurationProperties.getEndpoint(),
                                s3ConfigurationProperties.getRegion()
                        )
                )
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(
                                    s3ConfigurationProperties.getCredentials().getAccessKey(),
                                    s3ConfigurationProperties.getCredentials().getSecretKey()
                            )
                        )
                )
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
