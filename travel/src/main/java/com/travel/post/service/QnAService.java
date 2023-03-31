package com.travel.post.service;

import com.travel.global.response.PageResponseDTO;
import com.travel.post.dto.request.QnAAnswerRequestDTO;
import com.travel.post.dto.request.QnARequestDTO;
import org.springframework.data.domain.Pageable;

public interface QnAService {

    void createQnA(QnARequestDTO qnARequestDTO, String memberEmail);

    PageResponseDTO getQnAs(Pageable pageable, String memberEmail);

    void deleteQnA(Long postId, String memberEmail);

    PageResponseDTO getQnAsAdmin(Pageable pageable, String memberEmail);

    void createAnswer(QnAAnswerRequestDTO qnAAnswerRequestDTO, String memberEmail);

    void updateAnswer(QnAAnswerRequestDTO qnAAnswerRequestDTO);
}
