package com.adht.android.medicontrol.infra.persistencia;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adht.android.medicontrol.infra.ui.MediControlApp;

public class DBHelper extends SQLiteOpenHelper {
    private static final String NOME_DB = "medicontrol.db";
    private static final int VERSAO = 4;

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

    // TABELA DOS ALARMES
    public static final String TABELA_ALARME = "TB_ALARME";
    public static final String TABELA_ALARME_CAMPO_ID = "ID_ALARME";
    public static final String TABELA_ALARME_CAMPO_NOME_MEDICAMENTO = "NOME_MEDICAMENTO";
    public static final String TABELA_ALARME_CAMPO_HORARIO_INICIO = "INICIO";
    public static final String TABELA_ALARME_CAMPO_COMPLEMENTO = "COMPLEMENTO";
    public static final String TABELA_ALARME_CAMPO_FREQUENCIA_HORAS = "FREQUENCIA";
    public static final String TABELA_ALARME_CAMPO_DURACAO_DIAS = "DIAS";
    public static final String TABELA_ALARME_CAMPO_ID_PACIENTE = "ID_USUARIO";

    // TABELA DAS AMIZADES
    public static final String TABELA_AMIZADE = "TB_AMIZADE";
    public static final String TABELA_AMIZADE_CAMPO_ID = "ID";
    public static final String TABELA_AMIZADE_CAMPO_ID_PACIENTE = "ID_PACIENTE";
    public static final String TABELA_AMIZADE_CAMPO_ID_AMIGO = "ID_AMIGO";
    public static final String TABELA_AMIZADE_CAMPO_STATUS_AMIZADE = "STATUS_AMIZADE";

    private static final String[] TABELAS = {
            TABELA_USUARIO, TABELA_PACIENTE, TABELA_ALARME, TABELA_AMIZADE
    };

    DBHelper() {
        super(MediControlApp.getContext(), NOME_DB, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTabelaUsuario(db);
        createTabelaPaciente(db);
        createTabelaAlarme(db);
        createTabelaAmizade(db);
    }

    private void createTabelaAmizade(SQLiteDatabase db) {
        String sqlTbAmizade =
                "CREATE TABLE " + TABELA_AMIZADE + " (" +
                        TABELA_AMIZADE_CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TABELA_AMIZADE_CAMPO_ID_PACIENTE + " INTEGER NOT NULL, " +
                        TABELA_AMIZADE_CAMPO_ID_AMIGO + " INTEGER NOT NULL, " +
                        TABELA_AMIZADE_CAMPO_STATUS_AMIZADE + " INTEGER NOT NULL, " +
                        "FOREIGN KEY(" + TABELA_AMIZADE_CAMPO_ID_PACIENTE + ") " +
                            "REFERENCES " + TABELA_PACIENTE + "(" +
                                TABELA_PACIENTE_CAMPO_ID_USUARIO + "), " +
                        "FOREIGN KEY(" + TABELA_AMIZADE_CAMPO_ID_PACIENTE + ") " +
                        "REFERENCES " + TABELA_PACIENTE + "(" +
                        TABELA_AMIZADE_CAMPO_ID_AMIGO + ")" +
                ");";
        db.execSQL(sqlTbAmizade);
    }
    private void createTabelaUsuario(SQLiteDatabase db) {
        String sqlTbUsuario =
                "CREATE TABLE " + TABELA_USUARIO + " (" +
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

    private void createTabelaAlarme(SQLiteDatabase db) {
        String sqlTbAlarme =
                "CREATE TABLE " + TABELA_ALARME + "(" +
                        TABELA_ALARME_CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TABELA_ALARME_CAMPO_NOME_MEDICAMENTO + " TEXT NOT NULL, " +
                        TABELA_ALARME_CAMPO_HORARIO_INICIO + " REAL NOT NULL, " +
                        TABELA_ALARME_CAMPO_COMPLEMENTO + " TEXT NOT NULL, " +
                        TABELA_ALARME_CAMPO_FREQUENCIA_HORAS + " INTEGER NOT NULL," +
                        TABELA_ALARME_CAMPO_DURACAO_DIAS + " INTEGER NOT NULL," +
                        TABELA_ALARME_CAMPO_ID_PACIENTE  + " INTEGER NOT NULL, " +
                        "FOREIGN KEY(" + TABELA_ALARME_CAMPO_ID_PACIENTE + ")" +
                            "REFERENCES " + TABELA_USUARIO + "(" + TABELA_USUARIO_CAMPO_ID + ")" +
                        ");";
        db.execSQL(sqlTbAlarme);
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
