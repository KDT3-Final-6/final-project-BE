package com.travel.product.repository.product;

import com.travel.product.entity.Product;
import com.travel.product.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    Page<Product> findAllByOrderByProductIdDesc(Pageable pageable);

    List<Product> findByProductNameContaining(String title);

    List<Product> findByProductStatusNot(Status status);
}
