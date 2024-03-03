package com.thanhtd.zola.service.impl;

import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.exception.LogicException;
import com.thanhtd.zola.core.util.SharedAlgorithm;
import com.thanhtd.zola.dao.ConversationDao;
import com.thanhtd.zola.dao.ConversationMemberDao;
import com.thanhtd.zola.dao.UserDao;
import com.thanhtd.zola.dto.ConversationInfo;
import com.thanhtd.zola.model.*;
import com.thanhtd.zola.service.ConversationService;
import com.thanhtd.zola.service.MessageService;
import com.thanhtd.zola.service.RedisService;
import com.thanhtd.zola.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// TODO: check permission
@Service
public class ConversationServiceImpl implements ConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationServiceImpl.class);

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private ConversationMemberDao conversationMemberDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Override
    public List<Conversation> findAll() throws Exception {
        return conversationDao.findAllConversations();
    }

    @Override
    public Conversation findByConversationId(Long conversationId) throws Exception {
        if (ObjectUtils.isEmpty(conversationId)) throw new LogicException(ErrorCode.ID_NULL);

        return conversationDao.findByConversationId(conversationId);
    }

    @Override
    public List<ConversationInfo> getMyConversation() throws Exception {
        User user = userService.getCurrentUser();
        if (ObjectUtils.isEmpty(user)) throw new LogicException(ErrorCode.DATA_NULL);

        return findByUserId(user.getUserId());
    }

    @Override
    public List<Conversation> findByName(String name) throws Exception {

        User user = userService.getCurrentUser();
        String regex = "^" + name + ".";
        List<Conversation> conversations = new ArrayList<>();

        List<ConversationMember> conversationMembers = conversationMemberDao.findByMemberId(user.getUserId());
        for (ConversationMember conversationMember: conversationMembers) {
            Conversation conversation = findByConversationId(conversationMember.getConversationId());
            if (ObjectUtils.isEmpty(conversation)) continue;
            if (conversation.getName().matches(regex)) conversations.add(conversation);
        }
        return conversations;
    }

    @Override
    public Conversation createConversation(@NotNull ConversationInfo conversationInfo) throws Exception {
        String[] memberIds = conversationInfo.getMemberIdsStr().split("-");
        if (ObjectUtils.isEmpty(memberIds) || memberIds.length < 2)
            throw new LogicException(ErrorCode.DATA_NULL);

        Conversation conversation = new Conversation();
        if (!ObjectUtils.isEmpty(conversationInfo.getName()))
            conversation.setName(conversationInfo.getName());
        if (!ObjectUtils.isEmpty(conversationInfo.getAvatar()))
            conversation.setAvatar(conversationInfo.getAvatar());
        conversation.setStar(false);
        conversation.setMuteCode(0);
        conversation.setDeleted(false);
        conversation.setCreateDate(new Timestamp(System.currentTimeMillis()));

        conversation = conversationDao.save(conversation);

        for (String memberIdStr: memberIds) {
            if (!SharedAlgorithm.isNumeric(memberIdStr)) continue;
            long memberId = Long.parseLong(memberIdStr);
            User member = userDao.findByUserId(memberId);
            if (ObjectUtils.isEmpty(member)) continue;
            addMember(conversation.getConversationId(), memberId);
        }

        return conversation;
    }

    @Override
    public Conversation updateConversation(Long conversationId, @NotNull ConversationInfo conversationInfo) throws Exception {
        Conversation conversation = findByConversationId(conversationId);
        if (ObjectUtils.isEmpty(conversation)) throw new LogicException(ErrorCode.DATA_NULL);

        if (!ObjectUtils.isEmpty(conversationInfo.getName()))
            conversation.setName(conversationInfo.getName());
        if (!ObjectUtils.isEmpty(conversationInfo.getAvatar()))
            conversation.setAvatar(conversationInfo.getAvatar());
        if (!ObjectUtils.isEmpty(conversationInfo.getStar()))
            conversation.setStar(conversationInfo.getStar());
        if (!ObjectUtils.isEmpty(conversationInfo.getMuteCode()))
            conversation.setMuteCode(conversationInfo.getMuteCode());
        if (!ObjectUtils.isEmpty(conversationInfo.getLabelId()))
            conversation.setLabelId(conversationInfo.getLabelId());
        conversation.setModifiedDate(new Timestamp(System.currentTimeMillis()));

        return conversationDao.save(conversation);
    }

    @Override
    public ErrorCode deleteConversation(Long conversationId) throws Exception {
        Conversation conversation = findByConversationId(conversationId);
        if (ObjectUtils.isEmpty(conversation)) throw new LogicException(ErrorCode.DATA_NULL);

        conversation.setDeleted(true);
        conversation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        return ErrorCode.OK;
    }

    @Override
    public ConversationInfo addMembers(Long conversationId, String userIdsStr) throws Exception {
        // TODO: check if user already exists in conversation
        for (String userIdStr: userIdsStr.split("-")) {
            if (ObjectUtils.isEmpty(userIdStr) || SharedAlgorithm.isNumeric(userIdsStr)) continue;
            addMember(conversationId, Long.parseLong(userIdStr));
        }
        return getConversationInfo(conversationId);
    }

    @Override
    public ConversationInfo getConversationInfo(Long conversationId) throws Exception {
        Conversation conversation = findByConversationId(conversationId);
        ConversationInfo conversationInfo = new ConversationInfo();
        conversationInfo.setConversationId(conversation.getConversationId());

        List<ConversationMember> members = conversationMemberDao.findByConversationId(conversation.getConversationId());
        conversationInfo.setIsGroup(members.size() > 2);

        Message message = messageService.findById(conversation.getLastMessageId());
        conversationInfo.setLastMessage(message);
        User sender = messageService.findMessageSender(message.getMessageId());
        if (ObjectUtils.isEmpty(sender)) throw new LogicException(ErrorCode.DATA_NULL);
        conversationInfo.setLastMessageSender(sender.getDisplayName());

        User currentUser = userService.getCurrentUser();
        ConversationMember conversationMember = conversationMemberDao
                .findByConversationAndMember(conversationId, currentUser.getUserId());
        conversationInfo.setLastSeenAt(conversationMember.getLastSeenAt());

        conversationInfo.setName(conversation.getName()); // TODO
        conversationInfo.setAvatar(conversation.getAvatar());
        conversationInfo.setStar(conversation.getStar());
        conversationInfo.setMuteCode(conversation.getMuteCode());
        conversationInfo.setLabelId(conversation.getLabelId());

        List<Long> memberIds = members.stream().map(ConversationMember::getMemberId).toList();
        conversationInfo.setMemberIdsStr(SharedAlgorithm.buildIdsStr(memberIds));

        return conversationInfo;
    }

    private void addMember(Long conversationId, Long userId) throws Exception {
        if (ObjectUtils.isEmpty(userId)) throw new LogicException(ErrorCode.ID_NULL);
        User member = userDao.findByUserId(userId);
        Conversation conversation = findByConversationId(conversationId);
        if (ObjectUtils.isEmpty(conversation) || ObjectUtils.isEmpty(member))
            throw new LogicException(ErrorCode.DATA_NULL);

        ConversationMember conversationMember = new ConversationMember();
        conversationMember.setConversationId(conversationId);
        conversationMember.setMemberId(userId);
        conversationMember.setLastSeenAt(new Timestamp(System.currentTimeMillis()));
        // TODO: add role for member
        conversationMemberDao.save(conversationMember);

        conversation.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        conversationDao.save(conversation);
    }

    private List<ConversationInfo> findByUserId(Long userId) throws Exception {
        if (ObjectUtils.isEmpty(userId)) throw new LogicException(ErrorCode.ID_NULL);

        List<ConversationInfo> conversationInfoList = new ArrayList<>();

        List<ConversationMember> conversationMembers = conversationMemberDao.findByMemberId(userId);
        for (ConversationMember conversationMember: conversationMembers) {
            Conversation conversation = findByConversationId(conversationMember.getConversationId());
            if (ObjectUtils.isEmpty(conversation)) continue;

            ConversationInfo conversationInfo = getConversationInfo(conversation.getConversationId());
            conversationInfoList.add(conversationInfo);
        }

        return conversationInfoList;
    }
}
