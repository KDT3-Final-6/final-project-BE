package com.travel.member.repository;

import com.travel.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberEmail(String email);

    boolean existsByMemberEmail(String memberEmail);
}
