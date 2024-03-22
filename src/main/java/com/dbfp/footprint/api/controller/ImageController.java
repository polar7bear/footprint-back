package com.dbfp.footprint.api.controller;

import com.dbfp.footprint.dto.review.response.ImageResDto;
import com.dbfp.footprint.api.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/images", consumes = "multipart/form-data")
    public ResponseEntity<ImageResDto> imageUpload(@RequestPart("image") MultipartFile image) {
        ImageResDto upload = imageService.upload(image);

        return new ResponseEntity<>(upload, HttpStatus.OK);
    }
}