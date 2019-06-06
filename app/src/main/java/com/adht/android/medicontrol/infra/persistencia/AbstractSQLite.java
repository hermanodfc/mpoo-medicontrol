package com.adht.android.medicontrol.infra.persistencia;

import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;

public abstract class AbstractSQLite {
    protected void close(Closeable... args) throws IOException {
        for (Closeable closable : args) {
            closable.close();
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
