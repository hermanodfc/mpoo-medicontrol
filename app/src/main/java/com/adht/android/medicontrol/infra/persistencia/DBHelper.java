package com.adht.android.medicontrol.infra.persistencia;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adht.android.medicontrol.infra.ui.MediControlApp;

public class DBHelper extends SQLiteOpenHelper {
    private static final String NOME_DB = "mpooapp.db";
    private static final int VERSAO = 1;

    // TABELA DOS USUARIOS
    public static final String TABELA_USUARIO = "TB_USUARIO";
    public static final String CAMPO_ID = "ID";
    public static final String CAMPO_EMAIL = "EMAIL";
    public static final String CAMPO_PASSWORD = "PASSWORD";
    public static final String CAMPO_NOME = "NOME";
    public static final String CAMPO_NASCIMENTO = "NASCIMENTO";
    public static final String CAMPO_SEXO = "SEXO";

    private static final String[] TABELAS = {
            TABELA_USUARIO
    };

    DBHelper(){
        super(MediControlApp.getContext(), NOME_DB,null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTabelaUsuario(db);
    }

    private void createTabelaUsuario(SQLiteDatabase db) {
        String sqlTbUsuario =
                "CREATE TABLE %1$s ( "  +
                        "  %2$s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "  %3$s TEXT NOT NULL UNIQUE, " +
                        "  %4$s TEXT NOT NULL, " +
                        "  %5$s TEXT NOT NULL, " +
                        "  %6$s INTEGER NOT NULL, " +
                        "  %7$s INTEGER NOT NULL" +
                        ");";
        sqlTbUsuario = String.format(sqlTbUsuario,
                TABELA_USUARIO, CAMPO_ID, CAMPO_EMAIL, CAMPO_PASSWORD, CAMPO_NOME,
                CAMPO_NASCIMENTO, CAMPO_SEXO);
        db.execSQL(sqlTbUsuario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    }

    private void dropTables(SQLiteDatabase db) {
        StringBuilder dropTables = new StringBuilder();
        for (String tabela : TABELAS) {
            dropTables.append(" DROP TABLE IF EXISTS ");
            dropTables.append(tabela);
            dropTables.append("; ");
        }
        db.execSQL(dropTables.toString());
    }
}

