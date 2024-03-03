package com.thanhtd.zola.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class OTPInfo implements Serializable {

    private String code;

    private Timestamp timestamp;
}
