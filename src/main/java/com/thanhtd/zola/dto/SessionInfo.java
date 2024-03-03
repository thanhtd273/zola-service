package com.thanhtd.zola.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SessionInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String token;

    @NotEmpty
    private UserProfile userProfile;

    private int expireIn;
}
