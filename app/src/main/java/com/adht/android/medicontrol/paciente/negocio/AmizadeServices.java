package com.adht.android.medicontrol.paciente.negocio;

import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.paciente.dominio.Amizade;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.paciente.persistencia.AmizadeDAOSQLite;
import com.adht.android.medicontrol.paciente.persistencia.IAmizadeDAO;

public class AmizadeServices {

    public void cadastrarPedidoAmizade(Paciente paciente, Amizade amizade) throws MediControlException {
        IAmizadeDAO dao = new AmizadeDAOSQLite();
        Amizade result = dao.getAmizade(paciente, amizade.getAmigo());

        if (result != null) {
            throw new MediControlException("Amizade já existente.");
        }

        dao.cadastrarPedidoAmizade(paciente, amizade);
    }
}
