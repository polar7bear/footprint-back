package com.dbfp.footprint.api.service.review;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.dbfp.footprint.domain.review.Image;
import com.dbfp.footprint.dto.review.ImageDto;
import com.dbfp.footprint.exception.review.NotFoundImageException;
import com.dbfp.footprint.api.repository.review.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
@Service
public class ImageService {
    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloudfront-domain-name}")
    private String CLOUD_FRONT_DOMAIN_NAME;

    public ImageService(AmazonS3 amazonS3, ImageRepository imageRepository) {
        this.amazonS3 = amazonS3;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public ImageDto upload(MultipartFile multipartFile) {
        validateImage(multipartFile.getContentType());

        containingImageDelete(multipartFile);
        String fileName = createFileName(multipartFile.getOriginalFilename());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            uploadToBucket(fileName, inputStream, multipartFile.getSize(), multipartFile.getContentType());

            Image image = createImageEntity(fileName);
            imageRepository.save(image);

            return ImageDto.of(image);
        } catch (IOException e) {
            throw new NotFoundImageException(fileName);
        }
    }

    public String getImageUrl(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(NotFoundImageException::new);
        return image.getImageUrl();
    }

    @Transactional
    public void containingImageDelete(MultipartFile multipartFile) {
        Image containingImage = imageRepository.findImageByConvertImageNameContaining(
                multipartFile.getOriginalFilename());

        if (containingImage != null) {
            delete(containingImage);
        }
    }

    @Transactional(readOnly = true)
    public Image findById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow();
    }

    @Transactional
    public void delete(Image image) {
        amazonS3.deleteObject(bucket, image.getConvertImageName());
        imageRepository.deleteById(image.getId());
    }

    private void uploadToBucket(String fileName, InputStream inputStream,
                                long size, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contentType);

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(putObjectRequest);
    }

    private Image createImageEntity(String fileName) {
        String path = amazonS3.getUrl(bucket, fileName).getPath();
        return Image.builder()
                .imageUrl(CLOUD_FRONT_DOMAIN_NAME + path)
                .convertImageName(fileName.substring(fileName.lastIndexOf("/") + 1))
                .build();
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID() + "_" + fileName;
    }

    private void validateImage(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException();
        }
    }
}
