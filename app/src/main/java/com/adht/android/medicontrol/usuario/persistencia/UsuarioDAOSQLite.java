package com.adht.android.medicontrol.usuario.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.infra.exception.UsuarioEmailInvalidoException;
import com.adht.android.medicontrol.infra.exception.UsuarioSenhaInvalidaException;
import com.adht.android.medicontrol.infra.persistencia.AbstractSQLite;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

import java.io.IOException;

public class UsuarioDAOSQLite extends AbstractSQLite {

    public Usuario getUsuario(String email) throws IOException {
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

    public Usuario getUsuario(String email, String password) throws IOException {
        Usuario result = getUsuario(email);
        if (result != null && !password.equals(result.getSenha())) {
            result = null;
        }

        return result;
    }

    private Usuario getUsuario(int id) throws IOException {
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

    public boolean isUsuarioCadastrado(int idUsuario) throws IOException {
        Usuario usuario = getUsuario(idUsuario);

        boolean result = true;

        if (usuario == null) {
            result = false;
        }

        return result;
    }

    public void cadastrar(Usuario usuario) throws IOException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.TABELA_USUARIO_CAMPO_EMAIL, usuario.getEmail());
        values.put(DBHelper.TABELA_USUARIO_CAMPO_PASSWORD, usuario.getSenha());
        db.insert(DBHelper.TABELA_USUARIO, null, values);
        super.close(db);
    }

    private Usuario createUsuario(Cursor cursor) {
        Usuario result = new Usuario();
        result.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.TABELA_USUARIO_CAMPO_ID)));
        try {
            result.setSenha(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_USUARIO_CAMPO_PASSWORD)));
        } catch (UsuarioSenhaInvalidaException e) {
            e.printStackTrace();
        }
        try {
            result.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.TABELA_USUARIO_CAMPO_EMAIL)));
        } catch (UsuarioEmailInvalidoException e) {
            e.printStackTrace();
        }
        return result;
    }
}
