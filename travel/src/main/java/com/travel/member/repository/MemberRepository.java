package com.travel.member.repository;

import com.travel.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberEmail(String email);
    List<Member> findAllByMemberDeleteCheckFalseOrderByMemberIdDesc();
    List<Member> findAll();
    boolean existsByMemberEmail(String memberEmail);
    @Query("SELECT COUNT(m) FROM Member m WHERE m.memberDeleteCheck = false")
    long countActiveMembers();
    @Query("SELECT COUNT(m) FROM Member m WHERE m.memberDeleteCheck = true")
    long countDeleteMembers();

    Optional<Member> findByMemberNameAndMemberPhoneAndMemberBirthDate(String memberName, String memberPhone, String memberBirthDate);

    Optional<Member> findByMemberEmailAndMemberNameAndMemberPhone(String memberEmail, String memberName, String memberPhone);


}
