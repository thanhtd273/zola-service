package com.thanhtd.zola.controller;

import com.thanhtd.zola.core.APIResponse;
import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.exception.ExceptionHandler;
import com.thanhtd.zola.dto.ConversationInfo;
import com.thanhtd.zola.model.Conversation;
import com.thanhtd.zola.service.ConversationService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zola-service/conversation")
public class ConversationController {

    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);

    @Autowired
    private ConversationService conversationService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse getAllConversations() {
        long start = System.currentTimeMillis();
        try {
            List<Conversation> conversations = conversationService.findAll();
            long took = System.currentTimeMillis() - start;
            logger.info("GET /zola-service/conversation success, {} records, took {}ms", conversations.size(), took);
            return new APIResponse(ErrorCode.OK, "", took, conversations);
        } catch (Exception e) {
            logger.error("GET /zola-service/conversation fail, took {} ms", System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public APIResponse getConversationById(@PathVariable("id") Long conversationId) {
        long start = System.currentTimeMillis();
        try {
            Conversation conversation = conversationService.findByConversationId(conversationId);
            long took = System.currentTimeMillis() - start;
            logger.info("GET /zola-service/conversation/{} success, took {}ms", conversationId, took);
            return new APIResponse(ErrorCode.OK, "", took, conversation);
        } catch (Exception e) {
            logger.error("GET /zola-service/conversation/{} fail, took {} ms", conversationId, System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/my_conversations", method = RequestMethod.GET)
    public APIResponse getMyConversations(@RequestHeader(value = "userId", defaultValue = "0") Long userId) {
        long start = System.currentTimeMillis();
        try {
            List<ConversationInfo> conversations = conversationService.getMyConversation();
            long took = System.currentTimeMillis() - start;
            logger.info("GET /zola-service/conversation/my_conversations success, took {}ms", took);
            return new APIResponse(ErrorCode.OK, "", took, conversations);
        } catch (Exception e) {
            logger.error("GET /zola-service/conversation/my_conversations fail, took {} ms", System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/search-by-name", method = RequestMethod.GET)
    public APIResponse getConversationByName(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestHeader(value = "userId", defaultValue = "0") Long userId) {
        long start = System.currentTimeMillis();
        try {
            List<Conversation> conversations = conversationService.findByName(name);
            long took = System.currentTimeMillis() - start;
            logger.info("GET /zola-service/conversation?name={} success, took {}ms", name, took);
            return new APIResponse(ErrorCode.OK, "", took, conversations);
        } catch (Exception e) {
            logger.error("GET /zola-service/conversation?name={} fail, took {} ms", name, System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse createConversation(@RequestBody ConversationInfo conversationInfo) {
        long start = System.currentTimeMillis();
        try {
            Conversation conversation = conversationService.createConversation(conversationInfo);
            long took = System.currentTimeMillis() - start;
            logger.info("POST /zola-service/conversation success, took {}ms", took);
            return new APIResponse(ErrorCode.OK, "", took, conversation);
        } catch (Exception e) {
            logger.error("POST /zola-service/conversation fail, took {} ms", System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public APIResponse updateConversation(@PathVariable("id") Long conversationId, @RequestBody ConversationInfo conversationInfo) {
        long start = System.currentTimeMillis();
        try {
            Conversation conversation = conversationService.updateConversation(conversationId, conversationInfo);
            long took = System.currentTimeMillis() - start;
            logger.info("PUT /zola-service/conversation/{} success, took {}ms", conversationId, took);
            return new APIResponse(ErrorCode.OK, "", took, conversation);
        } catch (Exception e) {
            logger.error("PUT /zola-service/conversation/{} fail, took {} ms", conversationId, System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public APIResponse deleteConversation(@PathVariable("id") Long conversationId) {
        long start = System.currentTimeMillis();
        try {
            ErrorCode errorCode = conversationService.deleteConversation(conversationId);
            long took = System.currentTimeMillis() - start;
            logger.info("DELETE /zola-service/conversation/{} success, took {}ms", conversationId, took);
            return new APIResponse(errorCode, "", took, null);
        } catch (Exception e) {
            logger.error("DELETE /zola-service/conversation/{} fail, took {} ms", conversationId, System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/add_members", method = RequestMethod.POST)
    public APIResponse addMembers(@NotNull @RequestBody ConversationInfo conversationInfo) {
        long start = System.currentTimeMillis();
        try {
            ConversationInfo conversation = conversationService
                    .addMembers(conversationInfo.getConversationId(), conversationInfo.getMemberIdsStr());
            long took = System.currentTimeMillis() - start;
            logger.info("POST /zola-service/conversation success, took {}ms", took);
            return new APIResponse(ErrorCode.OK, "", took, conversation);
        } catch (Exception e) {
            logger.error("POST /zola-service/conversation fail, took {} ms", System.currentTimeMillis() - start);
            return ExceptionHandler.handleException(e, start);
        }
    }
}
