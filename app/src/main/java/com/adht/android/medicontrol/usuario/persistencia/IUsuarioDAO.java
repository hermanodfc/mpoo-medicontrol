package com.adht.android.medicontrol.usuario.persistencia;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

public interface IUsuarioDAO {
    Usuario getUsuario(String email) throws MediControlException;
    Usuario getUsuario(String email, String password) throws MediControlException;
    void cadastrar(Usuario usuario) throws MediControlException;
}
