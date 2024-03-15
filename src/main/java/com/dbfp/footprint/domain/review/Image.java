package com.dbfp.footprint.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column(name = "convert_image_name", nullable = false)
    private String convertImageName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "review_id")
    private Review review;

    @Builder
    private Image(String imageUrl, String convertImageName) {
        this.imageUrl = imageUrl;
        this.convertImageName = convertImageName;
    }
}
