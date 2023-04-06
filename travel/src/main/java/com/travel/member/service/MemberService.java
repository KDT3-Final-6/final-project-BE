package com.travel.member.service;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.member.dto.requestDTO.DeleteMemberDTO;
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
    ResponseDto<?> modifyMember(MemberRequestDto.Login login, MemberModifyRequestDTO.ModifyMemberRequestDTO modifyMemberInfoRequestDTO);
    void updateProfile(String memberEmail, MultipartFile profile) throws IOException;
    ResponseEntity<?> exampleOfUpdate(String email,MemberModifyRequestDTO.ModifyMemberRequestDTO dto);
    void deleteMember(DeleteMemberDTO deleteMember);
    Boolean passwordCheck(String email, PasswordCheckDTO passwordCheckDTO);

}
