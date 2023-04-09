package com.travel.admin.service.impl;

import com.amazonaws.services.kms.model.NotFoundException;
import com.travel.admin.dto.responseDTO.MemberDetailInfoDTO;
import com.travel.admin.dto.responseDTO.MemberListDTO;
import com.travel.admin.service.AdminService;
import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.image.repository.MemberImageRepository;
import com.travel.member.entity.Member;
import com.travel.member.repository.MemberRepository;
import com.travel.post.repository.PostRepository;
import com.travel.post.repository.qnapost.QnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final QnARepository qnaRepository;
    private final PostRepository postRepository;

    // 관리자에서 회원 정보 조회
    @Override
    public PageResponseDTO getAllMembers(Pageable pageable) {
        List<Member> members = memberRepository.findAllByMemberDeleteCheckFalseOrderByMemberIdDesc();
        List<MemberListDTO> memberListDTOs = new ArrayList<>();

        for (Member member : members) {
            Long totalQnAs = postRepository.countQnaPostsByMemberId(member.getMemberId());
            Long totalReviews = postRepository.countReviewPostsByMemberId(member.getMemberId());
            Long total = totalQnAs + totalReviews;
            memberListDTOs.add(MemberListDTO.builder()
                    .member(member)
                    .total(total)
                    .totalQnAs(totalQnAs)
                    .totalReviews(totalReviews)
                    .build());
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), memberListDTOs.size());
        if (start > end) {
            throw new GlobalException(GlobalExceptionType.PAGE_IS_EXCEEDED);
        }

        return new PageResponseDTO(new PageImpl<>(memberListDTOs.subList(start, end), pageable, memberListDTOs.size()));
    }

    @Override
    public void changeMemberToAdmin(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));
        if (!member.getRoles().contains("ROLE_ADMIN")) { // 현재 권한 목록에 ROLE_ADMIN이 없을 경우에만 추가
            member.getRoles().add("ROLE_ADMIN");
            memberRepository.save(member);
        }
    }

    @Override
    public void changeAdminToMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));
        if (member.getRoles().contains("ROLE_ADMIN")) {
            member.getRoles().remove("ROLE_ADMIN");
            memberRepository.save(member);
        }
    }

    @Override
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));
        if (member.getMemberDeleteCheck() == false) {
            member.setMemberDeleteCheck(true);
            memberRepository.save(member);
        }
    }

    @Override
    public MemberDetailInfoDTO getMemberDetailInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find member with id: " + memberId));
        return MemberDetailInfoDTO.builder()
                .member(member)
                .build();
    }

    @Override
    public long countActiveMembers() {
        return memberRepository.countActiveMembers();
    }
    @Override
    public long countDeleteMembers() {
        return memberRepository.countDeleteMembers();
    }

    @Override
    public long countQnaMembers(Long memberId) {
        return postRepository.countQnaPostsByMemberId(memberId);
    }

    @Override
    public long countReviewMembers(Long memberId) {
        return postRepository.countReviewPostsByMemberId(memberId);
    }


}

