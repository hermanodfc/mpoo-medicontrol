package com.adht.android.medicontrol.infra;

public class MediControlException extends Exception {
    public MediControlException(String msg) {
        super(msg);
    }
    public MediControlException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
