package com.thanhtd.zola.core.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    OK(200, "Success"),
    AUTH_SUCCESS(201, "Authentication pass"),

    VALID_OTP(202, "Valid OTP"),

    FAIL(404, "Fail"),

    DUPLICATE_ERROR(420, "Duplicate error"),

    TOKEN_INVALID(402, "Invalid token"),

    TOKEN_VALID(421, "Valid token"),

    INTERNAL_SERVER_ERROR(500, "Internal server error"),

    BLANK_FIELD(422, "Field input is blank"),

    DATA_NULL(423, "Data is null"),

    DELETED_DATA(423, "Data is deleted"),

    ID_NULL(424, "Id is null"),

    LOGIN_FAIL(401, "You are not logged in"),

    SIGNUP_FAIL(402, "Sign up fail"),

    INVALID_CREDENTIAL(401, "Invalid credential"),

    INVALID_OTP(425, "Invalid OTP"),


    ;
    private final int value;
    private final String message;


    private ErrorCode(int value, String message) {
        this.value = value;
        this.message = message;
    }

}
