package com.travel.auth.controller;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.auth.dto.request.RefreshTokenRequestDTO;
import com.travel.auth.dto.response.LoginResponseDTO;
import com.travel.auth.dto.response.MemberResponseDto;
import com.travel.auth.jwt.JwtTokenProvider;
import com.travel.auth.lib.Helper;
import com.travel.auth.service.MemberService;
import com.travel.member.dto.responseDTO.MemberResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.travel.auth.dto.ResponseDto.empty;


@Slf4j
@RestController
public class    AuthController {
    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    public AuthController(MemberService memberService, JwtTokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/members")
    public ResponseDto<?> signUp(@Validated @RequestBody MemberRequestDto.SignUp signUp, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return new ResponseDto<>(Helper.refineErrors(errors));
        }
        return memberService.signUp(signUp);
    }

@PostMapping("/login")
public ResponseEntity<ResponseDto<MemberResponseDto.TokenInfo>> login(@Validated @RequestBody MemberRequestDto.Login login) {
    MemberResponseDto.TokenInfo tokenInfo = memberService.login(login);
    ResponseDto<MemberResponseDto.TokenInfo> responseDto = new ResponseDto<>(tokenInfo);
    return ResponseEntity.ok(responseDto);
}

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto<MemberResponseDto.TokenInfo>> reissue(
            @RequestBody MemberRequestDto.TokenRequest tokenRequest) {
        ResponseDto<MemberResponseDto.TokenInfo> responseDto = memberService.reissue(tokenRequest.getRefreshToken());
        return ResponseEntity.ok(responseDto);
    }
    @PostMapping("/members/logout")
    public ResponseEntity<ResponseDto<Boolean>> logout(@Validated @RequestHeader(name = "Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        MemberRequestDto.Logout logoutRequest = new MemberRequestDto.Logout();
        logoutRequest.setAccessToken(token);

        memberService.logout(logoutRequest);

        return new ResponseEntity<>(new ResponseDto<>(true), HttpStatus.OK);
    }


    @GetMapping("/authority")
    public ResponseEntity<?> authority() {
        log.info("ADD ROLE_ADMIN");
        return memberService.authority();
    }

    @GetMapping("/userTest")
    public ResponseDto<?> userTest() {
        log.info("ROLE_USER TEST");
        return new ResponseDto<>(empty());
    }

    @GetMapping("/adminTest")
    public ResponseDto<?> adminTest() {
        log.info("ROLE_ADMIN TEST");
        return new ResponseDto<>(empty());
    }
}
