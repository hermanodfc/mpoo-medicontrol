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

    // TABELA DOS ALARMES

    public static final String TABELA_ALARME = "TB_ALARME";
    public static final String CAMPO_NOME_ALARME = "NOME_ALARME";
    public static final String CAMPO_ID_ALARME = "ID_ALARME";
    public static final String CAMPO_FREQUENCIA = "FREQUENCIA";
    public static final String CAMPO_INICIO = "INICIO";
    public static final String CAMPO_COMPLEMENTO = "COMPLEMENTO";
    public static final String CAMPO_DIAS = "DIAS";
    public static final String CAMPO_ID_USUARIO = "ID_USUARIO";

    public static final String[] TABELAS_DE_ALARMES = {
            TABELA_ALARME
    };

    DBHelper(){
        super(MediControlApp.getContext(), NOME_DB,null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTabelaUsuario(db);
        createTabelaAlarme(db);
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

/*        String sqlTbAlarme =
                "CREATE TABLE %1$s ( "  +
                        "  %2$s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "  %3$s TEXT NOT NULL UNIQUE, " +
                        "  %4$s TEXT, " +
                        "  %5$s INTEGER NOT NULL, " +
                        "  %6$s INTEGER NOT NULL," +
                        "  %7$s INTEGER NOT NULL," +
                        "  %8$s INTEGER, " +
                        "  FOREIGN KEY(%8$s) REFERENCES %9$s(%10$s)" +
                        ");";
        sqlTbAlarme = String.format(sqlTbAlarme,
                TABELA_ALARME, CAMPO_ID_ALARME, CAMPO_NOME_ALARME, CAMPO_COMPLEMENTO, CAMPO_INICIO,
                CAMPO_FREQUENCIA, CAMPO_DIAS, CAMPO_ID_USUARIO, TABELA_USUARIO, CAMPO_ID);*/



        db.execSQL(sqlTbUsuario);
        //db.execSQL(sqlTbAlarme);
    }

    private void createTabelaAlarme(SQLiteDatabase db){
        String sqlTbAlarme =
                "CREATE TABLE %1$s ( "  +
                        "  %2$s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "  %3$s TEXT NOT NULL UNIQUE, " +
                        "  %4$s TEXT, " +
                        "  %5$s INTEGER NOT NULL, " +
                        "  %6$s INTEGER NOT NULL," +
                        "  %7$s INTEGER NOT NULL," +
                        "  %8$s INTEGER, " +
                        "  FOREIGN KEY(%8$s) REFERENCES %9$s(%10$s)" +
                        ");";
        sqlTbAlarme = String.format(sqlTbAlarme,
                TABELA_ALARME, CAMPO_ID_ALARME, CAMPO_NOME_ALARME, CAMPO_COMPLEMENTO, CAMPO_INICIO,
                CAMPO_FREQUENCIA, CAMPO_DIAS, CAMPO_ID_USUARIO, TABELA_USUARIO, CAMPO_ID);
        db.execSQL(sqlTbAlarme);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        dropTableAlarme(db);
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

    private void dropTableAlarme(SQLiteDatabase db) {
        StringBuilder dropTableAlarme = new StringBuilder();
        for (String tabela: TABELAS_DE_ALARMES){
            dropTableAlarme.append(" DROP TABLE IF EXISTS ");
            dropTableAlarme.append(tabela);
            dropTableAlarme.append("; ");
        }
        db.execSQL(dropTableAlarme.toString());
        }
    }


