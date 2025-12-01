package com.moments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, UploadResponse> uploadPhoto(
            PhotoDynamoRepository repository,
            S3UploadService s3UploadService
    ) {
        return event -> {
            String body = (String) event.get("body");
            boolean isBase64 = Boolean.TRUE.equals(event.get("isBase64Encoded"));
            if (body == null || !isBase64) {
                throw new IllegalStateException("Expected base64-encoded body from API Gateway");
            }

            // 1) Decode Base64 once
            byte[] rawBytes = Base64.getDecoder().decode(body);

            // 2) Find end of headers (first "\r\n\r\n") in rawBytes
            byte[] sep = "\r\n\r\n".getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
            int headerEnd = indexOf(rawBytes, sep);
            if (headerEnd < 0) {
                throw new IllegalStateException("Invalid multipart payload: no header separator");
            }
            int fileStart = headerEnd + sep.length;

            // 3) Find the last boundary start from the end (the bytes just before "--boundary")
            byte[] boundaryPrefix = "\r\n------".getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
            int boundaryPos = lastIndexOf(rawBytes, boundaryPrefix);
            if (boundaryPos < 0) {
                boundaryPos = rawBytes.length;
            }
            int fileEnd = boundaryPos;

            // 4) Extract ONLY the binary part
            byte[] fileBytes = java.util.Arrays.copyOfRange(rawBytes, fileStart, fileEnd);

            // 5) (Optional) parse headers from the header section for filename/content-type
            String headerText = new String(java.util.Arrays.copyOfRange(rawBytes, 0, headerEnd),
                    java.nio.charset.StandardCharsets.ISO_8859_1);
            String fileName = "upload.bin";
            String contentType = "application/octet-stream";
            for (String line : headerText.split("\r\n")) {
                String lower = line.toLowerCase();
                if (lower.startsWith("content-disposition:") && lower.contains("filename=")) {
                    String v = line.substring(line.toLowerCase().indexOf("filename=") + 9).trim();
                    if (v.startsWith("\"") && v.endsWith("\"")) v = v.substring(1, v.length() - 1);
                    fileName = v;
                } else if (lower.startsWith("content-type:")) {
                    contentType = line.substring("Content-Type:".length()).trim();
                }
            }

            String extension = "";
            int dot = fileName.lastIndexOf('.');
            if (dot != -1) extension = fileName.substring(dot);

            // 6) Upload clean image bytes
            String s3Key = s3UploadService.upload(fileName, contentType, fileBytes, extension);
            String url = "https://d2zkx2tprcx426.cloudfront.net/%s".formatted(s3Key);
            repository.save(s3Key, url, fileName);
            return new UploadResponse(s3Key, url);
        };
    }

    @Bean
    public Function<Map<String, Object>, List<PhotoItem>> listPhotos(PhotoDynamoRepository repository) {
        return ignored -> repository.findAllNewestFirst();
    }


    /** Find first index of pattern in data, or -1. */
    private int indexOf(byte[] data, byte[] pattern) {
        outer: for (int i = 0; i <= data.length - pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) continue outer;
            }
            return i;
        }
        return -1;
    }

    /** Find last index of pattern in data, or -1. */
    private  int lastIndexOf(byte[] data, byte[] pattern) {
        outer: for (int i = data.length - pattern.length; i >= 0; i--) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) continue outer;
            }
            return i;
        }
        return -1;
    }

    public record UploadRequest(String fileName, String contentType, String data, String description) {
    }

    public record UploadResponse(String key, String url) {
    }
}
