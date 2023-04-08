package com.travel.member.dto.requestDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Getter
@Service
@ToString
public class FindMemberDTO {

    @Getter
    @Setter
    @ToString
    public static class FindMemberEmail{
        private String memberEmail;
        private String memberName;
        private String memberPhone;
        private String memberBirthDate;

        public FindMemberEmail(String memberName, String memberPhone, String memberBirthDate) {
            this.memberName = memberName;
            this.memberPhone = memberPhone;
            this.memberBirthDate = memberBirthDate;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class FindMemberPassword{
        private String memberEmail;
        private String memberName;
        private String memberPhone;
        private String memberPassword;

        public FindMemberPassword(String memberEmail, String memberName, String memberPhone) {
            this.memberEmail = memberEmail;
            this.memberName = memberName;
            this.memberPhone = memberPhone;
        }
    }
}
