package com.thanhtd.zola.dao;

import com.thanhtd.zola.model.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMemberDao extends JpaRepository<ConversationMember, Long> {

    List<ConversationMember> findByConversationId(Long conversationId);

    List<ConversationMember> findByMemberId(Long memberId);


    @Query("SELECT u FROM ConversationMember u WHERE u.conversationId = :conversationId AND u.memberId = :memberId")
    ConversationMember findByConversationAndMember(Long conversationId, Long memberId);
}
