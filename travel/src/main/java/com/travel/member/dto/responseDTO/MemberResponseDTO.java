package com.travel.member.dto.responseDTO;

import com.travel.member.entity.Grade;
import com.travel.member.entity.Hobby;
import com.travel.member.entity.Member;
import lombok.*;

import java.util.List;

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
        private List<Hobby> memberHobby;
        private boolean memberSmsAgree;
        private boolean memberEmailAgree;
        private Grade memberGrade;
        private String memberImage;
        private List<String> roles;
        private String memberGender;

        public MemberInfoResponseDTO(Member member) {
            this.memberEmail = member.getMemberEmail();
            this.memberName = member.getMemberName();
            this.memberNickName = member.getMemberNickname();
            this.memberPhone = member.getMemberPhone();
            this.memberBirthDate = member.getMemberBirthDate();
            this.memberHobby = member.getMemberHobby();
            this.memberSmsAgree = member.isMemberSmsAgree();
            this.memberEmailAgree = member.isMemberEmailAgree();
            this.memberGrade = member.getMemberGrade();
            this.memberImage = member.getMemberImage().getImagePath();
            this.roles = member.getRoles();
            this.memberGender = member.getMemberGender();
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

        public List<Hobby> getMemberHobby() {
            return memberHobby;
        }
        public Boolean getMemberSmsAgree() {
            return memberSmsAgree;
        }

        public Boolean getMemberEmailAgree() {
            return memberEmailAgree;
        }

        public Grade getMemberGrade() {
            return memberGrade;
        }
    }


}
