package com.thanhtd.zola.service.impl;

import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.exception.LogicException;
import com.thanhtd.zola.dao.ConversationDao;
import com.thanhtd.zola.dao.ConversationMessageDao;
import com.thanhtd.zola.dao.MessageDao;
import com.thanhtd.zola.dao.UserMessageDao;
import com.thanhtd.zola.dto.MessageInfo;
import com.thanhtd.zola.model.*;
import com.thanhtd.zola.service.MessageService;
import com.thanhtd.zola.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ConversationMessageDao conversationMessageDao;

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private UserMessageDao userMessageDao;

    @Autowired
    private UserService userService;

    @Override
    public List<Message> findAll() throws Exception {
        return messageDao.findAll();
    }

    @Override
    public Message findById(Long messageId) throws Exception {
        if (ObjectUtils.isEmpty(messageId)) throw new LogicException(ErrorCode.ID_NULL);
        return messageDao.findByMessageId(messageId);
    }

    @Override
    public User findMessageSender(Long messageId) throws Exception {
        if (ObjectUtils.isEmpty(messageId)) throw new LogicException(ErrorCode.ID_NULL);

        UserMessage userMessage = userMessageDao.findByMessageId(messageId);
        if (ObjectUtils.isEmpty(userMessage)) throw new LogicException(ErrorCode.DATA_NULL);
        return userService.findByUserId(userMessage.getOwnerId());
    }

    @Override
    public Message createMessage(MessageInfo messageInfo) throws Exception {
        Message message = new Message();
        ConversationMessage conversationMessage = new ConversationMessage();

        if (ObjectUtils.isEmpty(messageInfo.getConversationId()))
            throw new LogicException(ErrorCode.ID_NULL);
        conversationMessage.setConversationId(messageInfo.getConversationId());
        Conversation conversation = conversationDao.findByConversationId(messageInfo.getConversationId());
        if (ObjectUtils.isEmpty(conversation)) throw new LogicException(ErrorCode.DATA_NULL, "No such conversation");

        if (ObjectUtils.isEmpty(messageInfo.getContent()))
            throw new LogicException(ErrorCode.BLANK_FIELD);
        message.setContent(messageInfo.getContent());

        if (!ObjectUtils.isEmpty(messageInfo.getPriority()))
            message.setPriority(messageInfo.getPriority());

        message.setDeleted(false);
        message.setCreateDate(new Timestamp(System.currentTimeMillis()));
        message = messageDao.save(message);

        // add to user_message
        UserMessage userMessage = new UserMessage();
        userMessage.setMessageId(message.getMessageId());
        User currentUser = userService.getCurrentUser();
        userMessage.setOwnerId(currentUser.getUserId());
        userMessageDao.save(userMessage);

        // Update conversation_message
        conversationMessage.setMessageId(message.getMessageId());
        conversationMessageDao.save(conversationMessage);

        // Update conversation
        conversation.setLastMessageId(message.getMessageId());
        conversationDao.save(conversation);
        return message;
    }

    @Override
    public Message editMessage(Long messageId, MessageInfo messageInfo) throws Exception {
        if (ObjectUtils.isEmpty(messageId)) throw new LogicException(ErrorCode.ID_NULL);
        Message message = messageDao.findByMessageId(messageId);
        if (ObjectUtils.isEmpty(message)) throw new LogicException(ErrorCode.DATA_NULL);

        if (!ObjectUtils.isEmpty(messageInfo.getContent()))
            message.setContent(messageInfo.getContent());
        if (!ObjectUtils.isEmpty(messageInfo.getPriority()))
            message.setPriority(messageInfo.getPriority());

        message.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        return messageDao.save(message);
    }

    @Override
    public Message recallMessage(Long messageId) throws Exception {
        if (ObjectUtils.isEmpty(messageId)) throw new LogicException(ErrorCode.ID_NULL);
        Message message = messageDao.findByMessageId(messageId);
        if (ObjectUtils.isEmpty(message)) throw new LogicException(ErrorCode.DATA_NULL);

        message.setDeleted(true);
        message.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        return messageDao.save(message);
    }

    @Override
    public List<Message> findByConversationId(Long conversationId) throws Exception {
        if (ObjectUtils.isEmpty(conversationId)) throw new LogicException(ErrorCode.ID_NULL);
        List<ConversationMessage> conversationMessages = conversationMessageDao.findByConversationId(conversationId);
        List<Message> messages = new ArrayList<>();
        for (ConversationMessage conversationMessage: conversationMessages) {
            Message message = messageDao.findByMessageId(conversationMessage.getMessageId());
            if (ObjectUtils.isEmpty(message)) continue;
            messages.add(message);
        }

        return messages;
    }

    @Override
    public List<Message> getMessagesInConversation(Long conversationId) throws Exception {
        return null;
    }
}
