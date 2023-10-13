package com.panda.back.domain.member.service;

import com.panda.back.domain.member.dto.EmailRequestDto;
import com.panda.back.domain.member.dto.VerifiRequestDto;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
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
    public BaseResponse<String> sendEmail(EmailRequestDto request) {
        if (redisUtil.getData(request.getEmail()) != null){
            redisUtil.deleteData(request.getEmail());
        }
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);// 범위 : 111111 ~ 999999
        log.info("authKey : " + authKey);

        sendAuthEmail(request.getEmail(), authKey);
        log.info("email : " + request.getEmail());
        log.info("status : " + HttpStatus.OK);

        return BaseResponse.successMessage("인증코드 전송 완료");
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
    public BaseResponse<String> verifyEmail(VerifiRequestDto request) {
        String email = request.getEmail();
        String authKey = request.getAuthKey();

        if(email == null || authKey == null){
            throw new CustomException(ErrorCode.REQUIRED_EMAIL_AND_AUTHKEY);
        }

        String redisAuthKey = redisUtil.getData(email);
        if (redisAuthKey == null) {
            throw new CustomException(ErrorCode.EXPIRED_AUTHKEY);
        }

        if (!redisAuthKey.equals(authKey)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_AUTHKEY);
        }

        redisUtil.deleteData(email);
        return BaseResponse.successMessage("인증이 완료되었습니다.");
    }
}
