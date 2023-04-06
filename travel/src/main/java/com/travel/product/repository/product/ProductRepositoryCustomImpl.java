package com.travel.product.repository.product;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.product.entity.Product;
import com.travel.product.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.travel.product.entity.QProduct.product;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Product> findAllWithCheckBox(Pageable pageable, Boolean includeSoldOut) {
        QueryResults<Product> results = queryFactory.selectFrom(product)
                .where(productStatusRange(includeSoldOut))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.productId.desc())
                .fetchResults();

        List<Product> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private Predicate productStatusRange(Boolean includeSoldOut) {
        return includeSoldOut ? product.productStatus.in(Status.FORSALE, Status.SOLDOUT) : product.productStatus.eq(Status.FORSALE);
    }
}