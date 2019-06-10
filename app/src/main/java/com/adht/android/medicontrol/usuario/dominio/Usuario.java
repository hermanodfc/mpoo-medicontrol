package com.adht.android.medicontrol.usuario.dominio;

import com.adht.android.medicontrol.infra.exception.PacienteInvalidoException;
import com.adht.android.medicontrol.infra.exception.UsuarioEmailInvalidoException;
import com.adht.android.medicontrol.infra.exception.UsuarioSenhaInvalidaException;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.util.EmailValidator;

public class Usuario {

    private long id;
    private String email;
    private String senha;
    private Paciente paciente;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws UsuarioEmailInvalidoException {

        EmailValidator emailValidator = new EmailValidator();

        if (email == null || !emailValidator.isValidEmail(email)) {
            throw new UsuarioEmailInvalidoException("Endereço de email inválido");
        }

        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) throws UsuarioSenhaInvalidaException {

        if (senha == null) {
            throw new UsuarioSenhaInvalidaException("Senha inválida");
        } else if (senha.length() < 6) {
            throw new UsuarioSenhaInvalidaException("A senha deve ter, pelo menos, 6 caracteres");
        }

        this.senha = senha;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) throws PacienteInvalidoException {
        if (paciente == null) {
            throw new PacienteInvalidoException("Paciente inválido");
        }

        this.paciente = paciente;
    }
}
