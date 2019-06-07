package com.adht.android.medicontrol.paciente.dominio;

public enum StatusAmizade {

    PENDENTE(1),
    ACEITO(2);
    private final int valor;

    private StatusAmizade(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return this.valor;
    }

    public static StatusAmizade instanciaValor(int valor){
        StatusAmizade result = null;
        switch (valor) {
            case 1:
                result = PENDENTE;
                break;
            case 2:
                result = ACEITO;
                break;
        }
        return result;
    }
}
