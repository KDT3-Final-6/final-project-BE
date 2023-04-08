package com.travel.member.controller;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.auth.jwt.JwtTokenProvider;
import com.travel.member.dto.requestDTO.DeleteMemberDTO;
import com.travel.member.dto.requestDTO.FindMemberDTO;
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
import java.util.NoSuchElementException;


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
    public ResponseEntity<?> memberUpdate(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MemberModifyRequestDTO memberModifyRequestDTO) {

        String token = authorizationHeader.substring(7);

        // JWT 토큰에서 이메일 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String memberEmail = (claims.getSubject());
        try {
            memberService.memberUpdate(memberEmail, memberModifyRequestDTO);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        DeleteMemberDTO deleteMemberDTO = new DeleteMemberDTO();
        deleteMemberDTO.setAccessToken(token);

        memberService.deleteMember(deleteMemberDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/members/password-check")
    public ResponseEntity<?> passwordCheck(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PasswordCheckDTO passwordCheckDTO) {
        String token = authorizationHeader.substring(7);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String email = claims.getSubject(); // 토큰에서 회원 이메일 정보 추출
        boolean isMatched = memberService.passwordCheck(email, passwordCheckDTO);
        if (isMatched) {
            return ResponseEntity.ok("비밀번호가 일치합니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }
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

    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(@RequestBody FindMemberDTO.FindMemberPassword findPassword) {
        String newPassword = memberService.findMemberPassword(findPassword);
        return ResponseEntity.ok(newPassword);
    }

    @PostMapping("/find-email")
    public ResponseEntity<String> findEmail(@RequestBody FindMemberDTO.FindMemberEmail findEmail) {
        String email = memberService.findMemberEmail(findEmail);
        return ResponseEntity.ok(email);
    }
}

