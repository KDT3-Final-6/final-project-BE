package com.travel.member.service.impl;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.member.dto.requestDTO.DeleteMemberDTO;
import com.travel.member.dto.requestDTO.MemberModifyRequestDTO;
import com.travel.member.dto.responseDTO.MemberResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.repository.MemberRepository;
import com.travel.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

//    @Override
//    public ResponseDto<?> modifyMember(MemberRequestDto.Login login, MemberModifyRequestDTO.ModifyMemberRequestDTO modifyMemberRequestDTO) {
//        try {
//            // 회원 정보를 찾습니다.
//            Member member = memberRepository.findByMemberEmail(login.getMemberEmail())
//                    .orElseThrow(IllegalArgumentException::new);
//
//            // 비밀번호 검증을 수행합니다.
//            passwordCheck(modifyMemberRequestDTO.getMemberPassword(), member.getMemberPassword());
//
//            // 회원 정보를 업데이트합니다.
//            member.setMemberPassword(encodingPassword(modifyMemberRequestDTO.getMemberRenewPassword()));
//            member.setMemberNickname(modifyMemberRequestDTO.getMemberNickname());
//            member.setMemberHobby(modifyMemberRequestDTO.getMemberHobby());
//            member.setMemberPhone(modifyMemberRequestDTO.getMemberPhone());
//            member.setMemberSmsAgree(modifyMemberRequestDTO.getMemberSmsAgree());
//            member.setMemberEmailAgree(modifyMemberRequestDTO.getMemberEmailAgree());
//
//            memberRepository.save(member);
//
//            return new ResponseDto<>("회원정보 수정 성공하였습니다.");
//        } catch (IllegalArgumentException e) {
//            return new ResponseDto<>("로그인 정보가 없습니다.");
//        }
//    }

    @Override
    public Boolean deleteMember(DeleteMemberDTO delete) {
        try {
            Member member = memberRepository.findByMemberEmail(delete.getMemberEmail())
                    .orElseThrow(() -> new IllegalArgumentException("해당 이메일 주소로 가입한 회원이 존재하지 않습니다."));

            // 비밀번호 일치 여부 확인
            if (!passwordEncoder.matches(delete.getMemberPassword(), member.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            member.setMemberDeleteCheck(true);
            memberRepository.save(member);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    private void passwordCheck(String checkMemberPassword, String memberPassword) {
        if (!passwordEncoder.matches(checkMemberPassword, memberPassword)) {
            throw new IllegalArgumentException();
        }
    }

    private String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }

}

