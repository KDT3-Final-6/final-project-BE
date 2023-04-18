package com.travel.nonmember.service;

import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.member.dto.requestDTO.FindMemberDTO;
import com.travel.order.entity.Order;

import javax.mail.MessagingException;

public interface MailService {

    void sendEmailOrder(Order order) throws MessagingException;
    String checkEmail(MemberRequestDto.CheckEmail checkEmail) throws MessagingException;
    String findPassword(FindMemberDTO.FindMemberPassword findPassword) throws MessagingException;
}
