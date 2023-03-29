package com.travel.image.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member_image")
public class MemberImage extends Image {

    @Setter
    @OneToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    public MemberImage(Image image) {
        super(image);
    }

    private MemberImage(String imageName, String imageOriginalName, String imagePath, String imageFormat, Long imageBytes) {
        super(imageName, imageOriginalName, imagePath, imageFormat, imageBytes);
    }

    public static MemberImage CreateDefaultMemberImage() {
        return new MemberImage("default-image.png", "default-image.png",
                "https://final-project-travel.s3.ap-northeast-2.amazonaws.com/default-image.png",
                "png", 18700L);
    }
}
