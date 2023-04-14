package com.travel.post.service.impl;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.order.repository.OrderRepository;
import com.travel.post.dto.request.ReviewCreateRequestDTO;
import com.travel.post.dto.request.ReviewUpdateRequestDTO;
import com.travel.post.dto.response.ReviewInBoardDTO;
import com.travel.post.dto.response.ReviewInMemberDTO;
import com.travel.post.dto.response.ReviewInProductDTO;
import com.travel.post.entity.ReviewPost;
import com.travel.post.exception.PostException;
import com.travel.post.exception.PostExceptionType;
import com.travel.post.repository.ReviewRepository;
import com.travel.post.service.ReviewService;
import com.travel.product.entity.Product;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.PurchasedProductRepository;
import com.travel.product.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PurchasedProductRepository purchasedProductRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public void createReview(ReviewCreateRequestDTO reviewCreateRequestDTO, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        PurchasedProduct purchasedProduct = purchasedProductRepository.findById(reviewCreateRequestDTO.getPurchasedProductId())
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        if (!orderRepository.existsByPurchasedProductsContainsAndMember(purchasedProduct, member)) {
            throw new PostException(PostExceptionType.NOT_THE_PRODUCT_ORDERED);
        } else if (reviewRepository.existsByPurchasedProduct(purchasedProduct)) {
            throw new PostException(PostExceptionType.CAN_ONLY_WRITE_ONE_REVIEW);
        }

        ReviewPost reviewPost = new ReviewPost(purchasedProduct.getPurchasedProductName(), reviewCreateRequestDTO.getContent(), member, purchasedProduct, reviewCreateRequestDTO.getScope());

        reviewRepository.save(reviewPost);
    }

    @Override
    public PageResponseDTO getReviews(Pageable pageable) {
        List<ReviewPost> reviewPostList = reviewRepository.findAll().stream()
                .filter(reviewPost -> !reviewPost.isCanceled())
                .sorted(Comparator.comparing(ReviewPost::getPostId).reversed())
                .collect(Collectors.toList());

        List<ReviewInBoardDTO> reviewInBoardDTOList = reviewPostList.stream()
                .map(ReviewPost::toReviewInBoardDTO)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewInBoardDTOList.size());
        if (start > end) {
            throw new GlobalException(GlobalExceptionType.PAGE_IS_EXCEEDED);
        }

        return new PageResponseDTO(new PageImpl<>(reviewInBoardDTOList.subList(start, end), pageable, reviewInBoardDTOList.size()));
    }

    @Override
    public PageResponseDTO getReviewsByMember(Pageable pageable, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<ReviewPost> reviewPostList = reviewRepository.findByMember(member).stream()
                .filter(reviewPost -> !reviewPost.isCanceled())
                .sorted(Comparator.comparing(ReviewPost::getPostId).reversed())
                .collect(Collectors.toList());

        List<ReviewInMemberDTO> reviewInMemberDTOList = reviewPostList.stream()
                .map(ReviewPost::toReviewInMemberDTO)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewInMemberDTOList.size());
        if (start > end) {
            throw new GlobalException(GlobalExceptionType.PAGE_IS_EXCEEDED);
        }

        return new PageResponseDTO(new PageImpl<>(reviewInMemberDTOList.subList(start, end), pageable, reviewInMemberDTOList.size()));
    }

    @Override
    public PageResponseDTO getReviewsByProduct(Pageable pageable, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        List<ReviewPost> reviewPostList = reviewRepository.findByPurchasedProductProduct(product).stream()
                .filter(reviewPost -> !reviewPost.isCanceled())
                .sorted(Comparator.comparing(ReviewPost::getPostId).reversed())
                .collect(Collectors.toList());

        List<ReviewInProductDTO> reviewInProductDTOList = reviewPostList.stream()
                .map(ReviewPost::toReviewInProductDTO)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewInProductDTOList.size());
        if (start > end) {
            throw new GlobalException(GlobalExceptionType.PAGE_IS_EXCEEDED);
        }

        return new PageResponseDTO(new PageImpl<>(reviewInProductDTOList.subList(start, end), pageable, reviewInProductDTOList.size()));
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

    @Override
    public void deleteReview(Long postId, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        ReviewPost reviewPost = reviewRepository.findByPostIdAndMember(postId, member)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        reviewPost.setCanceled(true);

        reviewRepository.save(reviewPost);
    }
}
