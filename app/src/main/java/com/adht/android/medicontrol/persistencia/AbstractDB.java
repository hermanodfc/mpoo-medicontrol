package com.adht.android.medicontrol.persistencia;

import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.infra.MediControlAppRuntimeException;

import java.io.Closeable;
import java.io.IOException;

//Tá completamente igual ao de mpoo

public abstract class AbstractDB {

    protected void close(Closeable... args) throws MediControlAppRuntimeException{
        for (Closeable closable : args) {
            try{
                closable.close();
            } catch (IOException e) {
                throw new MediControlAppRuntimeException("Erro ao fechar as conexões", e);
            }
         }
    }

    protected SQLiteDatabase getReadableDatabase(){
        DBHelper dbHelper = new DBHelper();
            return dbHelper.getReadableDatabase();
    }

    protected SQLiteDatabase getWritableDatabase(){
        DBHelper dbHelper = new DBHelper();
            return dbHelper.getWritableDatabase();
    }
}
