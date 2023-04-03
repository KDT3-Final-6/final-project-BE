package com.travel.admin.dto.responseDTO;

import com.travel.image.entity.Image;
import com.travel.image.entity.MemberImage;
import com.travel.member.entity.Grade;
import com.travel.member.entity.Hobby;
import com.travel.member.entity.Member;
import com.travel.order.entity.Order;
import com.travel.survey.entity.Survey;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class MemberDetailInfoDTO {
    private Long memberId;
    private String memberEmail;
    private String memberName;
    private String memberNickname;
    private String memberPhone;
    private String memberBirthDate;
    private Grade memberGrade;
    private Boolean memberDeleteCheck;
    private List<Hobby> memberHobby;
    private String memberGender;
    private Boolean memberSmsAgree;
    private Boolean memberEmailAgree;
    private MemberImage memberImage;
    private List<String> roles;
    private List<Order> orders;
    private Survey survey;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public MemberDetailInfoDTO(Member member) {
        this.memberId = member.getMemberId();
        this.memberEmail = member.getMemberEmail();
        this.memberName = member.getMemberName();
        this.memberNickname = member.getMemberNickname();
        this.memberPhone = member.getMemberPhone();
        this.memberBirthDate = member.getMemberBirthDate();
        this.memberGrade = member.getMemberGrade();
        this.memberDeleteCheck = member.getMemberDeleteCheck();
        this.memberHobby = member.getMemberHobby();
        this.memberGender = member.getMemberGender();
        this.memberSmsAgree = member.getMemberSmsAgree();
        this.memberEmailAgree = member.getMemberEmailAgree();
        this.memberImage = member.getMemberImage();
        this.roles = member.getRoles();
        this.orders = member.getOrders();
        this.survey = member.getSurvey();
        this.createdDate = member.getCreatedDate();
        this.modifiedDate = member.getModifiedDate();
    }
}
