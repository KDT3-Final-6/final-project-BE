package com.travel.wishlist.controller;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {

    public static final int PAGE_SIZE = 10;

    private final WishlistService wishlistService;

    @PostMapping("/{productId}") //url에만 값을 넣으면 되는데 굳이 포스트를 써야할까?
    public ResponseEntity<Void> addWishlist(@PathVariable Long productId, Authentication authentication) {
        wishlistService.addWishlist(productId, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getWishlists(@RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO wishlists = wishlistService.getWishlists(pageRequest, authentication.getName());

        return ResponseEntity.ok(wishlists);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable Long productId, Authentication authentication) {
        wishlistService.deleteWishlist(productId, authentication.getName());

        return ResponseEntity.ok(null);
    }
}
