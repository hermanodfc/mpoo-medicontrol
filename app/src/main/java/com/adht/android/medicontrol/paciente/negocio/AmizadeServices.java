package com.adht.android.medicontrol.paciente.negocio;

import com.adht.android.medicontrol.infra.exception.AmizadeExistenteException;
import com.adht.android.medicontrol.infra.exception.PacienteNascimentoInvalidoException;
import com.adht.android.medicontrol.infra.exception.PacienteNomeInvalidoException;
import com.adht.android.medicontrol.infra.persistencia.AmizadeSemAmigos;
import com.adht.android.medicontrol.infra.persistencia.PacienteGeneroInvalidoException;
import com.adht.android.medicontrol.paciente.dominio.Amizade;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.paciente.persistencia.AmizadeDAOSQLite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AmizadeServices {

    public void cadastrarPedidoAmizade(Amizade amizade)
            throws AmizadeExistenteException, IOException {
        AmizadeDAOSQLite dao = new AmizadeDAOSQLite();
        Amizade result = dao.getAmizade(amizade.getSolicitante(), amizade.getConvidado());

        if (result != null) {
            throw new AmizadeExistenteException("Amizade já existente.");
        }
        dao.cadastrarPedidoAmizade(amizade);
    }

    public List<Amizade> getAmigos(Paciente paciente) throws IOException, AmizadeSemAmigos {
        AmizadeDAOSQLite dao = new AmizadeDAOSQLite();
        ArrayList<Amizade> result = dao.getAmizades(paciente);
        if (result.size() == 0) {
            throw new AmizadeSemAmigos("Você ainda não tem amigos");
        }
        return result;
    }

    public void desfazerAmizade(Amizade amizade) throws IOException {
        AmizadeDAOSQLite dao = new AmizadeDAOSQLite();
        dao.desfazerAmizade(amizade);
    }

    public void atualizar(Amizade amizade) {
        AmizadeDAOSQLite dao = new AmizadeDAOSQLite();
        dao.atualizar(amizade);
    }
}
