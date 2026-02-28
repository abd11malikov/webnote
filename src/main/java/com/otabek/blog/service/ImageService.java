package com.otabek.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Service
public class ImageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String publicUrl;

    public ImageService(
            @Value("${cloudflare.r2.access-key}") String accessKey,
            @Value("${cloudflare.r2.secret-key}") String secretKey,
            @Value("${cloudflare.r2.endpoint}") String endpoint,
            @Value("${cloudflare.r2.bucket}") String bucketName,
            @Value("${cloudflare.r2.public-url}") String publicUrl
    ) {
        this.bucketName = bucketName;
        this.publicUrl = publicUrl;

        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    public String uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return publicUrl + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: ", e);
        }
    }
}