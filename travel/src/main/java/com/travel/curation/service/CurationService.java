package com.travel.curation.service;

import com.travel.curation.repository.CurationRepository;
import com.travel.global.response.PageResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.product.dto.response.ProductListGetResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurationService {

    private final MemberRepository memberRepository;
    private final CurationRepository curationRepository;

    public PageResponseDTO defaultCuration(Pageable pageable, String season, String district, String theme) {

        return new PageResponseDTO(curationRepository.findAllWithThreeCategories(pageable, season, district, theme)
                .map(ProductListGetResponseDTO::new));
    }

    public PageResponseDTO detailCurationByTarget(String userEmail, Pageable pageable, String target) {
        Member member = memberRepository.findByMemberEmail(userEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
        if (member.getSurvey() == null)
            throw new MemberException(MemberExceptionType.SURVEY_NOT_EXISTS);

        if (target.equals("season")) {
            return new PageResponseDTO(curationRepository.findAllWithSeason(pageable, member.getSurvey())
                    .map(ProductListGetResponseDTO::new));
        }
        return new PageResponseDTO(curationRepository.findAllWithTarget(pageable, member.getSurvey(), target)
                .map(ProductListGetResponseDTO::new));
    }

    public PageResponseDTO CurationByGroupAndThemes(Pageable pageable, String group, List<String> conceptList) {

        conceptList.removeIf(String::isBlank);

        return new PageResponseDTO(curationRepository.findAllWithGroupAndThemes(pageable, group, conceptList)
                .map(ProductListGetResponseDTO::new));
    }
}
