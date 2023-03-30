package com.travel.post.entity;


import com.travel.post.dto.response.QnAAdminResponseDTO;
import com.travel.post.dto.response.QnAResponseDTO;
import com.travel.product.entity.PurchasedProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "qna_product_post")
@DiscriminatorValue("QP")
public class QnAProductPost extends QnAPost {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchased_product_id")
    private PurchasedProduct purchasedProduct;

    public QnAProductPost(QnAPost qnAPost, PurchasedProduct purchasedProduct) {
        super(qnAPost.getPostTitle(), qnAPost.getPostContent(), qnAPost.getMember(), qnAPost.getInquiryType());
        this.purchasedProduct = purchasedProduct;
    }

    @Override
    public QnAResponseDTO toQnAResponseDTO(AnswerPost answerPost) {
        String answerContent = null;
        if (answerPost != null) {
            answerContent = answerPost.getAnswerContent();
        }

        return QnAResponseDTO.builder()
                .postId(this.getPostId())
                .postTitle(this.getPostTitle())
                .postContent(this.getPostContent())
                .inquiryType(this.getInquiryType().getKorean())
                .qnAStatus(this.getQnAStatus().getKorean())
                .answer(answerContent)
                .purchasedProductName(this.getPurchasedProduct().getPurchasedProductName())
                .createdDate(this.getCreatedDate())
                .build();
    }

    @Override
    public QnAAdminResponseDTO toQnAAdminResponseDTO(AnswerPost answerPost) {
        String answerContent = null;
        if (answerPost != null) {
            answerContent = answerPost.getAnswerContent();
        }

        return QnAAdminResponseDTO.builder()
                .postId(this.getPostId())
                .postTitle(this.getPostTitle())
                .postContent(this.getPostContent())
                .inquiryType(this.getInquiryType().getKorean())
                .qnAStatus(this.getQnAStatus().getKorean())
                .answer(answerContent)
                .purchasedProductName(this.getPurchasedProduct().getPurchasedProductName())
                .createdDate(this.getCreatedDate())
                .memberName(this.getMember().getMemberName())
                .build();
    }
}
