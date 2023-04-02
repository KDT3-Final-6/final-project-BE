package com.travel.admin.dto.responseDTO;

import com.travel.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberListDTO {
    private String memberId;
    private String memberName;
    private String memberNickname;
    private String memberEmail;
    private LocalDateTime createdDate;

    @Builder
    public MemberListDTO(Member member) {

        this.memberName = member.getMemberName();
        this.memberNickname = member.getMemberNickname();
        this.memberEmail = member.getMemberEmail();
        this.createdDate = member.getCreatedDate();
    }
}
