package com.travel.cart.entity;

import com.travel.cart.dto.response.CartResponseDTO;
import com.travel.global.entity.BaseEntity;
import com.travel.member.entity.Member;
import com.travel.product.entity.PeriodOption;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cart")
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_option_id")
    private PeriodOption periodOption;

    @Setter
    @Column(name = "cart_quantity")
    private Integer cartQuantity;

    @Builder
    public Cart(Member member, PeriodOption periodOption, Integer cartQuantity) {
        this.member = member;
        this.periodOption = periodOption;
        this.cartQuantity = cartQuantity;
    }

    public CartResponseDTO toCartResponseDTO() {
        return CartResponseDTO.builder()
                .cartId(this.cartId)
                .productId(this.periodOption.getProduct().getProductId())
                .periodOptionId(this.periodOption.getPeriodOptionId())
                .productPrice(this.periodOption.getProduct().getProductPrice())
                .productName(this.periodOption.getProduct().getProductName())
                .periodOptionName(this.periodOption.getOptionName())
                .productThumbnail(this.periodOption.getProduct().getProductImages().get(0).getImagePath())
                .productContent(this.periodOption.getProduct().getProductContent())
                .cartQuantity(this.cartQuantity)
                .productStatus(this.periodOption.getProduct().getProductStatus().getKorean())
                .periodOptionStatus(this.periodOption.getPeriodOptionStatus().getKorean())
                .build();
    }
}
