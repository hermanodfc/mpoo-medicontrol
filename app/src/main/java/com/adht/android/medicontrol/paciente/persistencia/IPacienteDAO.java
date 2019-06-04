package com.adht.android.medicontrol.paciente.persistencia;

import com.adht.android.medicontrol.infra.MediControlException;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

public interface IPacienteDAO {
    void cadastrar(Paciente paciente, int idUsuario) throws MediControlException;

    Paciente getPaciente(int id) throws MediControlException;
}
