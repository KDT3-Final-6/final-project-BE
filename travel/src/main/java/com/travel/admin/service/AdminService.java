package com.travel.admin.service;

import com.travel.admin.dto.responseDTO.MemberDetailInfoDTO;
import com.travel.admin.dto.responseDTO.MemberListDTO;
import com.travel.global.response.PageResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    PageResponseDTO getAllMembers(Pageable pageable);
    void changeMemberToAdmin(Long memberId);
    void changeAdminToMember(Long memberId);
    void deleteMember(Long memberId);
    MemberDetailInfoDTO getMemberDetailInfo(Long memberId);    }
