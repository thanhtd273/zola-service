package com.thanhtd.zola.dao;

import com.thanhtd.zola.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationDao extends JpaRepository<Conversation, Long> {

    @Query("SELECT u FROM Conversation u WHERE u.deleted = false")
    List<Conversation> findAllConversations();

    @Query("SELECT u FROM Conversation u WHERE u.deleted = false AND u.conversationId = :conversationId ")
    Conversation findByConversationId(@Param("conversationId") Long conversationId);

}
