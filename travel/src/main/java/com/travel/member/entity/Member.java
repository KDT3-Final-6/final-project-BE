package com.travel.member.entity;

import com.travel.global.entity.BaseEntityWithModifiedDate;
import com.travel.image.entity.Image;
import com.travel.image.entity.MemberImage;
import com.travel.member.dto.requestDTO.MemberModifyRequestDTO;
import com.travel.order.entity.Order;
import com.travel.survey.entity.Survey;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Entity
@Setter
@Table(name = "member")
public class Member extends BaseEntityWithModifiedDate implements UserDetails {
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

    // localdate로 해보기
    @Column(name = "member_birth")
    private String memberBirthDate;

    @Column(name = "member_grade", nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade memberGrade = Grade.NORMAL;

    @Column(name = "member_deleteCheck", nullable = false)
    private Boolean memberDeleteCheck = false;

    @Column(name = "member_hobby")
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Hobby> memberHobby;

    @Column(name = "member_gender")
    private String memberGender;
    @Column(name = "member_smsAgree", nullable = false)
    private boolean memberSmsAgree;

    @Column(name = "member_emailAgree", nullable = false)
    private boolean memberEmailAgree;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member")
    private MemberImage memberImage;

    @Setter
    @Column(name = "is_non_members")
    private boolean isNonMembers = false;

    @Column
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "survey_id")
    private Survey survey;


    public Member() {
    }

    public Member(Optional<Member> member) {

    }

    public MemberImage addImage(Image image) {
        MemberImage convertedImage = image.toMemberImage();
        convertedImage.setMember(this);
        this.memberImage = convertedImage;

        return this.memberImage;
    }

    // 회원가입 입력
    @Builder
    public Member(String memberEmail, String memberPassword, String memberName, String memberNickname, String memberPhone, String memberBirthDate, List<Hobby> memberHobby, List<String> roles, String memberGender, boolean memberSmsAgree, boolean memberEmailAgree) {
        this.memberEmail = memberEmail;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberNickname = memberNickname;
        this.memberPhone = memberPhone;
        this.memberBirthDate = memberBirthDate;
        this.memberHobby = memberHobby;
        this.roles = roles;
        this.memberGender = memberGender;
        this.memberSmsAgree = memberSmsAgree;
        this.memberEmailAgree = memberEmailAgree;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void upgradeAuthority() {
        if (!this.roles.contains("ROLE_ADMIN")) {
            this.roles.add("ROLE_ADMIN");
        }
    }

    public void downgradeAuthority() {
        if (this.roles.contains("ROLE_ADMIN")) {
            this.roles.remove("ROLE_ADMIN");
        }
    }


    @Override
    public String getPassword() {
        return memberPassword;
    }

    @Override
    public String getUsername() {
        return memberEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public void delete() {
        this.memberDeleteCheck = true;
    }

    public void update(MemberModifyRequestDTO.ModifyMemberRequestDTO dto) {
        this.memberPassword = dto.getMemberRenewPassword();
        this.memberNickname = dto.getMemberNickname();
        this.memberPhone = dto.getMemberPhone();
        this.memberHobby = dto.getMemberHobby();
        this.memberSmsAgree = dto.isMemberSmsAgree();
        this.memberEmailAgree = dto.isMemberEmailAgree();
    }

    public void saveSurvey(Survey survey) {
        this.survey = survey;
    }
}
