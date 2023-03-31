package com.travel.search.service;

import com.travel.global.response.PageResponseDTO;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    PageResponseDTO displayProductsByCategory(Pageable pageable, String categoryName, String sortTarget, String memberEmail);

    PageResponseDTO searchProducts(Pageable pageable, String keyword, String sortTarget, String memberEmail);

    PageResponseDTO searchQnAs(Pageable pageable, String qnAStatus, String inquiryType, String keyword);
}
