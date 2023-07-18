package com.example.moyiza_be.domain.user.email;

import com.example.moyiza_be.common.redis.RedisUtil;
import com.example.moyiza_be.domain.user.util.ValidationUtil;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Message.RecipientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final ValidationUtil validationUtil;

    private MimeMessage createMessage(String receiverEmail, String verificationCode)throws Exception{
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, receiverEmail);
        message.setSubject("[모이자] 회원가입 이메일 인증번호");

        String msgg="";
        msgg+= "<div style=\"padding: 26px 18px;\">";
        msgg+= "<img src=\"https://moyiza-image.s3.ap-northeast-2.amazonaws.com/49d8aab8-01f1-4ffe-8d7c-3f5a958d8fd8_image%203.jpg\" style=\"width: 105px; height: 31px;\" loading=\"lazy\">";
        msgg+= "<h1 style=\"margin-top: 23px; margin-bottom: 9px; color: #222222; font-size: 19px; line-height: 25px; letter-spacing: -0.27px;\">이메일 인증</h1>";
        msgg+= "<div style=\"margin-top: 7px; margin-bottom: 22px; color: #222222;\">";
        msgg+= "<p style=\"margin-block-start: 0; margin-block-end: 0; margin-inline-start: 0; margin-inline-end: 0; line-height: 1.47; letter-spacing: -0.22px; font-size: 15px; margin: 8px 0 0;\">안녕하세요, 모이자 입니다.</p>";
        msgg+= "<p style=\"margin-block-start: 0; margin-block-end: 0; margin-inline-start: 0; margin-inline-end: 0; line-height: 1.47; letter-spacing: -0.22px; font-size: 15px; margin: 8px 0 0;\">아래 확인 코드를 회원가입 화면에 입력해주세요.</p>";
        msgg+= "<p style=\"margin-block-start: 0; margin-block-end: 0; margin-inline-start: 0; margin-inline-end: 0; line-height: 1.47; letter-spacing: -0.22px; font-size: 15px; margin: 8px 0 0;\">";
        msgg+= "<h1>" + verificationCode + "</h1></p>";
        msgg+= "<p style=\"margin-block-start: 0; margin-block-end: 0; margin-inline-start: 0; margin-inline-end: 0; line-height: 1.47; letter-spacing: -0.22px; font-size: 15px; margin: 20px 0;\">";
        msgg+= "감사합니다.<br>";
        msgg+= "모이자 팀 드림</p>";
        msgg+= "<hr style=\"display: block; height: 1px; background-color: #ebebeb; margin: 14px 0; padding: 0; border-width: 0;\"><div><div>";
        msgg+= "<p style=\"margin-block: 0; margin-inline: 0; font-weight: normal; font-size: 14px; font-stretch: normal; font-style: normal; line-height: 1.43; letter-spacing: normal; color: #8a8a8a; margin: 5px 0 0;\">본 메일은 발신전용 메일로 회신되지 않습니다. 본 메일과 관련되어 궁금하신 점이나 불편한 사항은 고객센터에 문의해 주시기 바랍니다.</p></div>";
        msgg+= "<div><p style=\"margin-block: 0; margin-inline: 0; font-weight: normal; font-size: 14px; font-stretch: normal; font-style: normal; line-height: 1.43; letter-spacing: normal; color: #8a8a8a; margin: 5px 0 0;\">주식회사 모이자 | 허리도 가늘군 만지면 부서지리 343, 3층 | cs@mo2za.com<br>";
        msgg+= "전화번호: 02-777-9999 | 통신판매업 신고번호: 제 2023-모이자-0965호<br>";
        msgg+= "Copyright © 2023 by <b>Moyiza, Inc.</b> All rights reserved.</p></div></div></div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("cs@mo2za.com","Moyiza"));

        return message;
    }

    public ResponseEntity<?> sendSimpleMessage(EmailRequestDto requestDto)throws Exception {
        String receiverEmail = requestDto.getEmail();
        validationUtil.checkDuplicatedEmail(receiverEmail);
        String verificationCode = validationUtil.createCode();
        MimeMessage message = createMessage(receiverEmail, verificationCode);
        try{
            redisUtil.setDataExpire(verificationCode, receiverEmail, 60 * 5L); //Valid for 5 minutes
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return new ResponseEntity<>("The email was sent successfully.", HttpStatus.OK);
    }

    public ResponseEntity<?> verifyCode(String code)throws Exception {
        if (redisUtil.getData(code) == null){
            throw new IllegalArgumentException("Invalid credentials.");
        }
        redisUtil.deleteData(code);
        return new ResponseEntity<>("Email verification successful!", HttpStatus.OK);
    }

}