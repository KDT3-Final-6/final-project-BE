package com.travel.wishlist.controller;

import com.travel.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{productId}") //url에만 값을 넣으면 되는데 굳이 포스트를 써야할까?
    public ResponseEntity<Void> addWishlist(@PathVariable Long productId, String userEmail) {
        wishlistService.addWishlist(productId, userEmail);

        return ResponseEntity.ok(null);
    }

}
