package com.adht.android.medicontrol.paciente.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;
import com.adht.android.medicontrol.paciente.dominio.Amizade;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.paciente.dominio.StatusAmizade;

public class AmizadeDAOSQLite extends AbstractSQLite implements IAmizadeDAO {
    @Override
    public Amizade getAmizade(Paciente paciente, Paciente amigo) throws MediControlException {
        Amizade result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_AMIZADE+ " U WHERE U." +
                DBHelper.TABELA_AMIZADE_CAMPO_ID_PACIENTE + " = ? AND U." +
                DBHelper.TABELA_AMIZADE_CAMPO_ID_AMIGO +" = ?;";

        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(paciente.getId()),
                                                      Integer.toString(amigo.getId())});
        if (cursor.moveToFirst()) {
            result = createAmizade(cursor);
        }
        super.close(cursor, db);
        return result;
    }

    @Override
    public void cadastrarPedidoAmizade(Paciente paciente, Amizade amizade) throws MediControlException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_ID_PACIENTE, paciente.getId());
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_ID_AMIGO, amizade.getAmigo().getId());
        values.put(DBHelper.TABELA_AMIZADE_CAMPO_STATUS_AMIZADE, amizade.getStatusAmizade().getValor());
        db.insert(DBHelper.TABELA_AMIZADE, null, values);
        super.close(db);
    }

    private Amizade createAmizade(Cursor cursor) throws MediControlException {
        Amizade result = new Amizade();
        result.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_AMIZADE_CAMPO_ID)));
        result.setStatusAmizade(StatusAmizade.instanciaValor(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_AMIZADE_CAMPO_STATUS_AMIZADE))));
        PacienteDAOSQLite pacienteDAOSQLite = new PacienteDAOSQLite();
        result.setAmigo(pacienteDAOSQLite.getPacienteById(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_AMIZADE_CAMPO_ID_AMIGO))));
        return result;
    }
}

