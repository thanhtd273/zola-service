package com.thanhtd.zola.service;

import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.dto.UserInfo;
import com.thanhtd.zola.model.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<User> findAll() throws IOException;

    User findByUserId(Long userId) throws Exception;

    User getCurrentUser() throws Exception;

    User findByPhone(String phone) throws Exception;

    List<User> findUsersInConversation(Long conversationId) throws Exception;

    User updateUser(Long userId, UserInfo userInfo) throws Exception;

    ErrorCode deleteUser(Long userId) throws Exception;

    User activeUser(Long userId) throws Exception;

    ErrorCode changePassword(UserInfo userInfo) throws Exception;

    User forgotPassword(Long userId) throws Exception;
}
