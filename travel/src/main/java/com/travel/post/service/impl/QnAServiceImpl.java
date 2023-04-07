package com.travel.post.service.impl;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.post.dto.request.QnAAnswerRequestDTO;
import com.travel.post.dto.request.QnARequestDTO;
import com.travel.post.dto.response.QnAAdminResponseDTO;
import com.travel.post.dto.response.QnAResponseDTO;
import com.travel.post.entity.*;
import com.travel.post.exception.PostException;
import com.travel.post.exception.PostExceptionType;
import com.travel.post.repository.AnswerRepository;
import com.travel.post.repository.PostRepository;
import com.travel.post.repository.QnAProductRepository;
import com.travel.post.repository.qnapost.QnARepository;
import com.travel.post.service.QnAService;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.PurchasedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final QnARepository qnARepository;
    private final QnAProductRepository qnAProductRepository;
    private final MemberRepository memberRepository;
    private final PurchasedProductRepository purchasedProductRepository;
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;

    @Override
    public void createQnA(QnARequestDTO qnARequestDTO, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        InquiryType inquiryType = getInquiryType(qnARequestDTO.getInquiryType());

        QnAPost qnAPost = new QnAPost(qnARequestDTO.getTitle(), qnARequestDTO.getContent(), member, inquiryType);

        if (inquiryType.equals(InquiryType.PRODUCT)) {
            if (qnARequestDTO.getPurchasedProductId() == null) {
                throw new PostException(PostExceptionType.PRODUCT_INQUIRY_REQUIRES_PRODUCT_NUM);
            }

            PurchasedProduct purchasedProduct = purchasedProductRepository.findById(qnARequestDTO.getPurchasedProductId())
                    .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

            qnAProductRepository.save(new QnAProductPost(qnAPost, purchasedProduct));
        } else {
            qnARepository.save(qnAPost);
        }
    }

    @Override
    public PageResponseDTO getQnAs(Pageable pageable, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<QnAPost> qnAPostList = qnARepository.findByMember(member).stream()
                .filter(qnAPost -> !qnAPost.isCanceled())
                .sorted(Comparator.comparing(QnAPost::getPostId).reversed())
                .collect(Collectors.toList());

        List<QnAResponseDTO> qnAPostDTOList = qnAPostList.stream()
                .map(qnAPost -> {
                    AnswerPost answerPost = answerRepository.findByQnAPost(qnAPost)
                            .orElse(null);

                    QnAProductPost qnAProductPost = qnAProductRepository.findById(qnAPost.getPostId())
                            .orElse(null);

                    if (qnAProductPost != null) {
                        return qnAProductPost.toQnAResponseDTO(answerPost);
                    }

                    return qnAPost.toQnAResponseDTO(answerPost);
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), qnAPostDTOList.size());
        if (start > end) {
            throw new GlobalException(GlobalExceptionType.PAGE_IS_EXCEEDED);
        }

        return new PageResponseDTO(new PageImpl<>(qnAPostDTOList.subList(start, end), pageable, qnAPostDTOList.size()));
    }

    @Override
    public void deleteQnA(Long postId, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        Post post = postRepository.findByMemberAndPostId(member, postId)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        post.setCanceled(true);

        postRepository.save(post);
    }

    @Override
    public PageResponseDTO getQnAsAdmin(Pageable pageable, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        if (!member.getRoles().contains("ROLE_ADMIN")) {
            throw new MemberException(MemberExceptionType.MEMBER_IS_NOT_ADMIN);
        }

        List<QnAPost> qnAPostList = qnARepository.findAll().stream()
                .filter(qnAPost -> !qnAPost.isCanceled())
                .sorted(Comparator.comparing(QnAPost::getPostId).reversed())
                .collect(Collectors.toList());

        List<QnAAdminResponseDTO> qnAPostDTOList = qnAPostList.stream()
                .map(qnAPost -> {
                    AnswerPost answerPost = answerRepository.findByQnAPost(qnAPost)
                            .orElse(null);

                    QnAProductPost qnAProductPost = qnAProductRepository.findById(qnAPost.getPostId())
                            .orElse(null);

                    if (qnAProductPost != null) {
                        return qnAProductPost.toQnAAdminResponseDTO(answerPost);
                    }

                    return qnAPost.toQnAAdminResponseDTO(answerPost);
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), qnAPostDTOList.size());
        if (start > end) {
            throw new GlobalException(GlobalExceptionType.PAGE_IS_EXCEEDED);
        }

        return new PageResponseDTO(new PageImpl<>(qnAPostDTOList.subList(start, end), pageable, qnAPostDTOList.size()));
    }

    @Override
    public void createAnswer(QnAAnswerRequestDTO qnAAnswerRequestDTO, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        if (!member.getRoles().contains("ROLE_ADMIN")) {
            throw new MemberException(MemberExceptionType.MEMBER_IS_NOT_ADMIN);
        }

        QnAPost qnAPost = qnARepository.findById(qnAAnswerRequestDTO.getPostId())
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        qnAPost.setQnAStatus(QnAStatus.ANSWER_COMPLETE);

        answerRepository.save(AnswerPost.builder()
                .answerContent(qnAAnswerRequestDTO.getContent())
                .qnAPost(qnAPost)
                .build());
    }

    @Override
    public void updateAnswer(QnAAnswerRequestDTO qnAAnswerRequestDTO) {
        QnAPost qnAPost = qnARepository.findById(qnAAnswerRequestDTO.getPostId())
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        AnswerPost answerPost = answerRepository.findByQnAPost(qnAPost)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        answerPost.setAnswerContent(qnAAnswerRequestDTO.getContent());

        answerRepository.save(answerPost);
    }

    private InquiryType getInquiryType(String inquiry) {

        return Stream.of(InquiryType.values())
                .filter(type -> inquiry.equals(type.getKorean()))
                .findFirst()
                .orElseThrow(() -> new PostException(PostExceptionType.INQUIRTY_TYPE_NOT_FOUND));
    }
}
