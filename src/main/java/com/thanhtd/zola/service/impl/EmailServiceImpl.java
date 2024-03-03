package com.thanhtd.zola.service.impl;

import com.thanhtd.zola.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public SimpleMailMessage sendSimpleMessage(String to, String subject, String text) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(text);
        message.setTo(to);
        message.setSubject(subject);
        mailSender.send(message);
        return message;
    }

    @Override
    public SimpleMailMessage sendSimpleMessage(String[] to, String subject, String text) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(text);
        message.setTo(to);
        message.setSubject(subject);
        mailSender.send(message);
        return message;
    }
}
