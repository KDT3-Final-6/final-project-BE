package com.travel.post.service.impl;

import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.post.dto.request.ReviewCreateRequestDTO;
import com.travel.post.dto.request.ReviewUpdateRequestDTO;
import com.travel.post.entity.ReviewPost;
import com.travel.post.exception.PostException;
import com.travel.post.exception.PostExceptionType;
import com.travel.post.repository.ReviewRepository;
import com.travel.post.service.ReviewService;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.PurchasedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PurchasedProductRepository purchasedProductRepository;

    @Override
    public void createReview(ReviewCreateRequestDTO reviewCreateRequestDTO, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        PurchasedProduct purchasedProduct = purchasedProductRepository.findById(reviewCreateRequestDTO.getPurchasedProductId())
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        ReviewPost reviewPost = new ReviewPost(purchasedProduct.getPurchasedProductName(), reviewCreateRequestDTO.getContent(), member, purchasedProduct, reviewCreateRequestDTO.getScope());

        reviewRepository.save(reviewPost);
    }

    @Override
    public void updateReview(Long postId, ReviewUpdateRequestDTO reviewUpdateRequestDTO, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        ReviewPost reviewPost = reviewRepository.findByPostIdAndMember(postId, member)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        reviewPost.setPostContent(reviewUpdateRequestDTO.getContent());
        reviewPost.setScope(reviewUpdateRequestDTO.getScope());

        reviewRepository.save(reviewPost);
    }
}
