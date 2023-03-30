package com.travel.post.controller;

import com.travel.post.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnAService;
}
