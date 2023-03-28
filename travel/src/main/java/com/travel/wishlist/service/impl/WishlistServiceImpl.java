package com.travel.wishlist.service.impl;

import com.travel.global.response.PageResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.product.entity.Product;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.product.ProductRepository;
import com.travel.wishlist.dto.response.WishlistResponseDTO;
import com.travel.wishlist.entity.Wishlist;
import com.travel.wishlist.exception.WishlistException;
import com.travel.wishlist.exception.WishlistExceptionType;
import com.travel.wishlist.repository.WishlistRepository;
import com.travel.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Override
    public void addWishlist(Long productId, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        if (wishlistRepository.existsByMemberAndProduct(member, product)) {
            throw new WishlistException(WishlistExceptionType.ALREADY_IN_WISHLIST);
        }

        wishlistRepository.save(Wishlist.builder()
                        .member(member)
                        .product(product)
                        .build());
    }

    @Override
    public PageResponseDTO getWishlists(Pageable pageable, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                        .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<Wishlist> wishlists = wishlistRepository.findByMember(member);

        List<WishlistResponseDTO> wishlistResponseDTOList = wishlists.stream()
                .map(Wishlist::toResponseDTO)
                .collect(Collectors.toList());

        return new PageResponseDTO(new PageImpl<>(wishlistResponseDTOList, pageable, wishlistResponseDTOList.size()));
    }
}
