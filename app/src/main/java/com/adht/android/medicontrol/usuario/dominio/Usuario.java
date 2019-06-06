package com.adht.android.medicontrol.usuario.dominio;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.util.EmailValidator;

public class Usuario {

    private int id;
    private String email;
    private String senha;
    private Paciente paciente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws MediControlException {

        EmailValidator emailValidator = new EmailValidator();

        if (email == null || !emailValidator.isValidEmail(email)) {
            throw new MediControlException("Endereço de email inválido");
        }

        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) throws MediControlException {

        if (senha == null) {
            throw new MediControlException("Senha inválida");
        } else if (senha.length() < 6) {
            throw new MediControlException("A senha deve ter, pelo menos, 6 caracteres");
        }

        this.senha = senha;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) throws MediControlException {
        if (paciente == null) {
            throw new MediControlException("Paciente inválido");
        }

        this.paciente = paciente;
    }
}
