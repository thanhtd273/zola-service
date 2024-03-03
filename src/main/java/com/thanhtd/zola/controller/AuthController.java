package com.thanhtd.zola.controller;

import com.thanhtd.zola.core.APIResponse;
import com.thanhtd.zola.core.common.ErrorCode;
import com.thanhtd.zola.core.exception.ExceptionHandler;
import com.thanhtd.zola.dto.LoginInfo;
import com.thanhtd.zola.dto.SMSInfo;
import com.thanhtd.zola.dto.SessionInfo;
import com.thanhtd.zola.dto.UserInfo;
import com.thanhtd.zola.model.User;
import com.thanhtd.zola.service.AuthService;
import com.thanhtd.zola.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zola-service")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${twilio.account.sid}")
    private String TWILIO_ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String TWILIO_AUTH_TOKEN;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

//    @RequestMapping(value = "/send-opt", method = RequestMethod.POST)
//    public APIResponse sendOPT(@RequestBody SMSInfo smsInfo) {
//        long start = System.currentTimeMillis();
//        try {
//            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
//            Message.creator(new PhoneNumber(smsInfo.getDestinationPhone()),
//                    new PhoneNumber("+84926272923"), smsInfo.getMessage()).create();
//            return new APIResponse(ErrorCode.OK, "", System.currentTimeMillis() - start, smsInfo.getMessage());
//        } catch (Exception e) {
//            logger.error("POST /zola-service/send-opt fail, error: {}", e.getMessage());
//            return ExceptionHandler.handleException(e, start);
//        }
//
//    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public APIResponse signUp(HttpServletRequest request, @Valid @RequestBody UserInfo userInfo) {
        long start = System.currentTimeMillis();
        try {
            User user = authService.signUp(userInfo);
            logger.info("POST /zola-service/sign-up success");
            return new APIResponse(ErrorCode.OK, "", System.currentTimeMillis() - start, user);

        } catch (Exception e) {
            logger.error("POST /zola-service/sign-up fail, error: {}", e.getMessage());
            return ExceptionHandler.handleException(e, start);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public APIResponse login(HttpServletRequest request, @Valid @RequestBody LoginInfo loginInfo) {
        long start = System.currentTimeMillis();
        logger.info("Log in with phone={}, password={}", loginInfo.getPhone(), loginInfo.getPassword());
        try {
            SessionInfo successLoginInfo =  authService.login(loginInfo);
            return new APIResponse(ErrorCode.OK, "", System.currentTimeMillis() - start, successLoginInfo);
        } catch (Exception e) {
            logger.error("POST /zola-service/login fail");
            return ExceptionHandler.handleException(e, start);
        }
    }
}
