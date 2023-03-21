package com.travel.cart.entity;

import com.travel.cart.dto.response.CartResponseDTO;
import com.travel.global.entity.BaseEntity;
import com.travel.member.entity.Member;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
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
    @JoinColumn(name = "product_id")
    private Product product;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_option_id")
    private PeriodOption periodOption;

    @Setter
    @Column(name = "cart_quantity")
    private Integer cartQuantity;

    @Builder
    public Cart(Member member, Product product, PeriodOption periodOption, Integer cartQuantity) {
        this.member = member;
        this.product = product;
        this.periodOption = periodOption;
        this.cartQuantity = cartQuantity;
    }

    public CartResponseDTO toCartResponseDTO() {
        return CartResponseDTO.builder()
                .cartId(this.getCartId())
                .productId(this.getProduct().getProductId())
                .periodOptionId(this.getPeriodOption().getPeriodOptionId())
                .cartPrice(this.getProduct().getProductPrice() * this.getCartQuantity())
                .productName(this.getProduct().getProductName())
                .periodOptionName(this.getPeriodOption().getOptionName())
                .productThumbnail(this.getProduct().getProductThumbnail())
                .productContent(this.getProduct().getProductContent())
                .cartQuantity(this.getCartQuantity())
                .build();
    }
}
