package com.travel.post.service;

import com.travel.post.dto.QnACreateDTO;

public interface QnAService {

    void createQnA(QnACreateDTO qnACreateDTO, String memberEmail);
}
