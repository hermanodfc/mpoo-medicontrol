package com.adht.android.medicontrol.paciente.dominio;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.infra.exception.PacienteNascimentoInvalidoException;
import com.adht.android.medicontrol.infra.exception.PacienteNomeInvalidoException;
import com.adht.android.medicontrol.infra.persistencia.PacienteGeneroInvalidoException;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Paciente {

    private int id;
    private String nome;
    private GregorianCalendar nascimento;
    private Genero genero;
    private ArrayList<Amizade> amizades = new ArrayList<>();

    public ArrayList<Amizade> getAmizades() {
        return amizades;
    }

    public void setAmizades(ArrayList<Amizade> amizades) {
        this.amizades = amizades;
    }

    public void adicionarAmizade(Amizade amizade) {
        amizades.add(amizade);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws PacienteNomeInvalidoException {

        if (nome == null || nome.trim() == "") {
            throw new PacienteNomeInvalidoException("Nome inválido");
        }

        this.nome = nome;
    }

    public GregorianCalendar getNascimento() {
        return nascimento;
    }

    public void setNascimento(GregorianCalendar nascimento) throws PacienteNascimentoInvalidoException {

        if (nascimento == null) {
            throw new PacienteNascimentoInvalidoException("Data de nascimento inválida");
        }

        this.nascimento = nascimento;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) throws PacienteGeneroInvalidoException {

        if (genero == null) {
            throw new PacienteGeneroInvalidoException("Genero inválido");
        }

        this.genero = genero;
    }

}
