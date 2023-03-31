package com.travel.auth.service;

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
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();
        MemberImage memberImage = member.addImage(defaultImage);
        memberRepository.save(member);
        memberImageRepository.save(memberImage);
        return new ResponseDto<>("회원가입 성공했습니다.");
    }

    @Transactional
    public ResponseDto<?> login(MemberRequestDto.Login login) {
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
        System.out.println("authenticationManagerBuilder = " + authenticationManagerBuilder);
        System.out.println(login.getMemberPassword());

        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return new ResponseDto<>(tokenInfo);
    }

    public ResponseDto<?> reissue(MemberRequestDto.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return new ResponseDto<>("Refresh Token 정보가 유효하지 않습니다.");
        }
        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("회원:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return new ResponseDto<>("잘못된 요청입니다.");
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return new ResponseDto<>("Refresh Token 정보가 일치하지 않습니다.");
        }

        // 4. 새로운 토큰 생성
        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

//        return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
        return new ResponseDto<>(tokenInfo);

    }
    @Transactional
    public ResponseDto<?> logout(MemberRequestDto.Logout logout) {
        // 로그아웃 하고 싶은 토큰이 유효한지 검증
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            throw new IllegalArgumentException("로그아웃: 유효하지 않은 토큰입니다.");
        }
        // access token에서 mail 가져온다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // redis에서 해당 mail 로 저장된 refresh token이 있는지 여부를 확인 후에 있을 경우 삭제
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            redisTemplate.delete("RT:" + authentication.getName());
        }

        Long expireToken = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue().set(logout.getAccessToken(), "logout", expireToken, TimeUnit.MILLISECONDS);

        return new ResponseDto<>(HttpStatus.OK);
    }

    public ResponseDto<?> authority() {
        // SecurityContext에 담겨 있는 authentication userEamil 정보
        String memberEmail = SecurityUtil.getCurrentUserEmail();

        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));

        // add ROLE_ADMIN
        member.getRoles().add(Authority.ROLE_ADMIN.name());
        memberRepository.save(member);

        return new ResponseDto<>(ResponseDto.empty());
    }

    public boolean checkToken(String token) {
        return redisTemplate.hasKey("token");
    }
}
