package com.travel.post.repository.qnapost;

import com.travel.post.entity.QnAPost;

import java.util.List;

public interface QnACustomRepository {

    List<QnAPost> findQnAsByKeyword(String keyword);
}
