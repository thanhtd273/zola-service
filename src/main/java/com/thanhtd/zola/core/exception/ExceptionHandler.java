package com.thanhtd.zola.core.exception;

import com.thanhtd.zola.core.APIResponse;
import lombok.extern.java.Log;

public class ExceptionHandler {
    private ExceptionHandler() {
        throw new IllegalStateException("Static class");
    }

    public static APIResponse handleException(Exception e, long start) {
        if (e.getClass() == LogicException.class) {
            LogicException specEx = (LogicException) e;
            return new APIResponse(specEx.getErrorCode(), specEx.getMessage(), System.currentTimeMillis() - start, null);
        }
        return new APIResponse(500, "error", e.getMessage(), System.currentTimeMillis() - start, null );
    }
}
