package com.travel.admin.service.impl;

import com.travel.admin.dto.responseDTO.MemberListDTO;
import com.travel.admin.service.AdminService;
import com.travel.global.response.PageResponseDTO;
import com.travel.image.repository.MemberImageRepository;
import com.travel.member.entity.Member;
import com.travel.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final PasswordEncoder passwordEncoder;


    // 관리자에서 회원 정보 조회
    @Override
    @Transactional
    public ResponseEntity<MemberListDTO> getMemberByMemberEmail(String email) {
        try {
            Member member = memberRepository.findByMemberEmail(email).orElseThrow(NoSuchElementException::new);
            return new ResponseEntity<>(new MemberListDTO(member), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public PageResponseDTO getAllMembers(Pageable pageable) {
        List<Member> members = memberRepository.findAll();
        List<MemberListDTO> memberListDTOs = new ArrayList<>();

        for (Member member : members) {
            memberListDTOs.add(MemberListDTO.builder()
                    .member(member)
                    .build());
        }

        return new PageResponseDTO(new PageImpl<>(memberListDTOs, pageable, memberListDTOs.size()));
    }
}

