package com.thanhtd.zola.service.impl;

import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.exception.LogicException;
import com.thanhtd.zola.dao.ConversationDao;
import com.thanhtd.zola.dao.ConversationMemberDao;
import com.thanhtd.zola.dao.RoleDao;
import com.thanhtd.zola.dao.UserDao;
import com.thanhtd.zola.dto.UserInfo;
import com.thanhtd.zola.model.Conversation;
import com.thanhtd.zola.model.ConversationMember;
import com.thanhtd.zola.model.Role;
import com.thanhtd.zola.model.User;
import com.thanhtd.zola.service.RedisService;
import com.thanhtd.zola.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final String PREFIX = "user";
    private static final int REDIS_TTL = 300;
    @Autowired
    private UserDao userDao;

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private ConversationMemberDao conversationMemberDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() throws IOException {
        String key = redisService.createKey(PREFIX, "all");
        List<User> users =  redisService.getListObjects(key, User.class);
        if (ObjectUtils.isEmpty(users)) {
            users = userDao.findAll();
            List<Object> objects = users.stream().map(user -> (Object) user).toList();
            if (!ObjectUtils.isEmpty(users)) redisService.saveList(key, objects, REDIS_TTL);
        }
        return users;
    }

    @Override
    public User findByUserId(Long userId) throws Exception {
        if (Objects.isNull(userId)) throw new LogicException(ErrorCode.ID_NULL);
        String key = redisService.createKey(PREFIX, String.valueOf(userId));
        User user = (User) redisService.getObject(key, User.class);
        if (ObjectUtils.isEmpty(user)) {
            user = userDao.findByUserId(userId);
            if (!ObjectUtils.isEmpty(user)) redisService.saveObject(key, user, REDIS_TTL);
        }
        return user;
    }

    @Override
    public User getCurrentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) throw new LogicException(ErrorCode.LOGIN_FAIL);
        return (User) authentication.getPrincipal();
    }

    @Override
    public User findByPhone(String phone) throws Exception {
        if (ObjectUtils.isEmpty(phone)) throw new LogicException(ErrorCode.DATA_NULL);
        String key = redisService.createKey(PREFIX, phone);
        User user = (User) redisService.getObject(key, User.class);
        if (ObjectUtils.isEmpty(user)) {
            user = userDao.findByPhone(phone);
            if (!ObjectUtils.isEmpty(user)) redisService.saveObject(key, user, REDIS_TTL);
        }
        return user;
    }

    @Override
    public List<User> findUsersInConversation(Long conversationId) throws Exception {
        if (ObjectUtils.isEmpty(conversationId)) throw new LogicException(ErrorCode.ID_NULL);
        Conversation conversation = conversationDao.findByConversationId(conversationId);
        if (ObjectUtils.isEmpty(conversation)) throw new LogicException(ErrorCode.DATA_NULL);
        if (conversation.getDeleted()) throw new LogicException(ErrorCode.DELETED_DATA);

        List<ConversationMember> conversationMembers = conversationMemberDao.findByConversationId(conversation.getConversationId());

        List<User> users = new ArrayList<>();
        for (ConversationMember conversationMember: conversationMembers) {
            User user = findByUserId(conversationMember.getMemberId());
            if (ObjectUtils.isEmpty(user)) continue;
            users.add(user);
        }
        return users;
    }


    @Override
    public User updateUser(Long userId, UserInfo userInfo) throws Exception {
        if (ObjectUtils.isEmpty(userId)) throw new LogicException(ErrorCode.ID_NULL);

        User user = findByUserId(userId);
        if (ObjectUtils.isEmpty(user)) throw new LogicException(ErrorCode.DATA_NULL);

        if (!ObjectUtils.isEmpty(userInfo.getFirstName()))
            user.setFirstName(userInfo.getFirstName());
        if (!ObjectUtils.isEmpty(userInfo.getLastName()))
            user.setLastName(userInfo.getLastName());
        if (!ObjectUtils.isEmpty(userInfo.getDisplayName()))
            user.setDisplayName(userInfo.getDisplayName());
        if (!ObjectUtils.isEmpty(userInfo.getBirthday()))
            user.setBirthday(userInfo.getBirthday());
        if (!ObjectUtils.isEmpty(userInfo.getAddress()))
            user.setAddress(userInfo.getAddress());
        if (!ObjectUtils.isEmpty(userInfo.getActive()))
            user.setActive(userInfo.getActive());
        if (!ObjectUtils.isEmpty(userInfo.getAvatar()))
            user.setAvatar(userInfo.getAvatar());
        if (!ObjectUtils.isEmpty(userInfo.getGender()))
            user.setGender(userInfo.getGender());

        if (!ObjectUtils.isEmpty(userInfo.getRoles())) {
            List<String> rolesName = userInfo.getRoles();
            Set<Role> roles = new HashSet<>();
            for (String roleName: rolesName) {
                Role role = roleDao.findByName(roleName);
                if (ObjectUtils.isEmpty(role)) continue;
                roles.add(role);
            }
            user.setRoles(roles);
        }

        user.setModifiedDate(new Timestamp(System.currentTimeMillis()));

        user = userDao.save(user);

        // Update cache
        String key = redisService.createKey(PREFIX, String.valueOf(user.getUserId()));
        // TODO: update, not delete then set
        redisService.delete(key);
        redisService.saveObject(key, user, REDIS_TTL);
        return user;
    }

    @Override
    public ErrorCode deleteUser(Long userId) throws Exception {
        if (ObjectUtils.isEmpty(userId)) throw new LogicException(ErrorCode.ID_NULL);
        User user = findByUserId(userId);
        if (ObjectUtils.isEmpty(user)) throw new LogicException(ErrorCode.DATA_NULL);

        user.setDeleted(true);
        user.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        userDao.save(user);

        // Delete cache
        String key = redisService.createKey(PREFIX, String.valueOf(user.getUserId()));
        redisService.delete(key);
        return ErrorCode.OK;
    }

    @Override
    public User activeUser(Long userId) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setActive(1);

        return updateUser(userId, userInfo);
    }

    @Override
    public ErrorCode changePassword(UserInfo userInfo) throws Exception {
        if (ObjectUtils.isEmpty(userInfo)) return ErrorCode.DATA_NULL;
        if (ObjectUtils.isEmpty(userInfo.getPhone()) || ObjectUtils.isEmpty(userInfo.getPassword()))
            return ErrorCode.ID_NULL;
        // Send reset email OTP

        // Verify OTP

        // Change password
        User user = findByPhone(userInfo.getPhone());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        return ErrorCode.OK;
    }

    @Override
    public User forgotPassword(Long userId) throws Exception {
        return null;
    }

}
