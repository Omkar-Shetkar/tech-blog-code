package com.moments;

import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3UploadService {

    private final S3Template s3Template;

    @Value("${moments.s3.bucket}")
    private String bucketName;

    public S3UploadService(S3Template s3Template) {
        this.s3Template = s3Template;
    }

    public String upload(MultipartFile file) throws IOException {

        String key = "photos/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        s3Template.upload(
                bucketName,
                key,
                file.getInputStream()
        );

        // public URL if bucket is public/CF later; for now just return key
        return key;
    }
}
