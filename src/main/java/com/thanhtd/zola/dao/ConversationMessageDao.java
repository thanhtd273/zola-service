package com.thanhtd.zola.dao;

import com.thanhtd.zola.model.ConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMessageDao extends JpaRepository<ConversationMessage, Long> {
    List<ConversationMessage> findByConversationId(Long conversationId);
}
