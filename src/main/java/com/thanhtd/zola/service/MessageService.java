package com.thanhtd.zola.service;

import com.thanhtd.zola.dto.MessageInfo;
import com.thanhtd.zola.model.Message;
import com.thanhtd.zola.model.User;

import java.util.List;

public interface MessageService {
    List<Message> findAll() throws Exception;
    Message findById(Long messageId) throws Exception;

    User findMessageSender(Long messageId) throws Exception;

    Message createMessage(MessageInfo messageInfo) throws Exception;

    Message editMessage(Long messageId, MessageInfo messageInfo) throws Exception;

    Message recallMessage(Long messageId) throws Exception;

    List<Message> findByConversationId(Long conversationId) throws Exception;

    List<Message> getMessagesInConversation(Long conversationId) throws Exception;
}
