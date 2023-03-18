package com.travel.order.controller;

import com.travel.order.dto.request.OrderCreateDTO;
import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.entity.Order;
import com.travel.order.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    OrderRepository orderRepository;

    @AfterEach
    void clean() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 구매 성공")
    void createOrderSuccess() {
        //given
        Long productId = 1L;
        Long periodOptionId = 1L;

        OrderCreateDTO createDTO1 = new OrderCreateDTO();
        OrderCreateDTO createDTO2 = new OrderCreateDTO();
        createDTO1.setProductId(productId);
        createDTO1.setPeriodOptionId(periodOptionId);
        createDTO2.setProductId(productId);
        createDTO2.setPeriodOptionId(periodOptionId);

        List<OrderCreateDTO> createDTOList = new ArrayList<>();
        createDTOList.add(createDTO1);
        createDTOList.add(createDTO2);

        OrderCreateListDTO createListDTO = new OrderCreateListDTO();
        createListDTO.setProductIds(createDTOList);

        String url = "http://localhost:" + port + "/orders";

        //when
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, createListDTO, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList.get(0).getIsCanceled()).isFalse();
        assertThat(orderList.get(0).getMember().getMemberId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("상품 구매 실패 (없는 상품)")
    void createOrderFailed1() {
        //given
        Long productId = 0L;
        Long periodOptionId = 1L;

        OrderCreateDTO createDTO1 = new OrderCreateDTO();
        OrderCreateDTO createDTO2 = new OrderCreateDTO();
        createDTO1.setProductId(productId);
        createDTO1.setPeriodOptionId(periodOptionId);
        createDTO2.setProductId(productId);
        createDTO2.setPeriodOptionId(periodOptionId);

        List<OrderCreateDTO> createDTOList = new ArrayList<>();
        createDTOList.add(createDTO1);
        createDTOList.add(createDTO2);

        OrderCreateListDTO createListDTO = new OrderCreateListDTO();
        createListDTO.setProductIds(createDTOList);

        String url = "http://localhost:" + port + "/orders";

        //when
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, createListDTO, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("존재하지 않는 상품입니다.");

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).isEmpty();
    }

    @Test
    @DisplayName("상품 구매 실패 (없는 옵션)")
    void createOrderFailed2() {
        //given
        Long productId = 1L;
        Long periodOptionId = 0L;

        OrderCreateDTO createDTO1 = new OrderCreateDTO();
        OrderCreateDTO createDTO2 = new OrderCreateDTO();
        createDTO1.setProductId(productId);
        createDTO1.setPeriodOptionId(periodOptionId);
        createDTO2.setProductId(productId);
        createDTO2.setPeriodOptionId(periodOptionId);

        List<OrderCreateDTO> createDTOList = new ArrayList<>();
        createDTOList.add(createDTO1);
        createDTOList.add(createDTO2);

        OrderCreateListDTO createListDTO = new OrderCreateListDTO();
        createListDTO.setProductIds(createDTOList);

        String url = "http://localhost:" + port + "/orders";

        //when
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, createListDTO, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("존재하지 않는 기간 옵션입니다.");

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).isEmpty();
    }
}