package com.travel.nonmember.service.impl;

import com.travel.auth.dto.request.MemberRequestDto;
import com.travel.member.dto.requestDTO.FindMemberDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.member.util.RandomCode;
import com.travel.nonmember.service.MailService;
import com.travel.order.entity.Order;
import com.travel.product.entity.PurchasedProduct;
import com.travel.product.repository.PurchasedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final PasswordEncoder passwordEncoder;
    private final PurchasedProductRepository purchasedProductRepository;
    private final MemberRepository memberRepository;


    @Override
    public void sendEmailOrder(Order order) throws MessagingException {
        Member member = order.getMember();

        List<PurchasedProduct> purchasedProductList = purchasedProductRepository.findByOrder(order);

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        Context context = new Context();
        context.setVariable("name", member.getMemberName());
        context.setVariable("purchasedProductList", purchasedProductList);

        String html = templateEngine.process("ordermail", context);

        helper.setTo(member.getMemberEmail()); //받는 사람
        helper.setSubject("안녕하세요. 고투게더입니다."); //제목
        helper.setText(html, true); //내용
        helper.setFrom("wpdud2003@gmail.com"); //보내는 사람

        mailSender.send(message);
    }

    @Override
    public String checkEmail(MemberRequestDto.CheckEmail checkEmail) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        String check = RandomCode.generateCheckEmail();
        Context context = new Context();
        context.setVariable("check", check);
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        String html = templateEngine.process("checkmail", context);

        helper.setTo(checkEmail.getMemberEmail()); //받는 사람
        helper.setSubject("안녕하세요. 고투게더입니다."); //제목
        helper.setText(html, true); //내용
        helper.setFrom("wpdud2003@gmail.com"); //보내는 사람
        mailSender.send(message);

        return check;
    }

    @Override
    public String findPassword(FindMemberDTO.FindMemberPassword findPassword) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        Member member = memberRepository.findByMemberEmailAndMemberNameAndMemberPhone(
                findPassword.getMemberEmail(),
                findPassword.getMemberName(),
                findPassword.getMemberPhone()
        ).orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));

        String check = RandomCode.generateRandomPassword();
        Context context = new Context();
        context.setVariable("check", check);
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        String html = templateEngine.process("checkmail", context);

        helper.setTo(findPassword.getMemberEmail()); //받는 사람
        helper.setSubject("안녕하세요. 고투게더입니다."); //제목
        helper.setText(html, true); //내용
        helper.setFrom("wpdud2003@gmail.com"); //보내는 사람
        mailSender.send(message);

        String hashedPassword = passwordEncoder.encode(check);
        member.setMemberPassword(hashedPassword);
        memberRepository.save(member);

        return check;
    }
}
