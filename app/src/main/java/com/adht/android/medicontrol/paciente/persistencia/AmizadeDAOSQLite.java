package com.adht.android.medicontrol.paciente.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;
import com.adht.android.medicontrol.paciente.dominio.Amizade;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.paciente.dominio.StatusAmizade;

import java.io.IOException;
import java.util.ArrayList;

public class AmizadeDAOSQLite extends AbstractSQLite {

    public Amizade getAmizade(Paciente solicitante, Paciente amigo) throws IOException {
        Amizade result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_AMIZADE+ " U WHERE (U." +
                DBHelper.TABELA_AMIZADE_CAMPO_ID_SOLICITANTE + " = ? AND U." +
                DBHelper.TABELA_AMIZADE_CAMPO_ID_CONVIDADO + " = ?) OR (U." +
                DBHelper.TABELA_AMIZADE_CAMPO_ID_CONVIDADO + " = ? AND U." +
                DBHelper.TABELA_AMIZADE_CAMPO_ID_SOLICITANTE + " = ?);";

        Cursor cursor = db.rawQuery(sql, new String[]{Long.toString(solicitante.getId()),
                Long.toString(amigo.getId()),
                Long.toString(solicitante.getId()),
                Long.toString(amigo.getId())});
        if (cursor.moveToFirst()) {
            result = createAmizade(cursor);
        }
        super.close(cursor, db);
        return result;
    }

    public void cadastrarPedidoAmizade(Amizade amizade) throws IOException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_ID_SOLICITANTE, amizade.getSolicitante().getId());
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_ID_CONVIDADO, amizade.getConvidado().getId());
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_STATUS_AMIZADE, amizade.getStatus().ordinal());
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_CUIDADOR, amizade.getIdCuidador());
        db.insert(DBHelper.TABELA_AMIZADE, null, values);
        super.close(db);
    }

    private Amizade createAmizade(Cursor cursor) throws IOException {
        Amizade result = new Amizade();
        result.setId(cursor.getLong(cursor.getColumnIndex(
                DBHelper.TABELA_AMIZADE_CAMPO_ID)));
        result.setStatusAmizade(StatusAmizade.values()[cursor.getInt(
                cursor.getColumnIndex(DBHelper.TABELA_AMIZADE_CAMPO_STATUS_AMIZADE))]);
        result.setIdCuidador(cursor.getLong(cursor.getColumnIndex(DBHelper.TABELA_AMIZADE_CAMPO_CUIDADOR)));
        PacienteDAOSQLite pacienteDAOSQLite = new PacienteDAOSQLite();
        result.setSolicitante(pacienteDAOSQLite.getPacienteById(cursor.getLong(
                cursor.getColumnIndex(DBHelper.TABELA_AMIZADE_CAMPO_ID_SOLICITANTE))));
        result.setConvidado(pacienteDAOSQLite.getPacienteById(cursor.getLong(
                cursor.getColumnIndex(DBHelper.TABELA_AMIZADE_CAMPO_ID_CONVIDADO))));
        return result;
    }

    public ArrayList<Amizade> getAmizades(Paciente paciente) throws IOException {
        ArrayList<Amizade> result = new ArrayList<>();

        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_AMIZADE+ " U WHERE U." +
                DBHelper.TABELA_AMIZADE_CAMPO_ID_SOLICITANTE + " = ? " +
                "OR U." + DBHelper.TABELA_AMIZADE_CAMPO_ID_CONVIDADO + " = ?;";

        Cursor cursor = db.rawQuery(sql, new String[]{Long.toString(paciente.getId()),
                Long.toString(paciente.getId())});
        while(cursor.moveToNext()){
            result.add(createAmizade(cursor));
        }

        super.close(cursor, db);
        return result;
    }

    public void desfazerAmizade(Amizade amizade) throws IOException {
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "DELETE FROM " + DBHelper.TABELA_AMIZADE + " WHERE " +
                DBHelper.TABELA_AMIZADE_CAMPO_ID + " = ?;";
        db.execSQL(sql, new String[] {Long.toString(amizade.getId())});
        super.close(db);
    }

    public void atualizar(Amizade amizade) {
        SQLiteDatabase db = super.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_ID_SOLICITANTE, amizade.getSolicitante().getId());
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_ID_CONVIDADO, amizade.getConvidado().getId());
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_STATUS_AMIZADE, amizade.getStatus().ordinal());
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_CUIDADOR, amizade.getIdCuidador());
        db.update(DBHelper.TABELA_AMIZADE, values, DBHelper.TABELA_AMIZADE_CAMPO_ID + " = ?",
                new String[] {Long.toString(amizade.getId())});
    }
}

