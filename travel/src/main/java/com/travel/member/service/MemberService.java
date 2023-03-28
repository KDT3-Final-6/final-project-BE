package com.travel.member.service;

import com.travel.auth.dto.ResponseDto;
import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.member.dto.requestDTO.MemberModifyRequestDTO;
import com.travel.member.entity.Member;

public interface MemberService {

    ResponseDto<?> memberInfo(MemberRequestDto.Login login);
    Member getMemberById(Long id);
    Member getMemberByMemberEmail(String email);

}
