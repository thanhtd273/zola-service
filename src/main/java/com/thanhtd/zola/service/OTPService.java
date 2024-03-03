package com.thanhtd.zola.service;

import com.thanhtd.zola.core.common.ErrorCode;

import java.sql.Timestamp;

public interface OTPService {
    ErrorCode sendOTP(String email, String subject);

    ErrorCode verifyOTP(String OTP, Timestamp timestamp);
}
