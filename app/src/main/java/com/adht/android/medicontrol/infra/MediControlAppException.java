package com.adht.android.medicontrol.infra;

public class MediControlAppException extends Exception {
    public MediControlAppException(String msg){
        super(msg);
    }
    public MediControlAppException(String msg, Throwable causa){
        super(msg);
        //Não é usada no código de mpoo

    }
}
