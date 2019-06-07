package com.adht.android.medicontrol.paciente.dominio;

public class Amizade {

    private int id;
    private Paciente solicitante;
    private Paciente convidado;
    private StatusAmizade statusAmizade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Paciente solicitante) {
        this.solicitante = solicitante;
    }

    public Paciente getConvidado() {
        return convidado;
    }

    public void setConvidado(Paciente convidado) {
        this.convidado = convidado;
    }

    public StatusAmizade getStatus() {
        return statusAmizade;
    }

    public void setStatusAmizade(StatusAmizade statusAmizade) {
        this.statusAmizade = statusAmizade;
    }
}
