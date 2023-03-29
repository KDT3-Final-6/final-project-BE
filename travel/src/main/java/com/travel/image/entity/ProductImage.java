package com.travel.image.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.product.entity.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_image")
public class ProductImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public ProductImage(Image image) {
        super(image);
    }
}
