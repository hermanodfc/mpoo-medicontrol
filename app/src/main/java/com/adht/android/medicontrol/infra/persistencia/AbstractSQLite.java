package com.adht.android.medicontrol.infra.persistencia;

import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.infra.MediControlException;

import java.io.Closeable;
import java.io.IOException;

public abstract class AbstractSQLite {
    protected void close(Closeable... args) throws MediControlException {
        for (Closeable closable : args) {
            try {
                closable.close();
            } catch (IOException e) {
                throw new MediControlException("Erro ao fechar as conexões",e);
            }
        }
    }

    protected SQLiteDatabase getReadableDatabase() {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getReadableDatabase();
    }

    protected SQLiteDatabase getWritableDatabase() {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getWritableDatabase();
    }
}
