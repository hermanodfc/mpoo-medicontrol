package com.adht.android.medicontrol.usuario.dominio;

public enum Sexo {

    FEMININO(1),
    MASCULINO(2);

    private int valor;

    private Sexo(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return this.valor;
    }

    public static Sexo instanciaValor(int valor) {
        Sexo retorno = MASCULINO;
        if (valor == 1) {
            return FEMININO;
        }

        return retorno;
    }
}
