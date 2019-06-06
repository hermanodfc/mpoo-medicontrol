package com.adht.android.medicontrol.paciente.persistencia;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.paciente.dominio.Amizade;
import com.adht.android.medicontrol.paciente.dominio.Paciente;

public interface IAmizadeDAO {
    Amizade getAmizade(Paciente paciente, Paciente amigo) throws MediControlException;

    void cadastrarPedidoAmizade(Paciente paciente, Amizade amizade) throws MediControlException;
}
