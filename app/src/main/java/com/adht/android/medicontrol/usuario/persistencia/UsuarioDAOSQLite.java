package com.adht.android.medicontrol.usuario.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.infra.MediControlException;
import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;
import com.adht.android.medicontrol.usuario.dominio.Sexo;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

import java.util.GregorianCalendar;

public class UsuarioDAOSQLite extends AbstractSQLite implements IUsuarioDAO{

    public Usuario getUsuario(String email) throws MediControlException {
        Usuario result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_USUARIO+ " U WHERE U." + DBHelper.CAMPO_EMAIL + " LIKE ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{email});
        if (cursor.moveToFirst()) {
            result = createUsuario(cursor);
        }
        super.close(cursor, db);
        return result;
    }

    public Usuario getUsuario(String email, String password) throws MediControlException {
        Usuario result = getUsuario(email);
        if (result != null && !password.equals(result.getSenha())) {
            result = null;
        }
        return result;
    }

    public void cadastrar(Usuario usuario) throws MediControlException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.CAMPO_EMAIL, usuario.getEmail());
        values.put(DBHelper.CAMPO_PASSWORD, usuario.getSenha());
        values.put(DBHelper.CAMPO_NOME, usuario.getNome());
        values.put(DBHelper.CAMPO_NASCIMENTO, usuario.getNascimento().getTimeInMillis());
        values.put(DBHelper.CAMPO_SEXO, usuario.getSexo().getValor());
        db.insert(DBHelper.TABELA_USUARIO, null, values);
        super.close(db);
    }

    private Usuario createUsuario(Cursor cursor) throws MediControlException {
        Usuario result = new Usuario();
        result.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.CAMPO_ID)));
        result.setSenha(cursor.getString(cursor.getColumnIndex(DBHelper.CAMPO_PASSWORD)));
        GregorianCalendar nascimento = new GregorianCalendar();
        nascimento.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DBHelper.CAMPO_NASCIMENTO)));
        result.setNascimento(nascimento);
        result.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.CAMPO_EMAIL)));
        result.setSexo(Sexo.instanciaValor(cursor.getInt(cursor.getColumnIndex(DBHelper.CAMPO_SEXO))));
        result.setNome(cursor.getString(cursor.getColumnIndex(DBHelper.CAMPO_NOME)));
        return result;
    }
}
