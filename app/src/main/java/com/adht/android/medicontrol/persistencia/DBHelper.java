package com.adht.android.medicontrol.persistencia;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adht.android.medicontrol.infra.ui.MediControlApp;

public class DBHelper extends SQLiteOpenHelper {

    //Informações do banco
    private static final String NOME_DB = "medicontrol.db";
    private static final int VERSAO = 1;

    //Informações dos usuários
    public static final String CAMPO_ID = "ID";
    public static final String TABELA_USUARIO = "TABELA";
    public static final String CAMPO_NOME = "NOME";
    public static final String CAMPO_GENERO = "GENERO";

    //public static final Date CAMPO_NASCIMENTO = qual o tipo aqui?;
    public static final String CAMPO_EMAIL = "EMAIL";
    public static final String CAMPO_SENHA = "SENHA";

    private static final String[] TABELAS = {
            TABELA_USUARIO
    };

    //Construtor
    public DBHelper() {
        super(MediControlApp.getContext(), NOME_DB, null, VERSAO);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        createTabelaUsuario(db);

    }

    public void createTabelaUsuario(SQLiteDatabase db){
        String sqlTabela =
                "CREATE TABLE %1$s ( " +
                " %2$s INTENGER PRIMARY KEY AUTOINCREMENT, " +
                " %3$s TEXT NOT NULL, " +
                " %4$s TEXT NOT NULL, " +
                " %5$s TEXT NOT NULL UNIQUE, " +
                " %6$s TEXT NOT NULL " +
                ") ;";
        sqlTabela = String.format(sqlTabela, TABELA_USUARIO, CAMPO_ID, CAMPO_NOME, CAMPO_GENERO, CAMPO_EMAIL, CAMPO_SENHA);
        db.execSQL(sqlTabela);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);

    }

    private void dropTables(SQLiteDatabase db){
        StringBuilder dropTables = new StringBuilder();
        for (String tabela : TABELAS) {
            dropTables.append(" DROP TABLE IF EXISTS ");
            dropTables.append(tabela);
            dropTables.append("; ");
        }
        db.execSQL(dropTables.toString());
    }
}
