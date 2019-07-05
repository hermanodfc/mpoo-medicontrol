package com.adht.android.medicontrol.alarme.dominio;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.paciente.dominio.Paciente;

public class Alarme {
    private long id;
    private String nomeMedicamento;
    private String complemento;
    private String horarioInicial;
    private int frequenciaHoras;
    private int duracaoDias;
    private Paciente paciente;
    private int requestCode;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) throws MediControlException {
        if (nomeMedicamento == null || nomeMedicamento.trim().equals("")) {
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

    public String getHorarioInicial() {
        return horarioInicial;
    }

    public void setHorarioInicial(String horarioInicial) {
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