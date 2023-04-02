package com.travel.post.controller;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.post.dto.request.QnARequestDTO;
import com.travel.post.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnAController {

    public static final int PAGE_SIZE = 10;
    private final QnAService qnAService;

    @PostMapping
    public ResponseEntity<Void> createQnA(@Valid @RequestBody QnARequestDTO qnARequestDTO, Authentication authentication) {
        qnAService.createQnA(qnARequestDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getQnAs(@RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        PageResponseDTO pageResponseDTO = qnAService.getQnAs(pageRequest, authentication.getName());

        return ResponseEntity.ok(pageResponseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteQnA(@PathVariable Long postId, Authentication authentication) {
        qnAService.deleteQnA(postId, authentication.getName());

        return ResponseEntity.ok(null);
    }
}
