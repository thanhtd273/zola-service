package com.thanhtd.zola.controller;

import com.thanhtd.zola.core.APIResponse;
import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.exception.ExceptionHandler;
import com.thanhtd.zola.dto.UserInfo;
import com.thanhtd.zola.model.User;
import com.thanhtd.zola.service.AuthService;
import com.thanhtd.zola.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zola-service/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse findAll() {
        long start = System.currentTimeMillis();
        try {
            List<User> users = userService.findAll();
            logger.info("GET /zola-service/users success, took: {}", System.currentTimeMillis() - start);
            return new APIResponse(ErrorCode.OK, "", System.currentTimeMillis() - start, users);
        } catch (Exception e) {
            logger.error("GET /zola-service/users fail: {}", e.getMessage());
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public APIResponse findByUserId(@PathVariable(name = "id") Long userId) {
        long start = System.currentTimeMillis();
        try {
            User user = userService.findByUserId(userId);
            logger.info("GET /zola-service/users/{} success", userId);
            return new APIResponse(ErrorCode.OK, "", System.currentTimeMillis() - start, user);
        } catch (Exception e) {
            logger.error("GET /zola-service/users/{} fail", userId);
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public APIResponse updateUser(@PathVariable(name = "id") Long userId, @Valid @RequestBody UserInfo userInfo) {
        long start = System.currentTimeMillis();
        try {
            User updateUser = userService.updateUser(userId, userInfo);
            long took = System.currentTimeMillis() - start;
            logger.info("PUT /zola-service/users success, took: {}", took);

            return new APIResponse(ErrorCode.OK, "", took, updateUser);
        } catch (Exception e) {
            logger.error("PUT /zola-service/users fail: {}", e.getMessage());
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public APIResponse deleteUser(@PathVariable(value = "id") Long userId) {
        long start = System.currentTimeMillis();
        try {
            ErrorCode errorCode = userService.deleteUser(userId);
            long took = System.currentTimeMillis() - start;
            logger.info("DELETE /zola-service/users success, took: {}", took);
            return new APIResponse(ErrorCode.OK, "", took, null);
        } catch (Exception e) {
            logger.error("DELETE /zola-service/users fail: {}", e.getMessage());
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/active/{id}", method = RequestMethod.PUT)
    public APIResponse activeUser(@PathVariable(value = "id") Long userId) {
        long start = System.currentTimeMillis();
        try {
            User user = userService.activeUser(userId);
            long took = System.currentTimeMillis() - start;
            logger.info("PUT /zola-service/users/active/{} success, took: {}", userId, took);
            return new APIResponse(ErrorCode.OK, "", took, user);
        } catch (Exception e) {
            logger.error("PUT /zola-service/users/active/{} fail, error: {}", userId, e.getMessage());
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.PUT)
    public APIResponse changePassword(@RequestBody UserInfo userInfo) {
        long start = System.currentTimeMillis();
        try {
            ErrorCode errorCode = userService.changePassword(userInfo);
            logger.info("PUT /zola-service/users/change-password success");
            return new APIResponse(errorCode, "Change password success", System.currentTimeMillis() - start, null);
        } catch (Exception e) {
            logger.error("PUT /zola-service/users/change-password fail, error: {}", e.getMessage());
            return ExceptionHandler.handleException(e, start);
        }
    }

}
