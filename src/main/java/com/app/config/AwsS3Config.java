package com.app.config;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@ConditionalOnProperty(name = "aws.s3.enabled", havingValue = "true")
@Log4j2
public class AwsS3Config {

    @Value("${aws.s3.region:us-east-1}")
    private String awsRegion;
    @Value("${aws.s3.accessKeyId:us-east-1}")
    private String accessKeyId;
    @Value("${aws.s3.secretAccessKey}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        try {
            log.info("Trying to S3Client create.");
            return S3Client.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                    .overrideConfiguration(ClientOverrideConfiguration.builder()
                            .retryPolicy(r -> r.numRetries(3))
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("Failed to create S3Client.", e);
            throw e;
        } finally {
            log.info("S3Client created successfully.");
        }
    }
}
