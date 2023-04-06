package com.travel.order.service.impl;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.order.dto.request.OrderApproveDTO;
import com.travel.order.dto.request.OrderCreateDTO;
import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.dto.request.OrderNonMemberCreateDTO;
import com.travel.order.dto.response.OrderAdminResponseDTO;
import com.travel.order.dto.response.OrderListAdminResponseDTO;
import com.travel.order.dto.response.OrderListResponseDTO;
import com.travel.order.dto.response.OrderResponseDTO;
import com.travel.order.entity.Order;
import com.travel.order.entity.OrderStatus;
import com.travel.order.entity.PaymentMethod;
import com.travel.order.exception.OrderException;
import com.travel.order.exception.OrderExceptionType;
import com.travel.order.repository.OrderRepository;
import com.travel.order.service.OrderService;
import com.travel.post.repository.ReviewRepository;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.entity.Status;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.PurchasedProductRepository;
import com.travel.product.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final ReviewRepository reviewRepository;

    @Override
    public void createOrder(OrderCreateListDTO orderCreateListDTO, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<PurchasedProduct> purchasedProductList = getPurchasedProducts(orderCreateListDTO.getProductIds());

        Order order = Order.builder()
                .member(member)
                .purchasedProducts(purchasedProductList)
                .paymentMethod(getPaymentMethod(orderCreateListDTO.getPaymentMethod()))
                .build();

        purchasedProductList.forEach(purchasedProduct -> purchasedProduct.setOrder(order));

        orderRepository.save(order);
    }

    @Override
    public PageResponseDTO getOrders(Pageable pageable, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<Order> orderList = orderRepository.findByMember(member).stream()
                .sorted(Comparator.comparing(Order::getOrderId).reversed())
                .collect(Collectors.toList());

        List<OrderListResponseDTO> orderListResponseDTOS = orderList.stream()
                .map(order -> {
                    List<OrderResponseDTO> orderResponseDTOList = purchasedProductRepository.findByOrder(order).stream()
                            .map(purchasedProduct -> {
                                boolean hasReview = reviewRepository.existsByPurchasedProduct(purchasedProduct);

                                return purchasedProduct.toOrderResponseDTO(hasReview);
                            })
                            .collect(Collectors.toList());

                    return OrderListResponseDTO.builder()
                            .orderList(orderResponseDTOList)
                            .order(order)
                            .build();
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), orderListResponseDTOS.size());
        if (start > end) {
            throw new GlobalException(GlobalExceptionType.PAGE_IS_EXCEEDED);
        }

        return new PageResponseDTO(new PageImpl<>(orderListResponseDTOS.subList(start, end), pageable, orderListResponseDTOS.size()));
    }

    @Override
    public void deleteOrder(Long orderId, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND)); //나중에 예외 만들기

        Order order = orderRepository.findByOrderIdAndMember(orderId, member)
                .orElseThrow(() -> new OrderException(OrderExceptionType.ORDER_NOT_FOUND));

        order.setOrderStatus(OrderStatus.WITHDRAW_ORDER);

        orderRepository.save(order);
    }

    @Override
    public PageResponseDTO getOrdersAdmin(Pageable pageable, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        if (!member.getRoles().contains("ROLE_ADMIN")) {
            throw new MemberException(MemberExceptionType.MEMBER_IS_NOT_ADMIN);
        }

        List<Order> orderList = orderRepository.findAll().stream()
                .sorted(Comparator.comparing(Order::getOrderId).reversed())
                .collect(Collectors.toList());

        List<OrderListAdminResponseDTO> orderListResponseDTOS = orderList.stream()
                .map(order -> {
                    Member user = order.getMember();
                    List<OrderAdminResponseDTO> orderResponseDTOList = purchasedProductRepository.findByOrder(order).stream()
                            .map(purchasedProduct -> purchasedProduct.toOrderAdminResponseDTO(user))
                            .collect(Collectors.toList());

                    return OrderListAdminResponseDTO.builder()
                            .orderList(orderResponseDTOList)
                            .order(order)
                            .build();
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), orderListResponseDTOS.size());
        if (start > end) {
            throw new GlobalException(GlobalExceptionType.PAGE_IS_EXCEEDED);
        }

        return new PageResponseDTO(new PageImpl<>(orderListResponseDTOS.subList(start, end), pageable, orderListResponseDTOS.size()));
    }

    @Override
    public void approveOrder(Long orderId, OrderApproveDTO orderApproveDTO, String userEmail) {
        Member admin = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        if (!admin.getRoles().contains("ROLE_ADMIN")) {
            throw new MemberException(MemberExceptionType.MEMBER_IS_NOT_ADMIN);
        }

        Member member = memberRepository.findById(orderApproveDTO.getMemberId())
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        Order order = orderRepository.findByOrderIdAndMember(orderId, member)
                .orElseThrow(() -> new OrderException(OrderExceptionType.ORDER_NOT_FOUND));

        order.setOrderStatus(OrderStatus.COMPLETE_PAYMENT);

        orderRepository.save(order);
    }

    @Override
    public void createOrderNonMember(OrderNonMemberCreateDTO orderNonMemberCreateDTO) {
        String memberEmail = null;
        if (orderNonMemberCreateDTO.getMemberEmail() != null) {
            memberEmail = orderNonMemberCreateDTO.getMemberEmail();
        }

        Member member = Member.builder()
                .memberName(orderNonMemberCreateDTO.getMemberName())
                .memberPhone(orderNonMemberCreateDTO.getMemberPhone())
                .memberEmail(memberEmail)
                .build();

        member.setNonMembers(true);

        memberRepository.save(member);


        List<PurchasedProduct> purchasedProductList = getPurchasedProducts(orderNonMemberCreateDTO.getProductIds());

        Order order = Order.builder()
                .member(member)
                .purchasedProducts(purchasedProductList)
                .paymentMethod(getPaymentMethod(orderNonMemberCreateDTO.getPaymentMethod()))
                .build();

        purchasedProductList.forEach(purchasedProduct -> purchasedProduct.setOrder(order));

        orderRepository.save(order);
    }

    private List<PurchasedProduct> getPurchasedProducts(List<OrderCreateDTO> orderCreateDTOList) {
        return orderCreateDTOList.stream()
                .map(createDTO -> {
                    Integer quantity = createDTO.getQuantity();

                    PeriodOption periodOption = periodOptionRepository.findById(createDTO.getPeriodOptionId())
                            .orElseThrow(() -> new ProductException(ProductExceptionType.PERIOD_OPTION_NOT_FOUND));

                    Product product = productRepository.findById(periodOption.getProduct().getProductId())
                            .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

                    if (product.getProductStatus() != Status.FORSALE || periodOption.getPeriodOptionStatus() != Status.FORSALE) {
                        throw new OrderException(OrderExceptionType.PRODUCTS_CANNOT_BE_ORDERED);
                    }

                    return updateQuantity(quantity, product, periodOption);
                })
                .collect(Collectors.toList());
    }

    private PurchasedProduct updateQuantity(Integer quantity, Product product, PeriodOption periodOption) {
        Integer periodOptionSoldQuantity = periodOption.getSoldQuantity();
        if (periodOptionSoldQuantity + quantity > periodOption.getMaximumQuantity()) {
            throw new OrderException(OrderExceptionType.MAX_CAPACITY_EXCEEDED);
        } else if (periodOptionSoldQuantity + quantity == periodOption.getMaximumQuantity()) {
            periodOption.setPeriodOptionStatus(Status.SOLDOUT);
        }
        periodOption.setSoldQuantity(periodOptionSoldQuantity + quantity);
        periodOptionRepository.save(periodOption);

        return product.toPurchase(periodOption, quantity);
    }

    private PaymentMethod getPaymentMethod(String paymentMethod) {
        return Stream.of(PaymentMethod.values())
                .filter(payment -> paymentMethod.equals(payment.getKorean()))
                .findFirst()
                .orElse(null);
    }
}
