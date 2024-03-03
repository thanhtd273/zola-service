package com.thanhtd.zola.core.exception;

import com.thanhtd.zola.core.common.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogicException extends GlobalException {
    private ErrorCode errorCode;
    private String addingInfo;

    public LogicException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    public LogicException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public LogicException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
