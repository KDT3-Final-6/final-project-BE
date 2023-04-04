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
            @RequestParam String season, @RequestParam String district, @RequestParam String theme) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        return ResponseEntity.ok(curationService.defaultCuration(pageRequest, season, district, theme));
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
            @RequestParam(required = false, defaultValue = "") String concept1,
            @RequestParam(required = false, defaultValue = "") String concept2,
            @RequestParam(required = false, defaultValue = "") String concept3) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }
        List<String> conceptList = new ArrayList<>();
        conceptList.add(concept1);
        conceptList.add(concept2);
        conceptList.add(concept3);
        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        return ResponseEntity.ok(curationService.CurationByGroupAndThemes(pageRequest, group, conceptList));
    }
}