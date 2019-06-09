package com.adht.android.medicontrol.usuario.negocio;

import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.infra.exception.PacienteInvalidoException;
import com.adht.android.medicontrol.infra.exception.UsuarioJaCadastradoException;
import com.adht.android.medicontrol.infra.exception.UsuarioNaoCadastradoException;
import com.adht.android.medicontrol.paciente.negocio.PacienteServices;
import com.adht.android.medicontrol.usuario.dominio.Usuario;
import com.adht.android.medicontrol.usuario.persistencia.UsuarioDAOSQLite;

import java.io.IOException;

public class UsuarioServices {

    public Usuario cadastrar(Usuario usuario) throws IOException, UsuarioJaCadastradoException {
        UsuarioDAOSQLite dao = new UsuarioDAOSQLite();
        if (dao.getUsuario(usuario.getEmail()) != null) {
            throw new UsuarioJaCadastradoException("Usuário já cadastrado");
        }
        dao.cadastrar(usuario);
        return dao.getUsuario(usuario.getEmail());
    }

    public void login(String email, String password) throws IOException, UsuarioNaoCadastradoException,
            PacienteInvalidoException {
        UsuarioDAOSQLite dao = new UsuarioDAOSQLite();
        Usuario usuario = dao.getUsuario(email, password);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException("Usuário não cadastrado");
        }

        usuario.setPaciente(new PacienteServices().getPaciente(usuario));
        Sessao.INSTANCE.setUsuario(usuario);
    }

    public void logout() {
        Sessao.INSTANCE.reset();
    }

    public boolean isUsuarioCadastro(int idUsuario) throws IOException {
        return new UsuarioDAOSQLite().isUsuarioCadastrado(idUsuario);
    }

    public int getUsuario(String email) throws UsuarioNaoCadastradoException, IOException {
        UsuarioDAOSQLite dao = new UsuarioDAOSQLite();
        Usuario usuario = dao.getUsuario(email);
        if (usuario == null) {
            throw new UsuarioNaoCadastradoException("Usuário não cadastrado");
        }

        return usuario.getId();
    }
}