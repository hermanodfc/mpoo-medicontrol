package com.adht.android.medicontrol.paciente.dominio;

import com.adht.android.medicontrol.infra.MediControlException;

import java.util.GregorianCalendar;

public class Paciente {

    private int id;
    private String nome;
    private GregorianCalendar nascimento;
    private Genero genero;

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

    public GregorianCalendar getNascimento() {
        return nascimento;
    }

    public void setNascimento(GregorianCalendar nascimento) throws MediControlException {

        if (nascimento == null) {
            throw new MediControlException("Data de nascimento inválida");
        }

        this.nascimento = nascimento;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) throws MediControlException {

        if (genero == null) {
            throw new MediControlException("Genero inválido");
        }

        this.genero = genero;
    }

}
