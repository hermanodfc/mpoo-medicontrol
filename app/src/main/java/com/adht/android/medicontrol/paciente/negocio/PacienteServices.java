package com.adht.android.medicontrol.paciente.negocio;

import com.adht.android.medicontrol.infra.exception.PacienteInvalidoException;
import com.adht.android.medicontrol.infra.exception.PacienteNascimentoInvalidoException;
import com.adht.android.medicontrol.infra.exception.PacienteNomeInvalidoException;
import com.adht.android.medicontrol.infra.exception.UsuarioEmailInvalidoException;
import com.adht.android.medicontrol.infra.exception.UsuarioNaoCadastradoException;
import com.adht.android.medicontrol.infra.exception.UsuarioSenhaInvalidaException;
import com.adht.android.medicontrol.infra.persistencia.PacienteGeneroInvalidoException;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.paciente.persistencia.PacienteDAOSQLite;
import com.adht.android.medicontrol.usuario.dominio.Usuario;
import com.adht.android.medicontrol.usuario.negocio.UsuarioServices;

import java.io.IOException;


public class PacienteServices {

    public void cadastrar(Paciente paciente, Usuario usuario) throws UsuarioNaoCadastradoException,
            IOException, PacienteInvalidoException {
        UsuarioServices usuarioServices = new UsuarioServices();

        if (!usuarioServices.isUsuarioCadastro(usuario.getId())) {
            throw new UsuarioNaoCadastradoException("Usuário não cadastrado");
        }

        if (paciente == null) {
            throw new PacienteInvalidoException("Paciente inválido");
        }

        PacienteDAOSQLite dao = new PacienteDAOSQLite();
        dao.cadastrar(paciente, usuario.getId());
    }

    public Paciente getPaciente(Usuario usuario) throws IOException {
        PacienteDAOSQLite dao = new PacienteDAOSQLite();
        Paciente paciente = dao.getPaciente(usuario.getId());
        return  paciente;
    }

    public Paciente getPaciente(String email) throws UsuarioNaoCadastradoException,
            IOException {

        UsuarioServices usuarioServices = new UsuarioServices();

        int idUsuario = usuarioServices.getUsuario(email);

        PacienteDAOSQLite pacienteDAOSQLite = new PacienteDAOSQLite();

        return pacienteDAOSQLite.getPaciente(idUsuario);
    }
}
