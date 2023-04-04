package com.travel.admin.dto.responseDTO;

import com.travel.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberListDTO {
    private Long memberId;
    private String memberName;
    private String memberNickname;
    private String memberEmail;
    private LocalDateTime createdDate;
    private Long totalQnAs;
    private Long totalReviews;
    private Long total;

    @Builder
    public MemberListDTO(Member member, Long totalQnAs, Long totalReviews, Long total) {
        this.memberId = member.getMemberId();
        this.memberName = member.getMemberName();
        this.memberNickname = member.getMemberNickname();
        this.memberEmail = member.getMemberEmail();
        this.createdDate = member.getCreatedDate();
        this.totalQnAs = totalQnAs;
        this.totalReviews = totalReviews;
        this.total = total;
    }
}
