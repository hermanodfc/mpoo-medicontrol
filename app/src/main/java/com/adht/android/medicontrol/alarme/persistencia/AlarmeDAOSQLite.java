package com.adht.android.medicontrol.alarme.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.infra.MediControlException;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

public class AlarmeDAOSQLite extends AbstractSQLite implements IAlarmeDao {

    //pegando o usuario
    Usuario usuario = Sessao.instance.getUsuario();

    public Alarme getAlarme(String nome) throws MediControlException {
        Alarme result = null;
/*        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABELA_ALARME+ " U WHERE U." + DBHelper.CAMPO_NOME_ALARME + " LIKE ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{nome});
        if (cursor.moveToFirst()) {
            result = createAlarme(cursor);
        }
        super.close(cursor, db);*/
        return result;
    }

    public void cadastrar(Alarme alarme) throws MediControlException {
/*        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CAMPO_NOME_ALARME, alarme.getNome());
        values.put(DBHelper.CAMPO_COMPLEMENTO, alarme.getComplemento());
        values.put(DBHelper.CAMPO_INICIO, alarme.getInicio());
        values.put(DBHelper.CAMPO_FREQUENCIA, alarme.getFrequencia());
        values.put(DBHelper.CAMPO_DIAS, alarme.getDias());
        values.put(DBHelper.CAMPO_ID_USUARIO, usuario.getId());
        db.insert(DBHelper.TABELA_ALARME, null, values);
        super.close(db);*/
    }

    private Alarme createAlarme(Cursor cursor) throws MediControlException {
        Alarme result = new Alarme();
/*        result.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.CAMPO_ID_ALARME)));
        result.setNome(cursor.getString(cursor.getColumnIndex(DBHelper.CAMPO_NOME_ALARME)));
        result.setInicio(cursor.getString(cursor.getColumnIndex(DBHelper.CAMPO_INICIO)));
        result.setFrequencia(cursor.getString(cursor.getColumnIndex(DBHelper.CAMPO_FREQUENCIA)));
        result.setComplemento(cursor.getString(cursor.getColumnIndex(DBHelper.CAMPO_COMPLEMENTO)));
        result.setDias(cursor.getString(cursor.getColumnIndex(DBHelper.CAMPO_DIAS)));
        result.setId_usuario(cursor.getInt(cursor.getColumnIndex(DBHelper.CAMPO_ID_USUARIO)));*/
        return result;
    }


}
