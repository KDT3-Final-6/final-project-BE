package com.travel.member.entity;

import com.travel.global.entity.BaseEntity;
import com.travel.image.entity.Image;
import com.travel.image.entity.MemberImage;
import com.travel.order.entity.Order;
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
public class Member extends BaseEntity implements UserDetails {
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

//    @Column(name = "member_authority", nullable = false)
//    private Boolean memberAuthority = false;

    @Column(name = "member_deleteCheck", nullable = false)
    private Boolean memberDeleteCheck = false;

    @Column(name = "member_hobby")
    @Enumerated(EnumType.STRING)
    private Hobby memberHobby;
    @Column(name = "member_gender")
    private String memberGender;
    @Column(name = "member_smsAgree", nullable = false)
    private Boolean memberSmsAgree = false;
    @Column(name = "member_emailAgree", nullable = false)
    private Boolean memberEmailAgree = false;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member")
    private MemberImage memberImage;

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

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

//    public Boolean isAdmin() {
//        if (this.memberAuthority) {
//            return true;
//        }
//        return false;
//    }

    // 회원가입 입력
    @Builder
    public Member(String memberEmail, String memberPassword, String memberName, String memberNickname, String memberPhone, String memberBirthDate, Hobby memberHobby, List<String> roles, String memberGender) {
        this.memberEmail = memberEmail;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberNickname = memberNickname;
        this.memberPhone = memberPhone;
        this.memberBirthDate = memberBirthDate;
        this.memberHobby = memberHobby;
        this.roles = roles;
        this.memberGender = memberGender;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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

    public void update(String memberPassword, String memberPhone, String memberNickname, Hobby memberHobby) {
        this.memberPassword = memberPassword;
        this.memberPhone = memberPhone;
        this.memberNickname = memberNickname;
        this.memberHobby = memberHobby;
    }

    public void delete() {
        this.memberDeleteCheck = true;
    }

}
