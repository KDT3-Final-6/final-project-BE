package com.travel.image.entity;

import com.travel.product.entity.Product;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_original_name")
    private String imageOriginalName;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_format")
    private String imageFormat;

    @Column(name = "image_bytes")
    private Long imageBytes;


    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public Image(String imageName, String imageOriginalName, String imagePath, String imageFormat, Long imageBytes) {
        this.imageName = imageName;
        this.imageOriginalName = imageOriginalName;
        this.imagePath = imagePath;
        this.imageFormat = imageFormat;
        this.imageBytes = imageBytes;
    }
}