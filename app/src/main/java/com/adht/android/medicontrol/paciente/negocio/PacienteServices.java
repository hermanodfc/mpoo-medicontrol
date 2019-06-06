package com.adht.android.medicontrol.paciente.negocio;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.paciente.persistencia.IPacienteDAO;
import com.adht.android.medicontrol.paciente.persistencia.PacienteDAOSQLite;
import com.adht.android.medicontrol.usuario.dominio.Usuario;
import com.adht.android.medicontrol.usuario.negocio.UsuarioServices;


public class PacienteServices {

    public void cadastrar(Paciente paciente, Usuario usuario) throws MediControlException {
        UsuarioServices usuarioServices = new UsuarioServices();
        if (!usuarioServices.isUsuarioCadastro(usuario.getId())) {
            throw new MediControlException("Usuário não cadastrado");
        }

        if (paciente == null) {
            throw new MediControlException("Paciente inválido");
        }

        IPacienteDAO dao = new PacienteDAOSQLite();
        dao.cadastrar(paciente, usuario.getId());
    }

    public Paciente getPaciente(Usuario usuario) throws MediControlException {
        IPacienteDAO dao = new PacienteDAOSQLite();
        Paciente paciente = dao.getPaciente(usuario.getId());
        return  paciente;
    }

    public Paciente getPaciente(String email) throws MediControlException {

        UsuarioServices usuarioServices = new UsuarioServices();

        int idUsuario;

        try {
            idUsuario = usuarioServices.getUsuario(email);
        } catch (MediControlException exception) {
            throw exception;
        }

        PacienteDAOSQLite pacienteDAOSQLite = new PacienteDAOSQLite();

        return pacienteDAOSQLite.getPaciente(idUsuario);
    }
}
