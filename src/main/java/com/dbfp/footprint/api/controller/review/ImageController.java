package com.dbfp.footprint.api.controller.review;

import com.dbfp.footprint.dto.review.ImageDto;
import com.dbfp.footprint.api.service.review.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Tag(name = "Image", description = "Image API")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/images", consumes = "multipart/form-data")
    @Operation(summary = "이미지 업로드 API", description = "S3에 이미지를 업로드하고 url과 이미지 id를 받는 API")
    public ResponseEntity<ImageDto> imageUpload(@RequestPart("image") MultipartFile image) {
        ImageDto upload = imageService.upload(image);

        return new ResponseEntity<>(upload, HttpStatus.OK);
    }
}