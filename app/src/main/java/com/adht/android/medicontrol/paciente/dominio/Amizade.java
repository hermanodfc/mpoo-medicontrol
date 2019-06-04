package com.adht.android.medicontrol.paciente.dominio;

import com.adht.android.medicontrol.usuario.dominio.Usuario;

public class Amizade {

    private Paciente paciente;
    private StatusAmizade statusAmizade;

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public StatusAmizade getStatusAmizade() {
        return statusAmizade;
    }

    public void setStatusAmizade(StatusAmizade statusAmizade) {
        this.statusAmizade = statusAmizade;
    }
}
