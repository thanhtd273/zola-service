package com.thanhtd.zola.dto;

import lombok.Data;

@Data
public class SMSInfo {
    private String sourcePhone;

    private String destinationPhone;

    private String message;
}
