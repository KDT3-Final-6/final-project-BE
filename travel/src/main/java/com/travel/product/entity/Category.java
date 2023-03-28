package com.travel.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @OneToMany(mappedBy = "category")
    private List<ProductCategory> productCategories = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    @Column(name = "category_name")
    private String categoryName;

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
