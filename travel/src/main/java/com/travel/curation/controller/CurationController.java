package com.travel.curation.controller;

import com.travel.curation.service.CurationService;
import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/curation")
public class CurationController {
    public static final int PAGE_SIZE = 10;

    private final CurationService curationService;

    @GetMapping
    public ResponseEntity<PageResponseDTO> defaultCuration(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam String season, @RequestParam String district, @RequestParam String theme,
            Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        PageResponseDTO pageResponseDTO = null;

        if (authentication == null) {
            // 로그인되지 않은 사용자용 정보를 반환
            pageResponseDTO = curationService.defaultCuration(null, pageRequest, season, district, theme);
        } else {
            // 로그인된 사용자용 정보를 반환
            String memberEmail = authentication.getName();
            pageResponseDTO = curationService.defaultCuration(authentication.getName(), pageRequest, season, district, theme);
        }

        return ResponseEntity.ok(pageResponseDTO);
    }

    @GetMapping("/detail/companion")
    public ResponseEntity<PageResponseDTO> detailCurationByCompanion(
            @RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        return ResponseEntity.ok(curationService.detailCurationByTarget(authentication.getName(), pageRequest, "companion"));
    }

    @GetMapping("/detail/gender")
    public ResponseEntity<PageResponseDTO> detailCurationByGender(
            @RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        return ResponseEntity.ok(curationService.detailCurationByTarget(authentication.getName(), pageRequest, "gender"));
    }

    @GetMapping("/detail/age-group")
    public ResponseEntity<PageResponseDTO> detailCurationByAgeGroup(
            @RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        return ResponseEntity.ok(curationService.detailCurationByTarget(authentication.getName(), pageRequest, "ageGroup"));
    }

    @GetMapping("/detail/hobby")
    public ResponseEntity<PageResponseDTO> detailCurationByHobby(
            @RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        return ResponseEntity.ok(curationService.detailCurationByTarget(authentication.getName(), pageRequest, "hobby"));
    }

    @GetMapping("/detail/religion")
    public ResponseEntity<PageResponseDTO> detailCurationByReligion(
            @RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        return ResponseEntity.ok(curationService.detailCurationByTarget(authentication.getName(), pageRequest, "religion"));
    }

    @GetMapping("/detail/season")
    public ResponseEntity<PageResponseDTO> detailCurationBySeason(
            @RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        return ResponseEntity.ok(curationService.detailCurationByTarget(authentication.getName(), pageRequest, "season"));
    }

    @GetMapping("/group")
    public ResponseEntity<PageResponseDTO> CurationByGroupAndThemes(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam String group,
            @RequestParam(required = false, defaultValue = "") List<String> conceptList,
            Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        PageResponseDTO pageResponseDTO = null;

        if (authentication == null) {
            // 로그인되지 않은 사용자용 정보를 반환
            pageResponseDTO = curationService.CurationByGroupAndThemes(null, pageRequest, group, conceptList);
        } else {
            // 로그인된 사용자용 정보를 반환
            String memberEmail = authentication.getName();
            pageResponseDTO = curationService.CurationByGroupAndThemes(memberEmail, pageRequest, group, conceptList);
        }

        return ResponseEntity.ok(pageResponseDTO);
    }
}
