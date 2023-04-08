package com.travel.member.service.impl;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.auth.jwt.JwtTokenProvider;
import com.travel.image.entity.Image;
import com.travel.image.entity.MemberImage;
import com.travel.image.repository.MemberImageRepository;
import com.travel.image.service.FileUploadService;
import com.travel.member.dto.requestDTO.DeleteMemberDTO;
import com.travel.member.dto.requestDTO.FindMemberDTO;
import com.travel.member.dto.requestDTO.MemberModifyRequestDTO;
import com.travel.member.dto.requestDTO.PasswordCheckDTO;
import com.travel.member.dto.responseDTO.MemberResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.travel.member.util.PasswordUtils.generateRandomPassword;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadService fileUploadService;
    private final JwtTokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;

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

    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceAccessException(Long.toString(id)));
    }

    // 회원 조회
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
    public void memberUpdate(String email, MemberModifyRequestDTO.ModifyMemberRequestDTO dto) {
        Optional<Member> memberOptional = memberRepository.findByMemberEmail(email);
        if (memberOptional.isPresent()) {
            Member member = memberRepository.findByMemberEmail(email).orElseThrow(NoSuchElementException::new);
            if (dto.getMemberPassword() != null) {
                member.setMemberPassword(passwordEncoder.encode(dto.getMemberPassword()));
            }
            member.setMemberNickname(dto.getMemberNickname());
            member.setMemberHobby(dto.getMemberHobby());
            member.setMemberPhone(dto.getMemberPhone());
            member.setMemberSmsAgree(dto.isMemberSmsAgree());
            member.setMemberEmailAgree(dto.isMemberEmailAgree());
            memberRepository.save(member);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void deleteMember(DeleteMemberDTO deleteMemberDTO) {
        String email = tokenProvider.getEmailFromToken(deleteMemberDTO.getAccessToken());
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 회원이 존재하지 않습니다."));

        member.setMemberDeleteCheck(true);
        memberRepository.save(member);

        long expireTime = tokenProvider.getExpiration(deleteMemberDTO.getAccessToken());
        redisTemplate.opsForValue().set(deleteMemberDTO.getAccessToken(), "logout", expireTime, TimeUnit.MILLISECONDS);
        redisTemplate.delete("RT:" + email);
    }

    @Override
    public boolean passwordCheck(String email, PasswordCheckDTO passwordCheckDTO) {
        Optional<Member> memberOptional = memberRepository.findByMemberEmail(email);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String hashedPassword = member.getPassword();
            String plainPassword = passwordCheckDTO.getMemberPassword();
            return passwordEncoder.matches(plainPassword, hashedPassword);
        }
        return false;
    }

    @Override
    public String findMemberEmail(FindMemberDTO.FindMemberEmail findMemberEmail) {
        Member member = memberRepository.findByMemberNameAndMemberPhoneAndMemberBirthDate(
                findMemberEmail.getMemberName(),
                findMemberEmail.getMemberPhone(),
                findMemberEmail.getMemberBirthDate()
        ).orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        String email = member.getMemberEmail();

        return email;
    }

    @Override
    public String findMemberPassword(FindMemberDTO.FindMemberPassword findMemberPassword) {

        Member member = memberRepository.findByMemberEmailAndMemberNameAndMemberPhone(
                findMemberPassword.getMemberEmail(),
                findMemberPassword.getMemberName(),
                findMemberPassword.getMemberPhone()
        ).orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        String newPassword = generateRandomPassword();
        String hashedPassword = passwordEncoder.encode(newPassword);

        member.setMemberPassword(hashedPassword);
        memberRepository.save(member);
        return newPassword;
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

