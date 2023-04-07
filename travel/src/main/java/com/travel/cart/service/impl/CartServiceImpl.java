package com.travel.cart.service.impl;

import com.travel.cart.dto.request.CartAddListDTO;
import com.travel.cart.dto.request.CartUpdateDTO;
import com.travel.cart.dto.response.CartResponseDTO;
import com.travel.cart.entity.Cart;
import com.travel.cart.exception.CartException;
import com.travel.cart.exception.CartExceptionType;
import com.travel.cart.repository.CartRepository;
import com.travel.cart.service.CartService;
import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.entity.Status;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final PeriodOptionRepository periodOptionRepository;
    private final ProductRepository productRepository;


    @Override
    public void addCart(CartAddListDTO cartAddListDTO, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<Cart> cartList = cartAddListDTO.getProductIds().stream()
                .map(addDTO -> {
                    PeriodOption periodOption = periodOptionRepository.findById(addDTO.getPeriodOptionId())
                            .orElseThrow(() -> new ProductException(ProductExceptionType.PERIOD_OPTION_NOT_FOUND));

                    Product product = productRepository.findById(periodOption.getProduct().getProductId())
                            .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

                    if (product.getProductStatus() != Status.FORSALE || periodOption.getPeriodOptionStatus() != Status.FORSALE) {
                        throw new CartException(CartExceptionType.PRODUCTS_CANNOT_BE_ADDED);
                    }

                    Cart cart = cartRepository.findByMemberAndPeriodOption(member, periodOption).orElse(null);
                    if (cart != null) {
                        cart.setCartQuantity(cart.getCartQuantity() + addDTO.getQuantity());
                        return cart;
                    }

                    return Cart.builder()
                            .member(member)
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

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cartResponseDTOList.size());
        if (start > end) {
            throw new GlobalException(GlobalExceptionType.PAGE_IS_EXCEEDED);
        }

        return new PageResponseDTO(new PageImpl<>(cartResponseDTOList.subList(start, end), pageable, cartResponseDTOList.size()));
    }

    @Override
    public void updateCart(Long cartId, CartUpdateDTO cartUpdateDTO, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberAndCartId(member, cartId)
                .orElseThrow(() -> new CartException(CartExceptionType.CART_NOT_FOUND));

        PeriodOption periodOption = periodOptionRepository.findById(cartUpdateDTO.getPeriodOptionId())
                .orElseThrow(() -> new ProductException(ProductExceptionType.PERIOD_OPTION_NOT_FOUND));

        cart.setPeriodOption(periodOption);
        cart.setCartQuantity(cartUpdateDTO.getQuantity());

        cartRepository.save(cart);
    }

    @Override
    public void deleteCarts(List<Long> cartIds, String userEmail) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        List<Cart> cartList = cartIds.stream()
                .map(cartId -> cartRepository.findByMemberAndCartId(member, cartId)
                        .orElseThrow(() -> new CartException(CartExceptionType.CART_NOT_FOUND)))
                .collect(Collectors.toList());

        cartRepository.deleteAll(cartList);
    }
}
