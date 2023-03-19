package com.travel.order.service.impl;

import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.order.dto.request.OrderCreateDTO;
import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.dto.response.OrderListResponseDTO;
import com.travel.order.dto.response.OrderResponseDTO;
import com.travel.order.entity.Order;
import com.travel.order.exception.OrderException;
import com.travel.order.exception.OrderExceptionType;
import com.travel.order.repository.OrderRepository;
import com.travel.order.service.OrderService;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
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
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND)); //나중에 예외 만들기

        List<OrderCreateDTO> createList = orderCreateListDTO.getProductIds();

        List<PurchasedProduct> purchasedProductList = createList.stream()
                .map(createDTO -> {
                    Integer quantity = createDTO.getQuantity();

                    Product product = productRepository.findById(createDTO.getProductId())
                            .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));
                    PeriodOption periodOption = periodOptionRepository.findById(createDTO.getPeriodOptionId())
                            .orElseThrow(() -> new ProductException(ProductExceptionType.PERIOD_OPTION_NOT_FOUND));

                    //만약에 상품에 해당하지 않은 옵션이 오면?
                    Integer periodOptionSoldQuantity = periodOption.getSoldQuantity();
                    if (periodOptionSoldQuantity + quantity > periodOption.getMaximumQuantity()) {
                        throw new OrderException(OrderExceptionType.MAX_CAPACITY_EXCEEDED);
                    }
                    periodOption.setSoldQuantity(periodOptionSoldQuantity + quantity);
                    periodOptionRepository.save(periodOption);


                    PurchasedProduct purchasedProduct = product.toPurchase(periodOption);
                    if (quantity > 1) {
                        purchasedProduct.setProductProductQuantity(quantity);
                    }

                    return purchasedProduct;
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
