package com.travel.member.service;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.member.dto.requestDTO.DeleteMemberDTO;
import com.travel.member.dto.requestDTO.FindMemberDTO;
import com.travel.member.dto.requestDTO.MemberModifyRequestDTO;
import com.travel.member.dto.requestDTO.PasswordCheckDTO;
import com.travel.member.dto.responseDTO.MemberResponseDTO;
import com.travel.member.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {

    ResponseDto<?> memberInfo(MemberRequestDto.Login login);
    Member getMemberById(Long id);
    ResponseEntity<MemberResponseDTO.MemberInfoResponseDTO> getMemberByMemberEmail(String email);
    void updateProfile(String memberEmail, MultipartFile profile) throws IOException;
    void memberUpdate(String email,MemberModifyRequestDTO dto);
    void deleteMember(DeleteMemberDTO deleteMember);
    boolean passwordCheck(String email, PasswordCheckDTO passwordCheckDTO);
    String findMemberEmail(FindMemberDTO.FindMemberEmail findMemberEmail);
    String findMemberPassword(FindMemberDTO.FindMemberPassword findMemberPassword);
}
