package com.adht.android.medicontrol.usuario.negocio;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.paciente.negocio.PacienteServices;
import com.adht.android.medicontrol.usuario.dominio.Usuario;
import com.adht.android.medicontrol.usuario.persistencia.IUsuarioDAO;
import com.adht.android.medicontrol.usuario.persistencia.UsuarioDAOSQLite;

public class UsuarioServices {
    public Usuario cadastrar(Usuario usuario) throws MediControlException{
        IUsuarioDAO dao = new UsuarioDAOSQLite();
        if (dao.getUsuario(usuario.getEmail()) != null) {
            throw new MediControlException("Usuário já cadastrado");
        }
        dao.cadastrar(usuario);
        return dao.getUsuario(usuario.getEmail());
    }

    public void login(String email, String password) throws MediControlException {
        IUsuarioDAO dao = new UsuarioDAOSQLite();
        Usuario usuario = dao.getUsuario(email, password);
        if (usuario == null) {
            throw new MediControlException("Usuário não cadastrado");
        }

        usuario.setPaciente(new PacienteServices().getPaciente(usuario));
        Sessao.instance.setUsuario(usuario);
    }

    public void logout() {
        Sessao.instance.reset();
    }

    public boolean isUsuarioCadastro(int idUsuario) throws MediControlException {
        return new UsuarioDAOSQLite().isUsuarioCadastrado(idUsuario);
    }

    public int getUsuario(String email) throws MediControlException {
        IUsuarioDAO dao = new UsuarioDAOSQLite();
        Usuario usuario = dao.getUsuario(email);
        if (usuario == null) {
            throw new MediControlException("Usuário já cadastrado");
        }

        return usuario.getId();
    }
}