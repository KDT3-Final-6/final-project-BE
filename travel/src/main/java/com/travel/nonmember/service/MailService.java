package com.travel.nonmember.service;

import com.travel.order.entity.Order;

import javax.mail.MessagingException;

public interface MailService {

    void sendEmailOrder(Order order) throws MessagingException;
}
