package com.adht.android.medicontrol.alarme.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;

<<<<<<< HEAD
=======
import java.io.IOException;
>>>>>>> versao-0.2
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmeDAOSQLite extends AbstractSQLite {
    public Alarme getAlarmes(int idPaciente) throws MediControlException, IOException {
        Alarme result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABELA_ALARME + " U WHERE U." + DBHelper.TABELA_ALARME_CAMPO_ID_PACIENTE + " = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(idPaciente)});
        if (cursor.moveToFirst()) {
            result = createAlarme(cursor);
        }
        super.close(cursor, db);
        return result;
    }

    public void cadastrar(Alarme alarme, int idPaciente) throws MediControlException, IOException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_ALARME_CAMPO_NOME_MEDICAMENTO, alarme.getNomeMedicamento());
        values.put(DBHelper.TABELA_ALARME_CAMPO_COMPLEMENTO, alarme.getComplemento());
        //values.put(DBHelper.TABELA_ALARME_CAMPO_HORARIO_INICIO, alarme.getHorarioInicial().getTime());
        values.put(DBHelper.TABELA_ALARME_CAMPO_FREQUENCIA_HORAS, alarme.getFrequenciaHoras());
        values.put(DBHelper.TABELA_ALARME_CAMPO_DURACAO_DIAS, alarme.getDuracaoDias());
        values.put(DBHelper.TABELA_ALARME_CAMPO_ID_PACIENTE, idPaciente);
        db.insert(DBHelper.TABELA_ALARME, null, values);
        super.close(db);
    }

    private Alarme createAlarme(Cursor cursor) throws MediControlException {
        Alarme result = new Alarme();
        result.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_ID)));
        result.setNomeMedicamento(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_NOME_MEDICAMENTO)));
        //Date date = new Date();
        //date.setTime((long) cursor.getDouble(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_HORARIO_INICIO)));
        //result.setHorarioInicial(date);
        result.setFrequenciaHoras(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_FREQUENCIA_HORAS)));
        result.setComplemento(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_COMPLEMENTO)));
        result.setDuracaoDias(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_ALARME_CAMPO_DURACAO_DIAS)));
        return result;
    }


    public List<Alarme> listar(int idPaciente) throws MediControlException {
        List<Alarme> alarmes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABELA_ALARME + " U WHERE U." + DBHelper.TABELA_ALARME_CAMPO_ID_PACIENTE + " = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(idPaciente)});
        while(cursor.moveToNext()){

            alarmes.add(createAlarme(cursor));

        }
        cursor.close();
        return  alarmes;



    }


<<<<<<< HEAD
}
=======
}
>>>>>>> versao-0.2
