package com.adht.android.medicontrol.infra;

public class MediControlAppRuntimeException extends RuntimeException{
    public MediControlAppRuntimeException(String msg){
        super(msg);
        //Não é usado no código de mpoo
    }

    public MediControlAppRuntimeException(String msg, Throwable causa){
        super(msg, causa);
    }

}
