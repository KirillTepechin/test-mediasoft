package com.mediasoft.warehouse.configuration;

import lombok.Data;

@Data
public class S3CredentialsProperties {
    private String accessKey;
    private String secretKey;
}
