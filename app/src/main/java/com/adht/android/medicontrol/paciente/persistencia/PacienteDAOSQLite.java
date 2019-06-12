package com.adht.android.medicontrol.paciente.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.infra.exception.PacienteNascimentoInvalidoException;
import com.adht.android.medicontrol.infra.exception.PacienteNomeInvalidoException;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;
import com.adht.android.medicontrol.infra.persistencia.PacienteGeneroInvalidoException;
import com.adht.android.medicontrol.paciente.dominio.Genero;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;

import java.io.IOException;
import java.util.GregorianCalendar;

public class PacienteDAOSQLite extends AbstractSQLite {

    public void cadastrar(Paciente paciente, long idUsuario) throws IOException {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_PACIENTE_CAMPO_NOME, paciente.getNome());
        values.put(DBHelper.TABELA_PACIENTE_CAMPO_GENERO, paciente.getGenero().ordinal());
        values.put(DBHelper.TABELA_PACIENTE_CAMPO_NASCIMENTO, paciente.getNascimento().getTimeInMillis());
        values.put(DBHelper.TABELA_PACIENTE_CAMPO_ID_USUARIO, idUsuario);
        db.insert(DBHelper.TABELA_PACIENTE, null, values);
        super.close(db);
    }

    public Paciente getPaciente(long idUsuario) throws IOException {
        Paciente result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_PACIENTE+ " U WHERE U." +
                DBHelper.TABELA_PACIENTE_CAMPO_ID_USUARIO + " = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Long.toString(idUsuario)});
        if (cursor.moveToFirst()) {
            result = createPaciente(cursor);
        }
        super.close(cursor, db);
        return result;
    }

    public Paciente getPacienteById(Long idPaciente) throws IOException {
        Paciente result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_PACIENTE+ " U WHERE U." +
                DBHelper.TABELA_PACIENTE_CAMPO_ID + " = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Long.toString(idPaciente)});
        if (cursor.moveToFirst()) {
            result = createPaciente(cursor);
        }
        super.close(cursor, db);
        return result;
    }


    private Paciente createPaciente(Cursor cursor)  {
        Paciente result = new Paciente();
        result.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_PACIENTE_CAMPO_ID)));
        try {
            result.setNome(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_PACIENTE_CAMPO_NOME)));
        } catch (PacienteNomeInvalidoException e) {
            e.printStackTrace();
        }
        GregorianCalendar nascimento = new GregorianCalendar();
        nascimento.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DBHelper.TABELA_PACIENTE_CAMPO_NASCIMENTO)));
        try {
            result.setNascimento(nascimento);
        } catch (PacienteNascimentoInvalidoException e) {
            e.printStackTrace();
        }
        try {
            result.setGenero(Genero.values()[cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_PACIENTE_CAMPO_GENERO))]);
        } catch (PacienteGeneroInvalidoException e) {
            e.printStackTrace();
        }
        return result;
    }
}
