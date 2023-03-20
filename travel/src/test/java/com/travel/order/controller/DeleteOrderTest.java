package com.travel.order.controller;

import com.travel.member.entity.Member;
import com.travel.member.repository.MemberRepository;
import com.travel.order.entity.Order;
import com.travel.order.repository.OrderRepository;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.ProductRepository;
import com.travel.product.repository.PurchasedProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteOrderTest {

    @LocalServerPort
    private int port;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PeriodOptionRepository periodOptionRepository;
    @Autowired
    PurchasedProductRepository purchasedProductRepository;

    @AfterEach
    void clean() {
        orderRepository.deleteAll();
        purchasedProductRepository.deleteAll();
        PeriodOption periodOption = periodOptionRepository.findById(1L).get();
        periodOption.setSoldQuantity(0);
        periodOptionRepository.save(periodOption);
    }

    private Order getOrder(Long productId, Long periodOptionId, String memberEmail) {

        Member member = memberRepository.findByMemberEmail(memberEmail).get();
        Product product = productRepository.findById(productId).get();

        PeriodOption periodOption = periodOptionRepository.findById(periodOptionId).get();

        PurchasedProduct purchasedProduct = PurchasedProduct.builder()
                .product(product)
                .periodOption(periodOption)
                .build();

        List<PurchasedProduct> purchasedProductList = new ArrayList<>();
        purchasedProductList.add(purchasedProduct);

        return orderRepository.save(Order.builder()
                .member(member)
                .purchasedProducts(purchasedProductList)
                .build());
    }

    @Nested
    @DisplayName("성공 테스트")
    class successTest {
        @Test
        @DisplayName("주문 취소")
        void withdrawOrder() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            String memberEmail = "test@example.com";
            String userEmail = memberEmail;

            Order order = getOrder(productId, periodOptionId, memberEmail);

            Long orderId = order.getOrderId();

            String url = "http://localhost:" + port + "/orders/" + orderId+ "?userEmail=" + userEmail;

            //when
            ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(responseEntity.getBody()).isNull();

            List<Order> orderList = orderRepository.findAll();
            assertThat(orderList.get(0).getIsCanceled()).isTrue();
            assertThat(orderList.get(0).getMember().getMemberId()).isEqualTo(1L);
        }

    }

    @Nested
    @DisplayName("실패 테스트")
    class failedTest {
        @Test
        @DisplayName("없는 멤버로 주문 취소")
        void withdrawOrderNoMember() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            String memberEmail = "test@example.com";
            String userEmail = "anpch@example.com";

            Order order = getOrder(productId, periodOptionId, memberEmail);

            Long orderId = order.getOrderId() - 1L;

            String url = "http://localhost:" + port + "/orders/" + orderId + "?userEmail=" + userEmail;

            //when
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(responseEntity.getBody()).isEqualTo("존재하지 않는 멤버입니다.");

            List<Order> orderList = orderRepository.findAll();
            assertThat(orderList.get(0).getIsCanceled()).isFalse();
            assertThat(orderList.get(0).getMember().getMemberId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("없는 주문 취소")
        void withdrawNoOrder() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            String memberEmail = "test@example.com";
            String userEmail = memberEmail;

            Order order = getOrder(productId, periodOptionId, memberEmail);

            Long orderId = order.getOrderId() - 1L;

            String url = "http://localhost:" + port + "/orders/" + orderId+ "?userEmail=" + userEmail;

            //when
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(responseEntity.getBody()).isEqualTo("존재하지 않는 주문입니다.");

            List<Order> orderList = orderRepository.findAll();
            assertThat(orderList.get(0).getIsCanceled()).isFalse();
            assertThat(orderList.get(0).getMember().getMemberId()).isEqualTo(1L);
        }
    }
}
