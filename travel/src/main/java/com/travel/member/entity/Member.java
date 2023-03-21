package com.travel.member.entity;

import com.travel.global.entity.BaseEntity;
import com.travel.order.entity.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "member_pw")
    private String memberPassword;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_nickname")
    private String memberNickname;

    @Column(name = "member_phone")
    private String memberPhone;

    @Column(name = "member_birth")
    private LocalDate memberBirthDate;

    @Column(name = "member_grade", nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade memberGrade = Grade.NORMAL;

    @Column(name = "member_authority", nullable = false)
    private Boolean memberAuthority = false;

    @Column(name = "member_deleteCheck", nullable = false)
    private Boolean memberDeleteCheck = false;

    @Column(name = "member_hobby")
    @Enumerated(EnumType.STRING)
    private Hobby memberHobby;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Builder
    public Member(String memberEmail, String memberPassword, String memberName, String memberNickname, String memberPhone, LocalDate memberBirthDate, Hobby memberHobby) {
        this.memberEmail = memberEmail;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberNickname = memberNickname;
        this.memberPhone = memberPhone;
        this.memberBirthDate = memberBirthDate;
        this.memberHobby = memberHobby;
    }

    public Boolean isAdmin() {
        if (this.memberAuthority) {
            return true;
        }
        return false;
    }
}
