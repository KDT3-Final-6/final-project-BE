package com.travel.product.repository;

import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodOptionRepository extends JpaRepository<PeriodOption, Long> {

    Optional<PeriodOption> findByProductAndPeriodOptionId(Product product, Long periodOptionId);
}
