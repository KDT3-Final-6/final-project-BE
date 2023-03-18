package com.travel.product.repository;

import com.travel.product.entity.PeriodOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodOptionRepository extends JpaRepository<PeriodOption, Long> {
}
