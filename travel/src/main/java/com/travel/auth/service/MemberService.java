package com.travel.auth.service;

import com.travel.auth.TokenType;
import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.auth.dto.response.MemberResponseDto;
import com.travel.auth.enums.Authority;
import com.travel.auth.exception.AuthException;
import com.travel.auth.exception.AuthExceptionType;
import com.travel.auth.jwt.JwtTokenProvider;
import com.travel.image.entity.MemberImage;
import com.travel.image.repository.MemberImageRepository;
import com.travel.member.repository.MemberRepository;
import com.travel.global.config.SecurityUtil;
import com.travel.member.entity.Hobby;
import com.travel.member.entity.Member;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;

    @Transactional
    public ResponseDto<?> signUp(MemberRequestDto.SignUp signUp) {
        if (memberRepository.existsByMemberEmail(signUp.getMemberEmail())) {
            return new ResponseDto<>("이미 회원가입된 이메일입니다.");
        }

        MemberImage defaultImage = MemberImage.CreateDefaultMemberImage();


        Member member = Member.builder()
                .memberEmail(signUp.getMemberEmail())
                .memberPassword(passwordEncoder.encode(signUp.getMemberPassword()))
                .memberName(signUp.getMemberName())
                .memberNickname(signUp.getMemberNickname())
                .memberPhone(signUp.getMemberPhone())
                .memberBirthDate(signUp.getMemberBirthDate())
                .memberHobby(signUp.getMemberHobby())
                .memberGender(signUp.getMemberGender())
                .memberEmailAgree(signUp.isMemberEmailAgree())
                .memberSmsAgree(signUp.isMemberSmsAgree())
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();

        MemberImage memberImage = member.addImage(defaultImage);
        memberRepository.save(member);
        memberImageRepository.save(memberImage);
        return new ResponseDto<>("회원가입 성공했습니다.");
    }

    @Transactional
    public MemberResponseDto.TokenInfo login(MemberRequestDto.Login login) {
        Member member = memberRepository.findByMemberEmail(login.getMemberEmail())
                .orElseThrow(() -> new AuthException(AuthExceptionType.INVALID_EMAIL_OR_PASSWORD));
        // 회원 삭제 여부
        if (member.getMemberDeleteCheck()) {
            throw new AuthException(AuthExceptionType.ACCOUNT_DISABLED);
        }
        // 패스워드 불일치 시
        if (!passwordEncoder.matches(login.getMemberPassword(), member.getPassword())) {
            throw new AuthException(AuthExceptionType.INVALID_EMAIL_OR_PASSWORD);
        }
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        redisTemplate.opsForValue().set("AT:" + authentication.getName(), tokenInfo.getAccessToken());
        redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        System.out.println(tokenInfo.getAccessToken());
        System.out.println(tokenInfo.getRefreshToken());
        return tokenInfo;
    }

    @Transactional
    public ResponseDto<MemberResponseDto.TokenInfo> reissue(String refreshToken) {
        // 1. 클라이언트로부터 refresh token을 전달

        // 2. 전달받은 refresh token이 유효한지 확인
        Claims claims = jwtTokenProvider.getClaimsFromToken(refreshToken, TokenType.REFRESH);
        String email = claims.getSubject();
//
        String storedRefreshToken = (String) redisTemplate.opsForValue().get("RT:" + email);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new AuthException(AuthExceptionType.INVALID_TOKEN);
        }
        // 3. refresh token이 유효하다면, 해당 refresh token에 대응되는 access token을 redis에서 삭제
        String storedAccessToken = (String) redisTemplate.opsForValue().get("AT:" + email);
        if (storedAccessToken != null) {
            redisTemplate.delete("AT:" + email);
        }
        // 4. 새로운 access token과 refresh token을 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null);
        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        // 5. 새로 생성된 refresh token을 redis에 저장합니다.
        redisTemplate.opsForValue()
                .set("RT:" + email, tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        // 6. 생성된 access token과 refresh token을 반환
        return new ResponseDto<>(tokenInfo);
    }
//    @Transactional
//    public ResponseDto<?> logout(MemberRequestDto.Logout logout) {
//        // 로그아웃 하고 싶은 토큰이 유효한지 검증
//        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
//            throw new IllegalArgumentException("로그아웃: 유효하지 않은 토큰입니다.");
//        }
//        // access token에서 mail 가져온다.
//        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());
//
//        // redis에서 해당 mail 로 저장된 refresh token이 있는지 여부를 확인 후에 있을 경우 삭제
//        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
//            redisTemplate.delete("RT:" + authentication.getName());
//        }
//
//        Long expireToken = jwtTokenProvider.getExpiration(logout.getAccessToken());
//        redisTemplate.opsForValue().set(logout.getAccessToken(), "logout", expireToken, TimeUnit.MILLISECONDS);
//
//        return new ResponseDto<>(HttpStatus.OK);
//    }
//    @Transactional
//    public ResponseDto<Boolean> logout(MemberRequestDto.Logout logout) {
//        // 로그아웃 하고 싶은 토큰이 유효한지 검증
//        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
//            throw new IllegalArgumentException("로그아웃: 유효하지 않은 토큰입니다.");
//        }
//        // access token에서 mail 가져온다.
//        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());
//
//        // Redis에서 해당 mail로 저장된 refresh token이 있는지 여부를 확인 후에 있을 경우 삭제
//        String refreshTokenKey = "RT:" + authentication.getName();
//        if (redisTemplate.hasKey(refreshTokenKey)) {
//            redisTemplate.delete(refreshTokenKey);
//        }
//
//        // Redis에 저장된 access token의 만료시간을 조정하여 유효하지 않은 토큰으로 만듦
//        Long remainingExpireTime = jwtTokenProvider.getRemainingExpireTime(logout.getAccessToken());
//        redisTemplate.expire(logout.getAccessToken(), remainingExpireTime, TimeUnit.MILLISECONDS);
//
//        return new ResponseDto<>(true);
//    }

    @Transactional
    public void logout(MemberRequestDto.Logout logout) {
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            throw new IllegalArgumentException("로그아웃: 유효하지 않은 토큰입니다.");
        }
        // Access Token에서 User email을 가져온다
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());
        // Redis에서 해당 User email로 저장된 Refresh Token 이 있는지 여부를 확인 후에 있을 경우 삭제를 한다.
        if (redisTemplate.opsForValue().get("RT:"+authentication.getName())!=null){
            // Refresh Token을 삭제
            redisTemplate.delete("RT:"+authentication.getName());
        }
        // 해당 Access Token 유효시간을 가지고 와서 BlackList에 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue().set(logout.getAccessToken(),"logout",expiration,TimeUnit.MILLISECONDS);
    }

    public ResponseEntity<?> authority() {
        String memberEmail = SecurityUtil.getCurrentUserEmail();

        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));

        // add ROLE_ADMIN
        member.getRoles().add(Authority.ROLE_ADMIN.name());
        memberRepository.save(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public boolean checkToken(String token) {
        return redisTemplate.hasKey("token");
    }

    public String getRefreshTokenFromCache(String userEmail) {
        return (String) redisTemplate.opsForValue().get("RT:" + userEmail);
    }

    public void updateRefreshTokenCache(String userEmail, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set("RT:" + userEmail, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }
}
