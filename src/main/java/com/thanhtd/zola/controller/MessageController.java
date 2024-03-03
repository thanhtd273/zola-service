package com.thanhtd.zola.controller;

import com.thanhtd.zola.core.APIResponse;
import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.common.GlobalConstant;
import com.thanhtd.zola.core.exception.ExceptionHandler;
import com.thanhtd.zola.core.exception.GlobalException;
import com.thanhtd.zola.dto.MessageInfo;
import com.thanhtd.zola.model.Message;
import com.thanhtd.zola.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zola-service/message")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/in_conversation/{id}", method = RequestMethod.GET)
    public APIResponse getMessagesInConversation(@PathVariable("id") Long conversationId) {
        long start = System.currentTimeMillis();
        try {
            List<Message> messageList = messageService.getMessagesInConversation(conversationId);
            logger.info("GET /zola-service/message/in_conversation/{} success", conversationId);
            return new APIResponse(ErrorCode.OK, "", System.currentTimeMillis() - start, messageList);
        } catch (Exception e) {
            logger.error("GET /zola-service/message/in_conversation/{} fail, took {} ms", conversationId, System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse createMessage(@RequestBody MessageInfo messageInfo) {
        long start = System.currentTimeMillis();
        try {
            Message message = messageService.createMessage(messageInfo);
            logger.info("POST /zola-service/message success, created messageId = {}", message.getMessageId());
            return new APIResponse(ErrorCode.OK, "", System.currentTimeMillis() - start, message);
        } catch (Exception e) {
            logger.error("POST /zola-service/message fail, took {} ms", System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }

}
