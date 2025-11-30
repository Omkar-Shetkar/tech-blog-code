package com.moments;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Repository
public class PhotoDynamoRepository {

    private static final String PARTITION_KEY = "PHOTO";
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_INSTANT;

    private final DynamoDbTemplate dynamoDbTemplate;

    public PhotoDynamoRepository(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    public PhotoItem save(String s3Key, String url, String description) {
        Instant now = Instant.now();
        String sk = ISO.format(now) + "#" + UUID.randomUUID();

        PhotoItem item = new PhotoItem();
        item.setPk(PARTITION_KEY);
        item.setSk(sk);
        item.setS3Key(s3Key);
        item.setUrl(url);
        item.setDescription(description);
        item.setUploadDate(now);

        return dynamoDbTemplate.save(item);
    }

    public List<PhotoItem> findAllNewestFirst() {

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder()
                        .partitionValue(PARTITION_KEY)
                        .build());

        QueryEnhancedRequest queryEnhancedRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)
                .build();

        PageIterable<PhotoItem> result = dynamoDbTemplate.query(queryEnhancedRequest, PhotoItem.class);

        return result.items().stream().toList();
    }
}
