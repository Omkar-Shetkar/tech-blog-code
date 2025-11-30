package com.moments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
@CrossOrigin(origins = "http://localhost:3000")
public class UploadController {

    private final S3UploadService s3UploadService;

    private final PhotoDynamoRepository photoDynamoRepository;

    @Value("${moments.s3.bucket}")
    private String bucketName;

    public UploadController(S3UploadService s3UploadService, PhotoDynamoRepository photoDynamoRepository) {
        this.s3UploadService = s3UploadService;
        this.photoDynamoRepository = photoDynamoRepository;
    }

    @PostMapping
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String key = s3UploadService.upload(file);
        String url = String.format("https://d2zkx2tprcx426.cloudfront.net/%s", key);

        photoDynamoRepository.save(key, url, file.getOriginalFilename());

        return ResponseEntity.ok(new UploadResponse(key));
    }

    public record UploadResponse(String key) {
    }
}
