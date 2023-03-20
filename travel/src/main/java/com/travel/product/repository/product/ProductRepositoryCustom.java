package com.travel.product.repository.product;

import com.travel.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> findAllWithCheckBox(Pageable pageable, Boolean includeSoldOut);
}
