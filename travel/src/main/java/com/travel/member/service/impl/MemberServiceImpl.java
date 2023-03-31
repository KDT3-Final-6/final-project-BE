package com.travel.member.service.impl;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.image.entity.Image;
import com.travel.image.entity.MemberImage;
import com.travel.image.repository.MemberImageRepository;
import com.travel.image.service.FileUploadService;
import com.travel.member.dto.requestDTO.DeleteMemberDTO;
import com.travel.member.dto.requestDTO.MemberModifyRequestDTO;
import com.travel.member.dto.requestDTO.PasswordCheckDTO;
import com.travel.member.dto.responseDTO.MemberResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadService fileUploadService;

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
    public ResponseEntity<MemberResponseDTO.MemberInfoResponseDTO> getMemberByMemberEmail(String email) {
        try {
            Member member = memberRepository.findByMemberEmail(email).orElseThrow(NoSuchElementException::new);
            return new ResponseEntity<>(new MemberResponseDTO.MemberInfoResponseDTO(member),HttpStatus.OK);

        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public ResponseDto<?> modifyMember(MemberRequestDto.Login login, MemberModifyRequestDTO.ModifyMemberRequestDTO modifyMemberRequestDTO) {
        try {
            // 회원 정보를 찾습니다.
            Member member = memberRepository.findByMemberEmail(login.getMemberEmail())
                    .orElseThrow(IllegalArgumentException::new);

            // 비밀번호 검증을 수행합니다.
            passwordCheck(modifyMemberRequestDTO.getMemberPassword(), member.getMemberPassword());

            // 회원 정보를 업데이트합니다.
            member.setMemberPassword(encodingPassword(modifyMemberRequestDTO.getMemberRenewPassword()));
            member.setMemberPassword(modifyMemberRequestDTO.getMemberPassword());
            member.setMemberNickname(modifyMemberRequestDTO.getMemberNickname());
            member.setMemberHobby(modifyMemberRequestDTO.getMemberHobby());
            member.setMemberPhone(modifyMemberRequestDTO.getMemberPhone());
            member.setMemberSmsAgree(modifyMemberRequestDTO.getMemberSmsAgree());
            member.setMemberEmailAgree(modifyMemberRequestDTO.getMemberEmailAgree());
            member.setMemberImage(modifyMemberRequestDTO.getMemberImage());

            memberRepository.save(member);

            return new ResponseDto<>("회원정보 수정 성공하였습니다.");
        } catch (IllegalArgumentException e) {
            return new ResponseDto<>("로그인 정보가 없습니다.");
        }
    }

    @Transactional
    @Override
    public void updateProfile(String memberEmail, MultipartFile profile) throws IOException {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        memberImageRepository.deleteByMember(member);

        Image newProfile = fileUploadService.upload(profile);
        MemberImage memberImage = member.addImage(newProfile);

        memberImageRepository.save(memberImage);
    }

    @Override
    @Transactional
    public ResponseEntity<?> exampleOfUpdate(String email, MemberModifyRequestDTO.ModifyMemberRequestDTO dto) {
        try {
            Member member = memberRepository.findByMemberEmail(email).orElseThrow(NoSuchElementException::new);
            dto.setMemberRenewPassword(encodingPassword(dto.getMemberRenewPassword()));
            member.update(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> deleteMember(String email, DeleteMemberDTO deleteMemberDTO) {
        try {
            Member member = memberRepository.findByMemberEmail(email).orElseThrow(NoSuchElementException::new);
            deleteMemberDTO.setMemberPassword(encodingPassword(deleteMemberDTO.getMemberPassword()));
            member.setMemberDeleteCheck(true);
            memberRepository.save(member);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @Override
//    public Boolean passwordCheck(String email, PasswordCheckDTO passwordCheckDTO) {
//        try {
//            memberRepository.findByMemberEmail(email).orElseThrow(NoSuchElementException::new);
//            passwordCheckDTO.setMemberPassword(encodingPassword(passwordCheckDTO.getMemberPassword()));
//            return true;
//        } catch (NoSuchElementException e) {
//            return false;
//        }
//    }

    @Override
    public Boolean passwordCheck(String email, PasswordCheckDTO passwordCheckDTO) {
        Optional<Member> memberOptional = memberRepository.findByMemberEmail(email);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            if (passwordEncoder.matches(passwordCheckDTO.getMemberPassword(), member.getPassword())) {
                return true;
            }
        }
        return false;
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

