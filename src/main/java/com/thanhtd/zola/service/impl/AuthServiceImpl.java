package com.thanhtd.zola.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.common.Status;
import com.thanhtd.zola.core.exception.LogicException;
import com.thanhtd.zola.core.util.SharedAlgorithm;
import com.thanhtd.zola.dao.RoleDao;
import com.thanhtd.zola.dao.UserDao;
import com.thanhtd.zola.dao.UserRoleDao;
import com.thanhtd.zola.dto.*;
import com.thanhtd.zola.model.Role;
import com.thanhtd.zola.model.User;
import com.thanhtd.zola.model.UserRole;
import com.thanhtd.zola.security.JwtService;
import com.thanhtd.zola.service.AuthService;
import com.thanhtd.zola.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    private boolean validEmail;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ObjectMapper jsonMapper;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User signUp(UserInfo userInfo) throws Exception {
        if (ObjectUtils.isEmpty(userInfo.getPhone())) throw new LogicException(ErrorCode.BLANK_FIELD);
        User user = userDao.findByPhone(userInfo.getPhone());
        if (!ObjectUtils.isEmpty(user)) throw new LogicException(ErrorCode.DUPLICATE_ERROR);

        user = new User();

        if (ObjectUtils.isEmpty(userInfo.getFirstName()) || ObjectUtils.isEmpty(userInfo.getLastName())
                || ObjectUtils.isEmpty(userInfo.getPassword()) || ObjectUtils.isEmpty(userInfo.getConfirmPassword()))
            throw new LogicException(ErrorCode.BLANK_FIELD);

        user.setFirstName(userInfo.getFirstName());
        user.setLastName(userInfo.getLastName());
        user.setPhone(userInfo.getPhone());
        user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        user.setConfirmPassword(userInfo.getConfirmPassword());
        if (!ObjectUtils.isEmpty(userInfo.getDisplayName())) {
            user.setDisplayName(userInfo.getDisplayName());
        }
        if (!ObjectUtils.isEmpty(userInfo.getAddress())) {
            user.setAddress(userInfo.getAddress());
        }
        if (!ObjectUtils.isEmpty(userInfo.getBirthday())) {
            user.setBirthday(userInfo.getBirthday());
        }
        if (!ObjectUtils.isEmpty(userInfo.getGender())) {
            user.setGender(userInfo.getGender());
        }
        if (!ObjectUtils.isEmpty(userInfo.getEmail()) && SharedAlgorithm.emailValidate(userInfo.getEmail())) {
            user.setEmail(userInfo.getEmail());
        }
        user.setActive(0);
        user.setDeleted(false);
        user.setStatus("ONLINE");
        return userDao.save(user);
    }

    @Override
    public SessionInfo login(LoginInfo loginInfo) throws Exception {
        Authentication userAuth = new UsernamePasswordAuthenticationToken(loginInfo.getPhone(), loginInfo.getPassword());

        Authentication authentication = authenticationManager
                .authenticate(userAuth);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        if (ObjectUtils.isEmpty(user)) throw new LogicException(ErrorCode.INVALID_CREDENTIAL);

        SessionInfo sessionInfo = new SessionInfo();
        UserProfile userProfile = getUserProfile(loginInfo);
        sessionInfo.setUserProfile(userProfile);
        String token = jwtService.generateToken(user);
        String subject = jwtService.extractPhone(token);
        sessionInfo.setToken(token);
        sessionInfo.setExpireIn(jwtService.getExpireIn());

        return sessionInfo;
    }

    private UserProfile getUserProfile(LoginInfo loginInfo) {
        User user = userDao.findByPhone(loginInfo.getPhone());
        if (ObjectUtils.isEmpty(user) || user.getActive() != 1) return null;

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(user.getUserId());
        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        userProfile.setDisplayName(user.getDisplayName());
        userProfile.setPhone(user.getPhone());
        userProfile.setEmail(user.getEmail());
        userProfile.setBirthday(user.getBirthday());
        userProfile.setAddress(user.getAddress());
        userProfile.setAvatar(user.getAvatar());
        Integer gender = user.getGender();
        if (!ObjectUtils.isEmpty(user.getGender()))
            userProfile.setGender(gender == 1 ? "MALE" : gender == 2 ? "FEMALE" : "OTHER");

        List<UserRole> userRoles = userRoleDao.findByUserId(user.getUserId());
        StringBuilder roleNames = new StringBuilder();
        int size = userRoles.size();
        for (int i = 0; i < size; i ++) {
            Role role = roleDao.findByRoleId(userRoles.get(i).getRoleId());
            if (ObjectUtils.isEmpty(role)) continue;
            roleNames.append(role.getName());
            if (i != size - 1) roleNames.append("-");
        }
        userProfile.setRoleNames(roleNames.toString());
        return userProfile;
    }

    private String generateVerifyCode() {
        Random random = new Random();
        int number = random.nextInt((int) Math.pow(10, 6));
        return String.format("%06d", number);
    }
}
