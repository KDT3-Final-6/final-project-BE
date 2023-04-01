package com.travel.member.controller;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.auth.jwt.JwtTokenProvider;
import com.travel.member.dto.requestDTO.DeleteMemberDTO;
import com.travel.member.dto.requestDTO.MemberModifyRequestDTO;
import com.travel.member.dto.requestDTO.PasswordCheckDTO;
import com.travel.member.dto.responseDTO.MemberResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    // 회원정보
    @GetMapping("/members")
    public ResponseEntity<MemberResponseDTO.MemberInfoResponseDTO> getMyMemberInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // "Bearer " 이후의 토큰 값 추출
        String token = authorizationHeader.substring(7);

        // JWT 파싱하여 회원 아이디 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String memberEmail = (claims.getSubject());

        return memberService.getMemberByMemberEmail(memberEmail);

    }

    // 회원정보 수정
    @PatchMapping("/members")
    public ResponseEntity<?> modifyMember(@RequestHeader("Authorization") String authorizationHeader,@RequestBody MemberModifyRequestDTO.ModifyMemberRequestDTO modifyMemberRequestDTO) {

        String token = authorizationHeader.substring(7);

        // JWT 토큰에서 이메일 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String memberEmail = (claims.getSubject());


        // 회원 정보 수정 요청 처리
//        MemberRequestDto.Login login = new MemberRequestDto.Login();
//        login.setMemberEmail(memberEmail);
//        ResponseDto<?> responseDto = memberService.modifyMember(login, modifyMemberRequestDTO);


        return memberService.exampleOfUpdate(memberEmail,modifyMemberRequestDTO);
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody DeleteMemberDTO deleteMemberDTO) {
            String token = authorizationHeader.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(tokenProvider.getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String memberEmail = claims.getSubject();
            return memberService.deleteMember(memberEmail, deleteMemberDTO);
        }

    @PostMapping("/members/password-check")
    public Boolean passwordCheck(@RequestHeader("Authorization") String authorizationHeader,
                                           @RequestBody PasswordCheckDTO passwordCheckDTO) {
        String token = authorizationHeader.substring(7);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String memberEmail = claims.getSubject();
        return memberService.passwordCheck(memberEmail, passwordCheckDTO);

    }


    @PutMapping("/members/profile")
    public ResponseEntity<String> putProfile(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestPart("profile") MultipartFile profile) throws IOException {

        String token = authorizationHeader.substring(7);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String memberEmail = claims.getSubject();

        memberService.updateProfile(memberEmail, profile);
        return ResponseEntity.ok(null);
    }
}

