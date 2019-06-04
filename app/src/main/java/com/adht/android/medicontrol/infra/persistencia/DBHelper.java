package com.adht.android.medicontrol.infra.persistencia;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adht.android.medicontrol.infra.ui.MediControlApp;

public class DBHelper extends SQLiteOpenHelper {
    private static final String NOME_DB = "medicontrol.db";
    private static final int VERSAO = 3;

    // TABELA DOS USUARIOS
    public static final String TABELA_USUARIO = "TB_USUARIO";
    public static final String TABELA_USUARIO_CAMPO_ID = "ID";
    public static final String TABELA_USUARIO_CAMPO_EMAIL = "EMAIL";
    public static final String TABELA_USUARIO_CAMPO_PASSWORD = "PASSWORD";


    // TABELA DOS PACIENTES
    public static final String TABELA_PACIENTE = "TB_PACIENTE";
    public static final String TABELA_PACIENTE_CAMPO_ID = "ID";
    public static final String TABELA_PACIENTE_CAMPO_NOME = "NOME";
    public static final String TABELA_PACIENTE_CAMPO_NASCIMENTO = "NASCIMENTO";
    public static final String TABELA_PACIENTE_CAMPO_GENERO = "SEXO";
    public static final String TABELA_PACIENTE_CAMPO_ID_USUARIO = "ID_USUARIO";

//    // TABELA DOS ALARMES
//
//    public static final String TABELA_ALARME = "TB_ALARME";
//    public static final String CAMPO_NOME_ALARME = "NOME_ALARME";
//    public static final String CAMPO_ID_ALARME = "ID_ALARME";
//    public static final String CAMPO_FREQUENCIA = "FREQUENCIA";
//    public static final String CAMPO_INICIO = "INICIO";
//    public static final String CAMPO_COMPLEMENTO = "COMPLEMENTO";
//    public static final String CAMPO_DIAS = "DIAS";
//    public static final String CAMPO_ID_PACIENTE = "ID_USUARIO";


    private static final String[] TABELAS = {
            TABELA_USUARIO, TABELA_PACIENTE
    };

    DBHelper() {
        super(MediControlApp.getContext(), NOME_DB, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTabelaUsuario(db);
        createTabelaPaciente(db);
    }

    private void createTabelaUsuario(SQLiteDatabase db) {
        String sqlTbUsuario =
                "CREATE TABLE " + TABELA_USUARIO + " ( " +
                        TABELA_USUARIO_CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TABELA_USUARIO_CAMPO_EMAIL + " TEXT NOT NULL UNIQUE, " +
                        TABELA_USUARIO_CAMPO_PASSWORD + " TEXT NOT NULL" +
                        ");";

        db.execSQL(sqlTbUsuario);
    }

    private void createTabelaPaciente(SQLiteDatabase db) {
        String sqlTbPaciente =
                "CREATE TABLE " + TABELA_PACIENTE + " ( " +
                        TABELA_PACIENTE_CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TABELA_PACIENTE_CAMPO_NOME + " TEXT NOT NULL, " +
                        TABELA_PACIENTE_CAMPO_NASCIMENTO + " REAL NOT NULL, " +
                        TABELA_PACIENTE_CAMPO_GENERO + " INTEGER NOT NULL, " +
                        TABELA_PACIENTE_CAMPO_ID_USUARIO + " INTEGER NOT NULL UNIQUE, " +
                        "FOREIGN KEY(" + TABELA_PACIENTE_CAMPO_ID_USUARIO + ") " +
                            "REFERENCES " + TABELA_USUARIO + "(" + TABELA_USUARIO_CAMPO_ID + ") " +
                ");";

        db.execSQL(sqlTbPaciente);
    }

//    private void createTabelaAlarme(SQLiteDatabase db) {
//        String sqlTbAlarme =
//                "CREATE TABLE %1$s ( " +
//                        "  %2$s INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                        "  %3$s TEXT NOT NULL UNIQUE, " +
//                        "  %4$s TEXT, " +
//                        "  %5$s INTEGER NOT NULL, " +
//                        "  %6$s INTEGER NOT NULL," +
//                        "  %7$s INTEGER NOT NULL," +
//                        "  %8$s INTEGER, " +
//                        "  FOREIGN KEY(%8$s) REFERENCES %9$s(%10$s)" +
//                        ");";
//        sqlTbAlarme = String.format(sqlTbAlarme,
//                TABELA_ALARME, CAMPO_ID_ALARME, CAMPO_NOME_ALARME, CAMPO_COMPLEMENTO, CAMPO_INICIO,
//                CAMPO_FREQUENCIA, CAMPO_DIAS, CAMPO_ID_USUARIO, TABELA_USUARIO, CAMPO_ID);
//        db.execSQL(sqlTbAlarme);
//    }

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
