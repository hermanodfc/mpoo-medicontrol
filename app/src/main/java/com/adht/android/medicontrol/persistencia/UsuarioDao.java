package com.adht.android.medicontrol.persistencia;

import com.adht.android.medicontrol.infra.MediControlAppException;
import com.adht.android.medicontrol.usuario.model.Usuario;

public interface UsuarioDao {
    Usuario getUsuario(String email) throws MediControlAppException;
    Usuario getUsuario(String email, String senha) throws MediControlAppException;
    void cadastrar(Usuario usuario) throws MediControlAppException;
}
