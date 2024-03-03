package com.thanhtd.zola.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class UserProfile implements Serializable {
    private static final Long serialVersionUID = 1L;

    private Long userId;

    private String firstName;

    private String lastName;

    private String displayName;

    private String phone;

    private String email;

    private Timestamp birthday;

    private String address;

    private String avatar;

    private String gender;

    private String roleNames;

}
