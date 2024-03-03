package com.thanhtd.zola.service;

import com.thanhtd.zola.dto.LoginInfo;
import com.thanhtd.zola.dto.SessionInfo;
import com.thanhtd.zola.dto.UserInfo;
import com.thanhtd.zola.model.User;

public interface AuthService {
    User signUp(UserInfo userInfo) throws Exception;

    SessionInfo login(LoginInfo userInfo) throws Exception;
}
