package com.adht.android.medicontrol.paciente.dominio;

public enum Genero {

    FEMININO(1),
    MASCULINO(2);

    private int valor;

    private Genero(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return this.valor;
    }

    public static Genero instanciaValor(int valor) {
        Genero retorno = MASCULINO;
        if (valor == 1) {
            return FEMININO;
        }

        return retorno;
    }
}
