package com.travel.cart.repository;

import com.travel.cart.entity.Cart;
import com.travel.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMemberAndId(Member member, Long cartId);
}
