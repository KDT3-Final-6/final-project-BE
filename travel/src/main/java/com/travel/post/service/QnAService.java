package com.travel.post.service;

import com.travel.post.dto.request.QnARequsetDTO;

public interface QnAService {

    void createQnA(QnARequsetDTO qnARequsetDTO, String memberEmail);

    void deleteQnA(Long postId, String memberEmail);

}
