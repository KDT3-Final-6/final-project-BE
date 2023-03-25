package com.travel.cart.service.impl;

import com.travel.cart.dto.request.CartAddListDTO;
import com.travel.cart.dto.request.CartDeleteListDTO;
import com.travel.cart.dto.request.CartUpdateDTO;
import com.travel.cart.dto.response.CartResponseDTO;
import com.travel.cart.entity.Cart;
import com.travel.cart.exception.CartException;
import com.travel.cart.exception.CartExceptionType;
import com.travel.cart.repository.CartRepository;
import com.travel.cart.service.CartService;
import com.travel.global.response.PageResponseDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public void addCart(CartAddListDTO cartAddListDTO, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<Cart> cartList = cartAddListDTO.getProductIds().stream()
                .map(addDTO -> {
                    Product product = productRepository.findById(addDTO.getProductId())
                            .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));
                    PeriodOption periodOption = periodOptionRepository.findByProductAndPeriodOptionId(product, addDTO.getPeriodOptionId())
                            .orElseThrow(() -> new ProductException(ProductExceptionType.PERIOD_OPTION_NOT_FOUND));

                    Cart cart = cartRepository.findByMemberAndProductAndPeriodOption(member, product, periodOption).orElse(null);
                    if (cart != null) {
                        cart.setCartQuantity(cart.getCartQuantity() + addDTO.getQuantity());
                        return cart;
                    }

                    return Cart.builder()
                            .member(member)
                            .product(product)
                            .periodOption(periodOption)
                            .cartQuantity(addDTO.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        cartRepository.saveAll(cartList);
    }

    @Override
    public PageResponseDTO getCarts(Pageable pageable, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<Cart> cartList = cartRepository.findByMember(member);

        List<CartResponseDTO> cartResponseDTOList = cartList.stream()
                .map(Cart::toCartResponseDTO)
                .collect(Collectors.toList());

        return new PageResponseDTO(new PageImpl<>(cartResponseDTOList, pageable, cartResponseDTOList.size()));
    }

    @Override
    public void updateCart(Long cartId, CartUpdateDTO cartUpdateDTO, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberAndCartId(member, cartId)
                .orElseThrow(() -> new CartException(CartExceptionType.CART_NOT_FOUND));

        Product product = productRepository.findById(cartUpdateDTO.getProductId())
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        PeriodOption periodOption = periodOptionRepository.findByProductAndPeriodOptionId(product, cartUpdateDTO.getPeriodOptionId())
                .orElseThrow(() -> new ProductException(ProductExceptionType.PERIOD_OPTION_NOT_FOUND));

        cart.setProduct(product);
        cart.setPeriodOption(periodOption);
        cart.setCartQuantity(cartUpdateDTO.getQuantity());

        cartRepository.save(cart);
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
