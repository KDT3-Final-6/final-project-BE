package com.travel.order.controller;

import com.travel.member.repository.MemberRepository;
import com.travel.order.dto.request.OrderCreateDTO;
import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.entity.Order;
import com.travel.order.repository.OrderRepository;
import com.travel.product.entity.PeriodOption;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateOrderTest {

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

    OrderCreateListDTO getOrderCreateListDTO(Long productId, Long periodOptionId, Integer quantity) {
        OrderCreateDTO createDTO1 = new OrderCreateDTO();
        createDTO1.setProductId(productId);
        createDTO1.setPeriodOptionId(periodOptionId);
        createDTO1.setQuantity(quantity);

        List<OrderCreateDTO> createDTOList = new ArrayList<>();
        createDTOList.add(createDTO1);

        OrderCreateListDTO createListDTO = new OrderCreateListDTO();
        createListDTO.setProductIds(createDTOList);

        return createListDTO;
    }

    @Nested
    @DisplayName("성공 테스트")
    class successTest {

        @Test
        @DisplayName("상품 1개 주문")
        void buy1Products() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            Integer quantity = 3;
            String userEmail = "test@example.com";

            OrderCreateListDTO createListDTO = getOrderCreateListDTO(productId, periodOptionId, quantity);

            String url = "http://localhost:" + port + "/orders" + "?userEmail=" + userEmail;

            //when
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(url, createListDTO, Void.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(responseEntity.getBody()).isNull();

            List<Order> orderList = orderRepository.findAll();
            assertThat(orderList.size()).isSameAs(1);
        }

        @Test
        @DisplayName("상품 2개 주문")
        void buy2Products() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            Integer quantity = 3;
            String userEmail = "test@example.com";

            OrderCreateListDTO createListDTO1 = getOrderCreateListDTO(productId, periodOptionId, quantity);
            OrderCreateListDTO createListDTO2 = getOrderCreateListDTO(productId, periodOptionId, quantity);
            OrderCreateListDTO createListDTOMerge = new OrderCreateListDTO();

            List<OrderCreateDTO> createDTOList = new ArrayList<>();
            createDTOList.add(createListDTO1.getProductIds().get(0));
            createDTOList.add(createListDTO2.getProductIds().get(0));
            createListDTOMerge.setProductIds(createDTOList);

            String url = "http://localhost:" + port + "/orders" + "?userEmail=" + userEmail;

            //when
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(url, createListDTOMerge, Void.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(responseEntity.getBody()).isNull();

            List<Order> orderList = orderRepository.findAll();
            assertThat(orderList.size()).isSameAs(1);
        }
    }

    @Nested
    @DisplayName("실패 테스트")
    class failedTest {

        @Test
        @DisplayName("없는 멤버로 상품 주문")
        void buyProductNoMember() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            Integer quantity = 3;
            String userEmail = "test2@example.com";

            OrderCreateListDTO createListDTO = getOrderCreateListDTO(productId, periodOptionId, quantity);

            String url = "http://localhost:" + port + "/orders" + "?userEmail=" + userEmail;

            //when
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, createListDTO, String.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(responseEntity.getBody()).isEqualTo("존재하지 않는 멤버입니다.");

            List<Order> orderList = orderRepository.findAll();
            assertThat(orderList).isEmpty();
        }

        @Test
        @DisplayName("없는 상품 주문")
        void buyNoProduct() {
            //given
            Long productId = 0L;
            Long periodOptionId = 1L;
            Integer quantity = 3;
            String userEmail = "test@example.com";

            OrderCreateListDTO createListDTO = getOrderCreateListDTO(productId, periodOptionId, quantity);

            String url = "http://localhost:" + port + "/orders" + "?userEmail=" + userEmail;

            //when
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, createListDTO, String.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(responseEntity.getBody()).isEqualTo("존재하지 않는 상품입니다.");

            List<Order> orderList = orderRepository.findAll();
            assertThat(orderList).isEmpty();
        }

        @Test
        @DisplayName("없는 옵션 주문")
        void buyNoOption() {
            //given
            Long productId = 1L;
            Long periodOptionId = 0L;
            Integer quantity = 3;
            String userEmail = "test@example.com";

            OrderCreateListDTO createListDTO = getOrderCreateListDTO(productId, periodOptionId, quantity);


            String url = "http://localhost:" + port + "/orders" + "?userEmail=" + userEmail;

            //when
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, createListDTO, String.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(responseEntity.getBody()).isEqualTo("존재하지 않는 기간 옵션입니다.");

            List<Order> orderList = orderRepository.findAll();
            assertThat(orderList).isEmpty();
        }

        @Test
        @DisplayName("최대 인원 초과된 옵션 주문")
        void buyMaxProduct() {
            //given
            Long productId = 1L;
            Long periodOptionId = 1L;
            Integer quantity = 3;
            String userEmail = "test@example.com";

            OrderCreateListDTO createListDTO = getOrderCreateListDTO(productId, periodOptionId, quantity);

            PeriodOption periodOption = periodOptionRepository.findById(periodOptionId).get();
            Integer maximumQuantity = periodOption.getMaximumQuantity();
            periodOption.setSoldQuantity(maximumQuantity);
            periodOptionRepository.save(periodOption);

            String url = "http://localhost:" + port + "/orders" + "?userEmail=" + userEmail;

            //when
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, createListDTO, String.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(responseEntity.getBody()).isEqualTo("최대 인원을 초과합니다.");

            List<Order> orderList = orderRepository.findAll();
            assertThat(orderList).isEmpty();
        }
    }
}
