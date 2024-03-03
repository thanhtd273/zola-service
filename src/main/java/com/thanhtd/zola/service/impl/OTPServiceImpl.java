package com.thanhtd.zola.service.impl;

import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.dto.OTPInfo;
import com.thanhtd.zola.service.EmailService;
import com.thanhtd.zola.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;

@Service
public class OTPServiceImpl implements OTPService {

    private static final String NUMBERS = "0123456789";

    @Autowired
    private EmailService emailService;

    @Override
    public ErrorCode sendOTP(String toEmail, String subject) {
        try {
            SimpleMailMessage message = emailService.sendSimpleMessage(toEmail, subject, generateOTP().getCode());

            return ErrorCode.OK;
        } catch (Exception e) {

            return ErrorCode.FAIL;
        }
    }

    @Override
    public ErrorCode verifyOTP(String OTP, Timestamp timestamp) {
        try {

            return ErrorCode.OK;
        } catch (Exception e) {

            return ErrorCode.FAIL;
        }
    }

    private OTPInfo generateOTP() {
        StringBuilder code = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 6; i ++) {
            int index = secureRandom.nextInt(NUMBERS.length());
            code.append(NUMBERS.charAt(index));
        }
        return new OTPInfo(code.toString(), new Timestamp(System.currentTimeMillis()));
    }
}
