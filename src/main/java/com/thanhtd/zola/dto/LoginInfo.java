package com.thanhtd.zola.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginInfo implements Serializable {
    private static final Long serialVersionUID = 1L;

    private String password;

    private String confirmPassword;

    private String phone;
}
