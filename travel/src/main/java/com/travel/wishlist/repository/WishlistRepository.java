package com.travel.wishlist.repository;

import com.travel.member.entity.Member;
import com.travel.product.entity.Product;
import com.travel.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByMemberAndProduct(Member member, Product product);
}
