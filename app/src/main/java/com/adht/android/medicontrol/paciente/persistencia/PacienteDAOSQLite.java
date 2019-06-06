package com.adht.android.medicontrol.paciente.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;
import com.adht.android.medicontrol.paciente.dominio.Genero;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;

import java.util.GregorianCalendar;

public class PacienteDAOSQLite extends AbstractSQLite implements IPacienteDAO {

    @Override
    public void cadastrar(Paciente paciente, int idUsuario) throws MediControlException {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_PACIENTE_CAMPO_NOME, paciente.getNome());
        values.put(DBHelper.TABELA_PACIENTE_CAMPO_GENERO, paciente.getGenero().getValor());
        values.put(DBHelper.TABELA_PACIENTE_CAMPO_NASCIMENTO, paciente.getNascimento().getTimeInMillis());
        values.put(DBHelper.TABELA_PACIENTE_CAMPO_ID_USUARIO, idUsuario);
        db.insert(DBHelper.TABELA_PACIENTE, null, values);
        super.close(db);
    }

    public Paciente getPaciente(int idUsuario) throws MediControlException {
        Paciente result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_PACIENTE+ " U WHERE U." +
                DBHelper.TABELA_PACIENTE_CAMPO_ID_USUARIO + " = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(idUsuario)});
        if (cursor.moveToFirst()) {
            result = createPaciente(cursor);
        }
        super.close(cursor, db);
        return result;
    }

    public Paciente getPacienteById(int idPaciente) throws MediControlException {
        Paciente result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_PACIENTE+ " U WHERE U." +
                DBHelper.TABELA_PACIENTE_CAMPO_ID + " = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(idPaciente)});
        if (cursor.moveToFirst()) {
            result = createPaciente(cursor);
        }
        super.close(cursor, db);
        return result;
    }


    private Paciente createPaciente(Cursor cursor) throws MediControlException {
        Paciente result = new Paciente();
        result.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_PACIENTE_CAMPO_ID)));
        result.setNome(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_PACIENTE_CAMPO_NOME)));
        GregorianCalendar nascimento = new GregorianCalendar();
        nascimento.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DBHelper.TABELA_PACIENTE_CAMPO_NASCIMENTO)));
        result.setNascimento(nascimento);
        result.setGenero(Genero.instanciaValor(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_PACIENTE_CAMPO_GENERO))));
        return result;
    }
}
