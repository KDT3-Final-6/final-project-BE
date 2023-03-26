package com.travel.order.entity;

import com.travel.global.entity.BaseEntityWithModifiedDate;
import com.travel.member.entity.Member;
import com.travel.product.entity.PurchasedProduct;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends BaseEntityWithModifiedDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Column(name = "is_canceled")
    private Boolean isCanceled = false;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<PurchasedProduct> purchasedProducts = new ArrayList<>();

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Builder
    public Order(Member member, List<PurchasedProduct> purchasedProducts, PaymentMethod paymentMethod) {
        this.member = member;
        this.purchasedProducts = purchasedProducts;
        this.paymentMethod = paymentMethod;
    }

}