package com.travel.order.controller;

import com.travel.global.response.PageResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.repository.MemberRepository;
import com.travel.order.entity.Order;
import com.travel.order.repository.OrderRepository;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.PurchasedProductRepository;
import com.travel.product.repository.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetOrdersTest {

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

    private void getOrder(Long productId, Long periodOptionId, String memberEmail) {

        Member member = memberRepository.findByMemberEmail(memberEmail).get();
        Product product = productRepository.findById(productId).get();

        PeriodOption periodOption = periodOptionRepository.findById(periodOptionId).get();

        PurchasedProduct purchasedProduct = PurchasedProduct.builder()
                .product(product)
                .periodOption(periodOption)
                .build();

        List<PurchasedProduct> purchasedProductList = new ArrayList<>();
        purchasedProductList.add(purchasedProduct);

        Order order = Order.builder()
                .member(member)
                .purchasedProducts(purchasedProductList)
                .build();

        purchasedProductList.forEach(pP -> pP.setOrder(order));

        orderRepository.save(order);
    }

    @Nested
    @DisplayName("성공 테스트")
    class successTest {

        @Test
        @DisplayName("주문이 1개일 때")
        void oneOrder() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            String memberEmail = "test@example.com";
            int page = 1;

            getOrder(productId, periodOptionId, memberEmail);

            String url = "http://localhost:" + port + "/orders" + "?page=" + page + "&userEmail=" + memberEmail;

            //when
            ResponseEntity<PageResponseDTO> responseEntity = restTemplate.getForEntity(url, PageResponseDTO.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageResponseDTO pageResponseDTO = responseEntity.getBody();
            assertThat(pageResponseDTO.getTotalPages()).isSameAs(1);
            assertThat(pageResponseDTO.getTotalElements()).isSameAs(1L);
            assertThat(pageResponseDTO.getPageNumber()).isSameAs(page);
            assertThat(pageResponseDTO.getSize()).isSameAs(3);
            assertThat(pageResponseDTO.getContent()).isNotEmpty();
        }

        @Test
        @DisplayName("페이지가 2개일 때 1페이지")
        void page1() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            String memberEmail = "test@example.com";
            int page = 1;

            getOrder(productId, periodOptionId, memberEmail);
            getOrder(productId, periodOptionId, memberEmail);
            getOrder(productId, periodOptionId, memberEmail);
            getOrder(productId, periodOptionId, memberEmail);

            String url = "http://localhost:" + port + "/orders" + "?page=" + page + "&userEmail=" + memberEmail;

            //when
            ResponseEntity<PageResponseDTO> responseEntity = restTemplate.getForEntity(url, PageResponseDTO.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageResponseDTO pageResponseDTO = responseEntity.getBody();
            assertThat(pageResponseDTO.getTotalPages()).isSameAs(2);
            assertThat(pageResponseDTO.getTotalElements()).isSameAs(4L);
            assertThat(pageResponseDTO.getPageNumber()).isSameAs(page);
            assertThat(pageResponseDTO.getSize()).isSameAs(3);
            assertThat(pageResponseDTO.getContent()).isNotEmpty();
        }

        @Test
        @DisplayName("페이지가 2개일 때 2페이지")
        void page2() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            String memberEmail = "test@example.com";
            int page = 1;

            getOrder(productId, periodOptionId, memberEmail);
            getOrder(productId, periodOptionId, memberEmail);
            getOrder(productId, periodOptionId, memberEmail);
            getOrder(productId, periodOptionId, memberEmail);

            String url = "http://localhost:" + port + "/orders" + "?page=" + page + "&userEmail=" + memberEmail;

            //when
            ResponseEntity<PageResponseDTO> responseEntity = restTemplate.getForEntity(url, PageResponseDTO.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

            PageResponseDTO pageResponseDTO = responseEntity.getBody();
            assertThat(pageResponseDTO.getTotalPages()).isSameAs(2);
            assertThat(pageResponseDTO.getTotalElements()).isSameAs(4L);
            assertThat(pageResponseDTO.getPageNumber()).isSameAs(page);
            assertThat(pageResponseDTO.getSize()).isSameAs(3);
            assertThat(pageResponseDTO.getContent()).isNotEmpty();
        }
    }
}
