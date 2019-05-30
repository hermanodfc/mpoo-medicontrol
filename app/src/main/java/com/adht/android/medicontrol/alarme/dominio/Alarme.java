package com.adht.android.medicontrol.alarme.dominio;

import com.adht.android.medicontrol.infra.MediControlException;

import java.util.Date;

public class Alarme {
    private int id;
    private String nome;
    private int inicio;
    private int frequencia;
    private String complemento;

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

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
