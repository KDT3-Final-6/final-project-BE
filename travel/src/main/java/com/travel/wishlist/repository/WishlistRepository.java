package com.travel.wishlist.repository;

import com.travel.member.entity.Member;
import com.travel.product.entity.Product;
import com.travel.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByMemberAndProduct(Member member, Product product);

    List<Wishlist> findByMember(Member member);

    Optional<Wishlist> findByWishlistIdAndMember(Long wishlistId, Member member);
}
