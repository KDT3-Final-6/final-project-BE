package com.travel.post.service.impl;

import com.travel.post.repository.QnARepository;
import com.travel.post.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final QnARepository qnARepository;
}
