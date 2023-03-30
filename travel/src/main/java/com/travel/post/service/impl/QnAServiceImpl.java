package com.travel.post.service.impl;

import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.post.dto.QnACreateDTO;
import com.travel.post.entity.InquiryType;
import com.travel.post.entity.QnAPost;
import com.travel.post.entity.QnAProductPost;
import com.travel.post.exception.PostException;
import com.travel.post.exception.PostExceptionType;
import com.travel.post.repository.QnAProductRepository;
import com.travel.post.repository.QnARepository;
import com.travel.post.service.QnAService;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.PurchasedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final QnARepository qnARepository;
    private final QnAProductRepository qnAProductRepository;
    private final MemberRepository memberRepository;
    private final PurchasedProductRepository purchasedProductRepository;

    @Override
    public void createQnA(QnACreateDTO qnACreateDTO, String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        InquiryType inquiryType = getInquiryType(qnACreateDTO.getInquiryType());

        QnAPost qnAPost = new QnAPost(qnACreateDTO.getTitle(), qnACreateDTO.getContent(), member, inquiryType);

        if (inquiryType.equals(InquiryType.PRODUCT)) {
            if (qnACreateDTO.getPurchasedProductId() == null) {
                throw new PostException(PostExceptionType.PRODUCT_INQUIRY_REQUIRES_PRODUCT_NUM);
            }

            PurchasedProduct purchasedProduct = purchasedProductRepository.findById(qnACreateDTO.getPurchasedProductId())
                    .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

            qnAProductRepository.save(new QnAProductPost(qnAPost, purchasedProduct));
        } else {
            qnARepository.save(qnAPost);
        }
    }

    private InquiryType getInquiryType(String inquiry) {
        InquiryType inquiryType = Stream.of(InquiryType.values())
                .filter(type -> inquiry.equals(type.getKorean()))
                .findFirst()
                .orElse(null);

        if (inquiryType == null) {
            throw new PostException(PostExceptionType.INQUIRTY_TYPE_NOT_FOUND);
        }

        return inquiryType;
    }
}
