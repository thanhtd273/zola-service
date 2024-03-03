package com.thanhtd.zola.dao;

import com.thanhtd.zola.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends JpaRepository<Message, Long> {
    @Query("SELECT u FROM Message u WHERE u.deleted = false AND u.messageId = :messageId")
    Message findByMessageId(@Param("messageId") Long messageId);

}
