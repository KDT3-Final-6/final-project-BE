package com.travel.auth.controller;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.auth.dto.response.MemberResponseDto;
import com.travel.auth.jwt.JwtTokenProvider;
import com.travel.auth.lib.Helper;
import com.travel.auth.service.MemberService;
import com.travel.nonmember.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import static com.travel.auth.dto.ResponseDto.empty;


@Slf4j
@RestController
public class    AuthController {
    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;
    private final MailService mailService;

    public AuthController(MemberService memberService, JwtTokenProvider tokenProvider, MailService mailService) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
        this.mailService = mailService;
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
    public ResponseEntity<ResponseDto<MemberResponseDto.LoginInfo>> login(@Validated @RequestBody MemberRequestDto.Login login) {
        MemberResponseDto.LoginInfo loginInfo = memberService.login(login);
        ResponseDto<MemberResponseDto.LoginInfo> responseDto = new ResponseDto<>(loginInfo);
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

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody MemberRequestDto.CheckEmail checkEmail) throws MessagingException {
        return ResponseEntity.ok(mailService.checkEmail(checkEmail));
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
