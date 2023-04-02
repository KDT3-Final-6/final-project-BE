package com.travel.survey.service;

import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.survey.dto.SurveyDTO;
import com.travel.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveSurvey(SurveyDTO.PostSurvey postSurvey, String memberEmail) {

        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        member.saveSurvey(postSurvey.toEntity());

    }
}
