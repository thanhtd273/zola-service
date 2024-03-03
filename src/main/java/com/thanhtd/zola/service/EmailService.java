package com.thanhtd.zola.service;

import com.thanhtd.zola.core.common.ErrorCode;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    SimpleMailMessage sendSimpleMessage(String to, String subject, String text) throws Exception;

    SimpleMailMessage sendSimpleMessage(String[] to, String subject, String text) throws Exception;
}
