package com.thanhtd.zola.controller;

import com.thanhtd.zola.core.APIResponse;
import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.exception.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zola-service/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse kafkaSendMessage() throws Exception {
        long start = System.currentTimeMillis();
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("noreply@zola.com");
            mailMessage.setTo("dinhthanha12703@gmail.com");
            mailMessage.setSubject("send mail test");
            mailMessage.setText("success send mail");

            mailSender.send(mailMessage);
            return new APIResponse(ErrorCode.OK, "", System.currentTimeMillis() - start, mailMessage);
        } catch (Exception e) {

            return ExceptionHandler.handleException(e, start);
        }

    }

    @MessageMapping("/user.test")
    @SendTo("/user/topic")
    public APIResponse testSock() {
        long start = System.currentTimeMillis();
        try {
            logger.info("test send message success");
            return new APIResponse(ErrorCode.OK, "success", System.currentTimeMillis() - start, "success websocket");
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, start);
        }
    }


}
