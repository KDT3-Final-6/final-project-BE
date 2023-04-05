package com.travel.auth.dto.request;


import com.travel.member.entity.Hobby;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class MemberRequestDto {

    @Getter
    @Setter
    public static class SignUp {
        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String memberEmail;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String memberPassword;

        @NotEmpty(message = "성함을 적어주세요")
        private String memberName;

        @NotEmpty(message = "아이디를 적어주세요")
        @Size(min = 2, max = 10, message = "2글자 이상 10글자 이하로 입력해주세요")
        private String memberNickname;

        @NotEmpty(message = "휴대폰 번호를 작성해주세요")
        @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "-없이 작성해주세요")
        private String memberPhone;

        @NotEmpty(message = "생일을 적어주세요")
        @Pattern(regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$", message = "YYYY-MM-DD 으로 작성해주세요")
        private String memberBirthDate;
        private List<Hobby> memberHobby;
        private String memberGender;
        private Boolean memberSmsAgree;
        private Boolean memberEmailAgree;

    }

    @Getter
    @Setter
    public static class Login {
        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String memberEmail;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String memberPassword;
        public UsernamePasswordAuthenticationToken toAuthentication() {
            System.out.println("memberPassword = " + memberPassword);
            return new UsernamePasswordAuthenticationToken(memberEmail, memberPassword);
        }
    }

    @Getter
    @Setter
    public static class Logout {
        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;
        public Logout() {
        }
    }

    @Getter
    @Setter
    public static class TokenRequest {
        private String accessToken;
        private String refreshToken;

    }

}
