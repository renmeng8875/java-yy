package com.yy.ent.platform.core.exception;


public class BaseException extends RuntimeException {
    public static final int CODE_UN_KNOWN=1000;
    public static final int CODE_NO_LOGIN=1001;
    public static final int CODE_RPC_ERROR=1002;
    private int errorCode;

    public BaseException() {
        super();
    }

    public BaseException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public BaseException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "BaseException{" +
                "errorCode=" + errorCode +
                ",message=" + getMessage() +
                '}';
    }
}
