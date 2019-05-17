package com.adht.android.medicontrol.usuario.negocio;

import com.adht.android.medicontrol.infra.MediControlException;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.usuario.dominio.Usuario;
import com.adht.android.medicontrol.usuario.persistencia.IUsuarioDAO;
import com.adht.android.medicontrol.usuario.persistencia.UsuarioDAOSQLite;

public class UsuarioServices {
    public void cadastrar(Usuario usuario) throws MediControlException{
        IUsuarioDAO dao = new UsuarioDAOSQLite();
        if (dao.getUsuario(usuario.getEmail()) != null) {
            throw new MediControlException("Usuário já cadastrado");
        }
        dao.cadastrar(usuario);
    }

    public void login(String email, String password) throws MediControlException {
        IUsuarioDAO dao = new UsuarioDAOSQLite();
        Usuario usuario = dao.getUsuario(email, password);
        if (usuario == null) {
            throw new MediControlException("Usuário não cadastrado");
        }
        Sessao.instance.setUsuario(usuario);
    }

    public void logout() {
        Sessao sessao = Sessao.instance;
        sessao.reset();
    }
}