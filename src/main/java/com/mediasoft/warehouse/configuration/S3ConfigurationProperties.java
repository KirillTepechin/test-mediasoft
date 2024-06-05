package com.mediasoft.warehouse.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "s3")
public class S3ConfigurationProperties {
    private S3CredentialsProperties credentials;
    private String region;
    private String endpoint;
    private String bucketName;
}
