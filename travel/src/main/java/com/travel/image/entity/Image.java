package com.travel.image.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "image")
@DiscriminatorColumn
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

    @Builder
    public Image(String imageName, String imageOriginalName, String imagePath, String imageFormat, Long imageBytes) {
        this.imageName = imageName;
        this.imageOriginalName = imageOriginalName;
        this.imagePath = imagePath;
        this.imageFormat = imageFormat;
        this.imageBytes = imageBytes;
    }

    public Image(Image image) {
        this.imageName = image.getImageName();
        this.imageOriginalName = image.getImageOriginalName();
        this.imagePath = image.getImagePath();
        this.imageFormat = image.getImageFormat();
        this.imageBytes = image.getImageBytes();
    }

    public ProductImage toProductImage() {
        return new ProductImage(this);
    }

    public MemberImage toMemberImage() {
        return new MemberImage(this);
    }
}