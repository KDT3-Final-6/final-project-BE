package com.travel.admin.service;

import com.travel.admin.dto.responseDTO.MemberListDTO;
import com.travel.global.response.PageResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<MemberListDTO> getMemberByMemberEmail(String email);
    PageResponseDTO getAllMembers(Pageable pageable);
    void changeMemberToAdmin(Long memberId);
    void changeAdminToMember(Long adminId);

}
