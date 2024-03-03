package com.thanhtd.zola.service;

import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.dto.ConversationInfo;
import com.thanhtd.zola.model.Conversation;

import java.util.List;

public interface ConversationService {
    List<Conversation> findAll() throws Exception;

    Conversation findByConversationId(Long conversationId) throws Exception;

//    List<Conversation> findByUserId(Long userId) throws Exception;

    List<ConversationInfo> getMyConversation() throws Exception;

    List<Conversation> findByName(String name) throws Exception;

    Conversation createConversation(ConversationInfo conversationInfo) throws Exception;

    Conversation updateConversation(Long conversationId, ConversationInfo conversationInfo) throws Exception;

    ErrorCode deleteConversation(Long conversationId) throws Exception;

    ConversationInfo addMembers(Long conversationId, String userIdsStr) throws Exception;

    ConversationInfo getConversationInfo(Long conversationId) throws Exception;
}
