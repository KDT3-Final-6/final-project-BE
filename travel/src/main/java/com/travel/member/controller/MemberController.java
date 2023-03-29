package com.travel.member.controller;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.auth.jwt.JwtTokenProvider;
import com.travel.member.dto.requestDTO.DeleteMemberDTO;
import com.travel.member.dto.requestDTO.MemberModifyRequestDTO;
import com.travel.member.dto.responseDTO.MemberResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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


        // 회원 아이디를 토대로 회원 정보 조회
        Member member = memberService.getMemberByMemberEmail(memberEmail);

        // 회원 정보가 없는 경우 404 응답 반환
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        // 회원 정보를 DTO 객체에 매핑
        MemberResponseDTO.MemberInfoResponseDTO memberInfo = new MemberResponseDTO.MemberInfoResponseDTO();
        memberInfo.setMemberEmail(member.getMemberEmail());
        memberInfo.setMemberName(member.getMemberName());
        memberInfo.setMemberNickName(member.getMemberNickname());
        memberInfo.setMemberPhone(member.getMemberPhone());
        memberInfo.setMemberBirthDate(member.getMemberBirthDate());
        memberInfo.setMemberHobby(member.getMemberHobby().toString());
        memberInfo.setMemberEmailAgree(member.getMemberEmailAgree());
        memberInfo.setMemberSmsAgree(member.getMemberSmsAgree());
//        memberInfo.setGrade(member.getMemberGrade());
        // 회원 정보를 담은 DTO 객체와 200 응답 반환
        return ResponseEntity.ok(memberInfo);
    }

    // 회원정보 수정
    @PatchMapping("/members")
    public ResponseEntity<ResponseDto<?>> modifyMember(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody MemberModifyRequestDTO.ModifyMemberRequestDTO modifyMemberRequestDTO) {

        String token = authorizationHeader.substring(7);

        // JWT 토큰에서 이메일 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String memberEmail = (claims.getSubject());

        // 회원 정보 수정 요청 처리
        MemberRequestDto.Login login = new MemberRequestDto.Login();
        login.setMemberEmail(memberEmail);
        ResponseDto<?> responseDto = memberService.modifyMember(login, modifyMemberRequestDTO);


        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/members")
    public ResponseEntity<ResponseDto<?>> deleteMember(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody DeleteMemberDTO deleteMemberDTO) {
        String token = authorizationHeader.substring(7);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String memberEmail = claims.getSubject();

        DeleteMemberDTO delete = new DeleteMemberDTO();
        delete.setMemberEmail(memberEmail);
        delete.setMemberPassword(deleteMemberDTO.getMemberPassword());

        boolean isDeleted = memberService.deleteMember(delete);

        if (isDeleted) {
            ResponseDto<?> responseDto = new ResponseDto<>("success");
            return ResponseEntity.ok(responseDto);
        } else {
            ResponseDto<?> responseDto = new ResponseDto<>( "회원 삭제에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

}

