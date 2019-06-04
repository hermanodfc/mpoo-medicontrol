package com.adht.android.medicontrol.alarme.dominio;

import com.adht.android.medicontrol.infra.MediControlException;

import java.util.Date;

public class Alarme {
    private int id;
    private String nomeMedicamento;
    private String complemento;
    private Date horarioInicial;
    private int frequenciaHoras;
    private int duracaoDias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) throws MediControlException {
        if (nomeMedicamento == null || nomeMedicamento.trim() == "") {
            throw new MediControlException("Nome inválido");
        }
        this.nomeMedicamento = nomeMedicamento;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Date getHorarioInicial() {
        return horarioInicial;
    }

    public void setHorarioInicial(Date horarioInicial) {
        this.horarioInicial = horarioInicial;
    }

    public int getFrequenciaHoras() {
        return frequenciaHoras;
    }

    public void setFrequenciaHoras(int frequenciaHoras) {
        this.frequenciaHoras = frequenciaHoras;
    }

    public int getDuracaoDias() {
        return duracaoDias;
    }

    public void setDuracaoDias(int duracaoDias) {
        this.duracaoDias = duracaoDias;
    }
}
