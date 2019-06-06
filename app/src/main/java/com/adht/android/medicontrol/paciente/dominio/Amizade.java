package com.adht.android.medicontrol.paciente.dominio;

public class Amizade {

    private int id;
    private Paciente amigo;
    private StatusAmizade statusAmizade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getAmigo() {
        return amigo;
    }

    public void setAmigo(Paciente amigo) {
        this.amigo = amigo;
    }

    public StatusAmizade getStatusAmizade() {
        return statusAmizade;
    }

    public void setStatusAmizade(StatusAmizade statusAmizade) {
        this.statusAmizade = statusAmizade;
    }
}
