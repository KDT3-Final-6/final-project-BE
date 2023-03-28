package com.travel.cart.repository;

import com.travel.cart.entity.Cart;
import com.travel.member.entity.Member;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMemberAndCartId(Member member, Long cartId);

    List<Cart> findByMember(Member member);

    Optional<Cart> findByMemberAndPeriodOption(Member member, PeriodOption periodOption);
}
