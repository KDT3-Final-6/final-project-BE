package com.travel.cart.service.impl;

import com.travel.cart.dto.request.CartAddDTO;
import com.travel.cart.dto.request.CartDeleteListDTO;
import com.travel.cart.entity.Cart;
import com.travel.cart.exception.CartException;
import com.travel.cart.exception.CartExceptionType;
import com.travel.cart.repository.CartRepository;
import com.travel.cart.service.CartService;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final PeriodOptionRepository periodOptionRepository;


    @Override
    public void addCart(CartAddDTO cartAddDTO, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        Product product = productRepository.findById(cartAddDTO.getProductId())
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        PeriodOption periodOption = periodOptionRepository.findByProductAndPeriodOptionId(product, cartAddDTO.getPeriodOptionId())
                .orElseThrow(() -> new ProductException(ProductExceptionType.PERIOD_OPTION_NOT_FOUND));

        cartRepository.save(Cart.builder()
                .member(member)
                .product(product)
                .periodOption(periodOption)
                .cartQuantity(cartAddDTO.getQuantity())
                .build());
    }

    @Override
    public void deleteCarts(CartDeleteListDTO cartDeleteListDTO, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                        .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<Cart> cartList = cartDeleteListDTO.getCartIds().stream()
                .map(cartDeleteDTO -> cartRepository.findByMemberAndCartId(member, cartDeleteDTO.getCartId())
                        .orElseThrow(() -> new CartException(CartExceptionType.CART_NOT_FOUND)))
                .collect(Collectors.toList());

        cartRepository.deleteAll(cartList);
    }
}
