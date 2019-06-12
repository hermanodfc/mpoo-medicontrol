package com.adht.android.medicontrol.alarme.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlarmeDAOSQLite extends AbstractSQLite {
    public Alarme getAlarme(long idAlarme) throws MediControlException, IOException {
        Alarme result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABELA_ALARME + " U WHERE U." + DBHelper.TABELA_ALARME_CAMPO_ID + " = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Long.toString(idAlarme)});
        if (cursor.moveToFirst()) {
            result = createAlarme(cursor);
        }
        super.close(cursor, db);
        return result;
    }

    public void cadastrar(Alarme alarme, long idPaciente) throws IOException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_ALARME_CAMPO_NOME_MEDICAMENTO, alarme.getNomeMedicamento());
        values.put(DBHelper.TABELA_ALARME_CAMPO_COMPLEMENTO, alarme.getComplemento());
        values.put(DBHelper.TABELA_ALARME_CAMPO_HORARIO_INICIO, alarme.getHorarioInicial());
        values.put(DBHelper.TABELA_ALARME_CAMPO_FREQUENCIA_HORAS, alarme.getFrequenciaHoras());
        values.put(DBHelper.TABELA_ALARME_CAMPO_DURACAO_DIAS, alarme.getDuracaoDias());
        values.put(DBHelper.TABELA_ALARME_CAMPO_ID_PACIENTE, idPaciente);
        db.insert(DBHelper.TABELA_ALARME, null, values);
        super.close(db);
    }

    private Alarme createAlarme(Cursor cursor) throws MediControlException {
        Alarme result = new Alarme();
        result.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_ID)));
        result.setNomeMedicamento(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_NOME_MEDICAMENTO)));
        result.setHorarioInicial(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_HORARIO_INICIO)));
        result.setFrequenciaHoras(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_FREQUENCIA_HORAS)));
        result.setComplemento(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_COMPLEMENTO)));
        result.setDuracaoDias(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_DURACAO_DIAS)));
        return result;
    }


    public List<Alarme> listar(long idPaciente) throws MediControlException {
        List<Alarme> alarmes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABELA_ALARME + " U WHERE U." + DBHelper.TABELA_ALARME_CAMPO_ID_PACIENTE + " = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Long.toString(idPaciente)});
        while (cursor.moveToNext()) {
            alarmes.add(createAlarme(cursor));
        }
        cursor.close();
        return alarmes;
    }

    public void atualizar (Alarme alarme, long idAlarme) throws IOException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_ALARME_CAMPO_NOME_MEDICAMENTO, alarme.getNomeMedicamento());
        values.put(DBHelper.TABELA_ALARME_CAMPO_COMPLEMENTO, alarme.getComplemento());
        values.put(DBHelper.TABELA_ALARME_CAMPO_HORARIO_INICIO, alarme.getHorarioInicial());
        values.put(DBHelper.TABELA_ALARME_CAMPO_FREQUENCIA_HORAS, alarme.getFrequenciaHoras());
        values.put(DBHelper.TABELA_ALARME_CAMPO_DURACAO_DIAS, alarme.getDuracaoDias());
        db.update(DBHelper.TABELA_ALARME, values, DBHelper.TABELA_ALARME_CAMPO_ID + "=" + idAlarme, null);
        super.close(db);
    }

    public void deletar(long idAlarme){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBHelper.TABELA_ALARME, DBHelper.TABELA_ALARME_CAMPO_ID + "=" + idAlarme, null);
    }
}
