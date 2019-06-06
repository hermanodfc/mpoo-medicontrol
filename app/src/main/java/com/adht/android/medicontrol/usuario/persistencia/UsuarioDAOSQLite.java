package com.adht.android.medicontrol.usuario.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

public class UsuarioDAOSQLite extends AbstractSQLite implements IUsuarioDAO{

    public Usuario getUsuario(String email) throws MediControlException {
        Usuario result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_USUARIO+ " U WHERE U." + DBHelper.TABELA_USUARIO_CAMPO_EMAIL + " LIKE ?;";
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

    private Usuario getUsuario(int id) throws MediControlException {
        Usuario result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " +DBHelper.TABELA_USUARIO+ " U WHERE U." + DBHelper.TABELA_USUARIO_CAMPO_ID + " = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(id)});
        if (cursor.moveToFirst()) {
            result = createUsuario(cursor);
        }
        super.close(cursor, db);
        return result;
    }

    public boolean isUsuarioCadastrado(int idUsuario) throws MediControlException {
        Usuario usuario = getUsuario(idUsuario);

        boolean result = true;

        if (usuario == null) {
            result = false;
        }

        return result;
    }

    public void cadastrar(Usuario usuario) throws MediControlException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_USUARIO_CAMPO_EMAIL, usuario.getEmail());
        values.put(DBHelper.TABELA_USUARIO_CAMPO_PASSWORD, usuario.getSenha());
        db.insert(DBHelper.TABELA_USUARIO, null, values);
        super.close(db);
    }

    private Usuario createUsuario(Cursor cursor) throws MediControlException {
        Usuario result = new Usuario();
        result.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_USUARIO_CAMPO_ID)));
        result.setSenha(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_USUARIO_CAMPO_PASSWORD)));
        result.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_USUARIO_CAMPO_EMAIL)));
        return result;
    }
}
