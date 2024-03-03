package com.thanhtd.zola.dao;

import com.thanhtd.zola.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageDao extends JpaRepository<UserMessage, Long> {

    UserMessage findByMessageId(Long messageId);
}
