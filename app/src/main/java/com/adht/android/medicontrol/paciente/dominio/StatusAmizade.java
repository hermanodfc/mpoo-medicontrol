package com.adht.android.medicontrol.paciente.dominio;

public enum StatusAmizade {

    ENVIADO_PENDENTE(1),
    ENVIADO_ACEITO(2),
    RECEBIDO_PENDENTE(3),
    RECEBIDO_ACEITO(4);

    private final int valor;

    private StatusAmizade(int valor) {
        this.valor = valor;
    }

    private int getValor() {
        return this.valor;
    }

    public StatusAmizade instanciaValor(int valor){
        StatusAmizade result = null;
        switch (valor) {
            case 1:
                result = ENVIADO_PENDENTE;
                break;
            case 2:
                result = ENVIADO_ACEITO;
                break;
            case 3:
                result = RECEBIDO_PENDENTE;
                break;
            case 4:
                result = RECEBIDO_ACEITO;
                break;
        }
        return result;
    }
}
