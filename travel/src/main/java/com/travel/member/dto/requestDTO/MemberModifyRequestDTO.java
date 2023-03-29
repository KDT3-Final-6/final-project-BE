package com.travel.member.dto.requestDTO;

import com.travel.member.entity.Grade;
import com.travel.member.entity.Hobby;
import com.travel.member.entity.Member;
import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberModifyRequestDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    // 회원 정보 수정
    public static class ModifyMemberRequestDTO {
        private String memberEmail;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String memberPassword;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String memberRenewPassword;
        private String memberName;
        @Size(min = 2, max = 10, message = "2글자 이상 10글자 이하로 입력해주세요")
        private String memberNickname;
        private Hobby memberHobby;
        @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "-없이 작성해주세요")
        private String memberPhone;
        private Boolean memberSmsAgree;
        private Boolean memberEmailAgree;
        private String memberGender;
        private Grade memberGrade;
        public static ModifyMemberRequestDTO from(Member member) {
            ModifyMemberRequestDTO modifyMemberRequestDTO = new ModifyMemberRequestDTO();
            modifyMemberRequestDTO.setMemberPassword(member.getMemberPassword());
            modifyMemberRequestDTO.setMemberRenewPassword(member.getMemberPassword());
            modifyMemberRequestDTO.setMemberNickname(member.getMemberNickname());
            modifyMemberRequestDTO.setMemberHobby(member.getMemberHobby());
            modifyMemberRequestDTO.setMemberPhone(member.getMemberPhone());
            return modifyMemberRequestDTO;
        }
    }
}
