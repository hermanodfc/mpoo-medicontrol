package com.adht.android.medicontrol.alarme.dominio;

import com.adht.android.medicontrol.infra.MediControlException;

import java.util.Date;

public class Alarme {
    private int id;
    private String nome;
    private String inicio;
    private String frequencia;
    private String complemento;
    private String dias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws MediControlException {

        if (nome == null || nome.trim() == "") {
            throw new MediControlException("Nome inválido");
        }

        this.nome = nome;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }
}
