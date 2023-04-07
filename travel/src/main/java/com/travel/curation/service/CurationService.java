package com.travel.curation.service;

import com.travel.curation.repository.CurationRepository;
import com.travel.global.response.PageResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.product.dto.response.ProductListGetResponseDTO;
import com.travel.product.entity.Product;
import com.travel.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurationService {

    private final MemberRepository memberRepository;
    private final CurationRepository curationRepository;
    private final WishlistRepository wishlistRepository;


    public PageResponseDTO defaultCuration(String userEmail, Pageable pageable, String season, String district, String theme) {

        Page<Product> products = curationRepository.findAllWithThreeCategories(pageable, season, district, theme);

        Page<ProductListGetResponseDTO> page = productsToDTO(products, pageable, userEmail);

        return new PageResponseDTO(page);
    }

    public PageResponseDTO detailCurationByTarget(String userEmail, Pageable pageable, String target) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
        if (member.getSurvey() == null)
            throw new MemberException(MemberExceptionType.SURVEY_NOT_EXISTS);

        if (target.equals("season")) {

            Page<Product> products = curationRepository.findAllWithSeason(pageable, member.getSurvey());

            Page<ProductListGetResponseDTO> page = productsToDTO(products, pageable, userEmail);

            return new PageResponseDTO(page);
        }

        Page<Product> products = curationRepository.findAllWithTarget(pageable, member.getSurvey(), target);

        Page<ProductListGetResponseDTO> page = productsToDTO(products, pageable, userEmail);

        return new PageResponseDTO(page);
    }

    public PageResponseDTO CurationByGroupAndThemes(String userEmail, Pageable pageable, String group, List<String> conceptList) {

        conceptList.removeIf(String::isBlank);

        Page<Product> products = curationRepository.findAllWithGroupAndThemes(pageable, group, conceptList);

        Page<ProductListGetResponseDTO> page = productsToDTO(products, pageable, userEmail);

        return new PageResponseDTO(page);
    }

    private boolean isExistsByMemberAndProduct(String memberEmail, Product product) {
        boolean existsByMemberAndProduct = false;
        if (memberEmail != null) {
            Member member = memberRepository.findByMemberEmail(memberEmail)
                    .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
            existsByMemberAndProduct = wishlistRepository.existsByMemberAndProduct(member, product);
        }
        return existsByMemberAndProduct;
    }

    private Page<ProductListGetResponseDTO> productsToDTO(Page<Product> products, Pageable pageable, String userEmail) {

        return new PageImpl<>(
                products.getContent().stream()
                        .map(product -> new ProductListGetResponseDTO(product, isExistsByMemberAndProduct(userEmail, product)))
                        .collect(toList()), pageable, products.getTotalElements());
    }
}
