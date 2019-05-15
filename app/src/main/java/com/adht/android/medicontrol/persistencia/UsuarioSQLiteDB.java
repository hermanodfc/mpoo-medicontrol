package com.adht.android.medicontrol.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adht.android.medicontrol.usuario.model.Usuario;

public class UsuarioSQLiteDB extends AbstractDB implements UsuarioDao {

    public Usuario getUsuario(String email){
        Usuario result = null;
        SQLiteDatabase db = super.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABELA_USUARIO + " U WHERE U." + DBHelper.CAMPO_EMAIL + " LIKE ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{email});
        if (cursor.moveToFirst()){
            result = createUsuario(cursor);
        }
        super.close(cursor, db);
        return result;
    }

    public Usuario getUsuario(String email, String senha){
        Usuario result = getUsuario(email);
        if (result != null && !senha.equals(result.getPassword())){
            result = null;
        }
        return result;
    }

    public void cadastrar(Usuario usuario){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //Coloca os valores do campo na variavel values pra depois inserir no banco db
        values.put(DBHelper.CAMPO_NOME, usuario.getName());
        values.put(DBHelper.CAMPO_EMAIL, usuario.getEmail());
        //values.put(DBHelper.CAMPO_GENERO, usuario.);  falta implementar genero em usuario
        values.put(DBHelper.CAMPO_SENHA, usuario.getPassword());
        db.insert(DBHelper.TABELA_USUARIO, null, values);
        super.close(db);
    }

    private Usuario createUsuario(Cursor cursor){
        Usuario result = new Usuario();
        int colummIndex = cursor.getColumnIndex(DBHelper.CAMPO_ID);
        result.setId(cursor.getString(colummIndex));
        colummIndex = cursor.getColumnIndex(DBHelper.CAMPO_NOME);
        result.setName(cursor.getString(colummIndex));
        colummIndex = cursor.getColumnIndex(DBHelper.CAMPO_EMAIL);
        result.setEmail(cursor.getString(colummIndex));
        //colummIndex = cursor.getColumnIndex(DBHelper.CAMPO_GENERO);
        //Fazer a mesma coisa com o result aqui para genero
        colummIndex = cursor.getColumnIndex(DBHelper.CAMPO_SENHA);
        result.setPassword(cursor.getString(colummIndex));
        return result;
    }
}
