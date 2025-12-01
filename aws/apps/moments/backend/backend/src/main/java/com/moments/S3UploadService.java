package com.moments;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
public class S3UploadService {

    private final S3Template s3Template;

    @Value("${moments.s3.bucket}")
    private String bucketName;

    public S3UploadService(S3Template s3Template) {
        this.s3Template = s3Template;
    }

    public String upload(String fileName,
                         String contentType,
                         byte[] bytes,
                         String extension) {

        // if fileName already has ".jpg", don't append extension again
        String keyName = fileName;
        if (extension != null && !extension.isBlank() && !fileName.endsWith(extension)) {
            keyName = fileName + extension;
        }

        String key = "photos/" + UUID.randomUUID() + "-" + keyName;

        s3Template.upload(
                bucketName,
                key,
                new ByteArrayInputStream(bytes),
                ObjectMetadata.builder().contentType(contentType).build()
        );

        return key;
    }
}
