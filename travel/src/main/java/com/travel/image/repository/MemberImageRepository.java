package com.travel.image.repository;

import com.travel.image.entity.MemberImage;
import com.travel.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    void deleteByMember(Member member);
}
