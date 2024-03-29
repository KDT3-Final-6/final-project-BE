package com.travel.member.dto.requestDTO;

import com.travel.global.entity.BaseEntityWithModifiedDate;
import com.travel.image.entity.MemberImage;
import com.travel.member.entity.Grade;
import com.travel.member.entity.Hobby;
import com.travel.member.entity.Member;
import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberModifyRequestDTO extends BaseEntityWithModifiedDate {

    // 회원 정보 수정
        private String memberEmail;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String memberPassword;
        private String memberName;
        @Size(min = 2, max = 10, message = "2글자 이상 10글자 이하로 입력해주세요")
        private String memberNickname;
        private List<Hobby> memberHobby;
        @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$",  message = "-없이 작성해주세요")
        private String memberPhone;
        private boolean memberSmsAgree;
        private boolean memberEmailAgree;
        private String memberGender;
        private Grade memberGrade;
        private MemberImage memberImage;

    }

