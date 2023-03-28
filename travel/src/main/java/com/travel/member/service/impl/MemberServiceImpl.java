package com.travel.member.service.impl;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.member.dto.responseDTO.MemberResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.repository.MemberRepository;
import com.travel.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public ResponseDto<?> memberInfo(MemberRequestDto.Login login) {
        try {
            if (login != null) {
                Member member = memberRepository.findByMemberEmail(login.getMemberEmail())
                        .orElseThrow(IllegalArgumentException::new);
                return new ResponseDto<>(new MemberResponseDTO.MemberInfoResponseDTO(member));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseDto<>("로그인 정보가 없습니다.");
        }
    }

    public Member loadMemberByEmail(String memberEmail) {
        Optional<Member> member = memberRepository.findByMemberEmail(memberEmail);
        if (member == null) {
            throw new UsernameNotFoundException("No member found with email" + memberEmail);
        }
        return new Member(member);
    }

    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceAccessException(Long.toString(id)));
    }

    // optional을 안쓸꺼면 orElse 를 써서 예외처리를 해주거나
    @Override
    public Member getMemberByMemberEmail(String email) {
        return memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new ResourceAccessException((email)));
    }
}

