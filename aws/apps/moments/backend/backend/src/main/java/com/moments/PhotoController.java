package com.moments;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:3000")
public class PhotoController {

    private final PhotoDynamoRepository photoDynamoRepository;

    public PhotoController(PhotoDynamoRepository photoDynamoRepository) {
        this.photoDynamoRepository = photoDynamoRepository;
    }

    @GetMapping
    public List<PhotoItem> listPhotos() {
        return photoDynamoRepository.findAllNewestFirst();
    }
}
