package com.travel.order.service.impl;

import com.travel.member.entity.Member;
import com.travel.member.repository.MemberRepository;
import com.travel.order.dto.request.OrderCreateDTO;
import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.entity.Order;
import com.travel.order.repository.OrderRepository;
import com.travel.order.service.OrderService;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.ProductRepository;
import com.travel.product.repository.PurchasedProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final PurchasedProductRepository purchasedProductRepository;
    private final PeriodOptionRepository periodOptionRepository;

    @Override
    public void createOrder(OrderCreateListDTO orderCreateListDTO, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> null); //나중에 예외처리 하기

        List<OrderCreateDTO> createList = orderCreateListDTO.getProductIds();

        List<PurchasedProduct> purchasedProductList = createList.stream()
                .map(createDTO -> {
                    Product product = productRepository.findById(createDTO.getProductId())
                            .orElseThrow(() -> null);
                    PeriodOption periodOption = periodOptionRepository.findById(createDTO.getPeriodOptionId())
                            .orElseThrow(() -> null);

                    Integer periodOptionSoldQuantity = periodOption.getPeriodOptionSoldQuantity();
                    periodOption.setPeriodOptionSoldQuantity(periodOptionSoldQuantity + 1);
                    periodOptionRepository.save(periodOption);

                    return product.toPurchase(periodOption);
                })
                .collect(Collectors.toList());

        Order order = Order.builder()
                .member(member)
                .purchasedProducts(purchasedProductList)
                .build();

        purchasedProductList.forEach(purchasedProduct -> purchasedProduct.setOrder(order));

        orderRepository.save(order);
    }
}
