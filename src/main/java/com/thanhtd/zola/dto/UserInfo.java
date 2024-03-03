package com.thanhtd.zola.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class UserInfo implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Long userId;

    private String firstName;

    private String lastName;

    private String displayName;

    private String password;

    private String confirmPassword;

    private String phone;

    private String email;

    private Timestamp birthday;

    private String address;

    private String avatar;

    private Integer active;

    private Timestamp createDate;

    private Timestamp modifiedDate;

    private Integer gender;

    private List<String> roles;

}
