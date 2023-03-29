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
                .cartId(this.getCartId())
                .productId(this.getPeriodOption().getProduct().getProductId())
                .periodOptionId(this.getPeriodOption().getPeriodOptionId())
                .cartPrice(this.getPeriodOption().getProduct().getProductPrice() * this.getCartQuantity())
                .productName(this.getPeriodOption().getProduct().getProductName())
                .periodOptionName(this.getPeriodOption().getOptionName())
                .productThumbnail(this.getPeriodOption().getProduct().getProductImages().get(0).getImagePath())
                .productContent(this.getPeriodOption().getProduct().getProductContent())
                .cartQuantity(this.getCartQuantity())
                .build();
    }
}
