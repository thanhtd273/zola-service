package com.thanhtd.zola.core;

import com.thanhtd.zola.core.common.ErrorCode;
import lombok.Data;

@Data
public class APIResponse {
    private int codeStatus;
    private String messageStatus;
    private String description;
    private long took;
    private Object data;
    public APIResponse(Integer codeStatus, String messageStatus, String description, Long took, Object data) {
        this.codeStatus = codeStatus;
        this.messageStatus = messageStatus;
        this.description = description;
        this.took = took;
        this.data = data;
    }

    public APIResponse(ErrorCode errorCode, String description, long took, Object data) {
        this.codeStatus = errorCode.getValue();
        this.messageStatus = errorCode.getMessage();
        this.description = description;
        this.took = took;
        this.data = data;
    }
}
