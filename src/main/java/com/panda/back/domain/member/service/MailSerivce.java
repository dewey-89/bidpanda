package com.panda.back.domain.member.service;

import com.panda.back.domain.member.dto.EmailRequestDto;
import com.panda.back.domain.member.dto.VerifiRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Log
public class MailSerivce {
    private final RedisUtil redisUtil;
    private final JavaMailSender javaMailSender;

    @Transactional
    public ResponseEntity sendEmail(EmailRequestDto request) {
        if (redisUtil.getData(request.getEmail()) != null){
            redisUtil.deleteData(request.getEmail());
        }
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);// 범위 : 111111 ~ 999999
        log.info("authKey : " + authKey);

        sendAuthEmail(request.getEmail(), authKey);
        log.info("email : " + request.getEmail());
        log.info("status : " + HttpStatus.OK);
    }

    private void sendAuthEmail(String email, String authKey) {

        String subject = "Bid Panda 회원가입 인증 메일입니다.";
        String text = "인증번호는 " + authKey + "입니다. <br/>";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true); //포함된 텍스트가 HTML이라는 의미로 true.
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

// 유효 시간(3분)동안 {email, authKey} 저장
        redisUtil.setDataExpire(email, authKey,  3 * 60 * 1L);

    }

    // 이메일 인증
    public ResponseEntity<String> verifyEmail(VerifiRequestDto request) {
        String email = request.getEmail();
        String authKey = request.getAuthKey();

        String redisAuthKey = redisUtil.getData(email);
        if (redisAuthKey == null) {
            throw new IllegalArgumentException("인증코드가 만료되었습니다.");
        }

        if (!redisAuthKey.equals(authKey)) {
            throw new IllegalArgumentException("인증코드가 맞지 않습니다.");
        }

        redisUtil.deleteData(email);
        return ResponseEntity.ok().body("인증이 완료되었습니다.");
    }
}
