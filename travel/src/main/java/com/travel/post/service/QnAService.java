package com.travel.post.service;

import com.travel.global.response.PageResponseDTO;
import com.travel.post.dto.request.QnARequsetDTO;
import org.springframework.data.domain.Pageable;

public interface QnAService {

    void createQnA(QnARequsetDTO qnARequsetDTO, String memberEmail);

    PageResponseDTO getQnAs(Pageable pageable, String memberEmail);

    void deleteQnA(Long postId, String memberEmail);

    PageResponseDTO getQnAsAdmin(Pageable pageable, String memberEmail);

}
