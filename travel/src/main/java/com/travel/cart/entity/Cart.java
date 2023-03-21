package com.travel.cart.entity;

import com.travel.global.entity.BaseEntity;
import com.travel.member.entity.Member;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_option_id")
    private PeriodOption periodOption;

    @Column(name = "cart_quantity")
    private Integer cartQuantity;

    @Builder
    public Cart(Member member, Product product, PeriodOption periodOption, Integer cartQuantity) {
        this.member = member;
        this.product = product;
        this.periodOption = periodOption;
        this.cartQuantity = cartQuantity;
    }
}
