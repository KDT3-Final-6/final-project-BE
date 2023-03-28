package com.travel.member.dto.responseDTO;

import com.travel.member.entity.Grade;
import com.travel.member.entity.Member;
import lombok.*;

public class MemberResponseDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    // 회원 정보 출력
    public static class MemberInfoResponseDTO {
        private String memberEmail;
        private String memberName;
        private String memberNickName;
        private String memberPhone;
        private String memberBirthDate;
        private String memberHobby;
        private Grade grade;

        public MemberInfoResponseDTO(Member member) {
        }

        public String getMemberEmail() {
            return memberEmail;
        }


        public String getMemberName() {
            return memberName;
        }

        public String getMemberNickName() {
            return memberNickName;
        }

        public String getMemberPhone() {
            return memberPhone;
        }

        public String getMemberBirthDate() {
            return memberBirthDate;
        }

        public String getMemberHobby() {
            return memberHobby;
        }
    }


}
