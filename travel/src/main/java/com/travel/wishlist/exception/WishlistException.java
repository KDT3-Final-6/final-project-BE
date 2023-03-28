package com.travel.wishlist.exception;

import com.travel.global.exception.CustomException;
import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WishlistException extends CustomException {

    private final WishlistExceptionType wishlistExceptionType;


    @Override
    public CustomExceptionType getCustomExceptionType() {
        return wishlistExceptionType;
    }
}
