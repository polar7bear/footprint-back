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
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "convert_image_name", nullable = false)
    private String convertImageName;


    //나중에 review 완성되면 nullable false로 만들어야함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, name = "review_id")
    private Review review;

    @Builder
    private Image(String imageUrl, String convertImageName) {
        this.imageUrl = imageUrl;
        this.convertImageName = convertImageName;
    }
}
