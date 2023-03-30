package com.travel.post.controller;

import com.travel.post.dto.QnACreateDTO;
import com.travel.post.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnAService;

    @PostMapping
    public ResponseEntity<Void> createQnA(@RequestBody QnACreateDTO qnACreateDTO, Authentication authentication) {
        qnAService.createQnA(qnACreateDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> createQnA(@PathVariable Long postId, Authentication authentication) {
        qnAService.deleteQnA(postId, authentication.getName());

        return ResponseEntity.ok(null);
    }
}
