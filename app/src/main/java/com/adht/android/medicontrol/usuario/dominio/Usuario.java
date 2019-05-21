package com.adht.android.medicontrol.usuario.dominio;

import com.adht.android.medicontrol.infra.MediControlException;
import com.adht.android.medicontrol.util.EmailValidator;
import java.util.GregorianCalendar;

public class Usuario {

    private int mId;
    private String mEmail;
    private String mNome;
    private GregorianCalendar mNascimento;
    private Sexo mSexo;
    private String mSenha;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) throws MediControlException {

        EmailValidator emailValidator = new EmailValidator();

        if (email == null || !emailValidator.isValidEmail(email)) {
            throw new MediControlException("Endereço de email inválido");
        }

        mEmail = email;
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) throws MediControlException {

        if (nome == null || nome.trim() == "") {
            throw new MediControlException("Nome inválido");
        }

        mNome = nome;
    }

    public GregorianCalendar getNascimento() {
        return mNascimento;
    }

    public void setNascimento(GregorianCalendar nascimento) throws MediControlException {

        if (nascimento == null) {
            throw new MediControlException("Data de nascimento inválida");
        }

        mNascimento = nascimento;
    }

    public Sexo getSexo() {
        return mSexo;
    }

    public void setSexo(Sexo sexo) throws MediControlException {

        if (sexo == null) {
            throw new MediControlException("Sexo inválido");
        }

        mSexo = sexo;
    }

    public String getSenha() {
        return mSenha;
    }

    public void setSenha(String senha) throws MediControlException {

        if (senha == null) {
            throw new MediControlException("Senha inválida");
        } else if (senha.length() < 6) {
            throw new MediControlException("A senha deve ter, pelo menos, 6 caracteres");
        }

        mSenha = senha;
    }
}
