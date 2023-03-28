package com.travel.wishlist.service;

import com.travel.global.response.PageResponseDTO;
import com.travel.wishlist.dto.response.WishlistResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishlistService {

    void addWishlist(Long productId, String userEmail);

    PageResponseDTO getWishlists(Pageable pageable, String userEmail);
}
