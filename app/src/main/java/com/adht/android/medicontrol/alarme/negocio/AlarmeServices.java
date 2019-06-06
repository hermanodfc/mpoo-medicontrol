package com.adht.android.medicontrol.alarme.negocio;

import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.alarme.persistencia.AlarmeDAOSQLite;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlarmeServices {
    public void cadastrar(Alarme alarme) throws MediControlException, IOException {
        AlarmeDAOSQLite dao = new AlarmeDAOSQLite();
        Usuario usuario = Sessao.instance.getUsuario();
        // Arranjar um jeito de validar se o remédio já não está cadastro (precisa checar se é o mesmo usuário antes)
/*        if (dao.getAlarme(alarme.getNome()) != null) {
            throw new MediControlException("Remédio já cadastrado");
        }*/
        int idPaciente = usuario.getPaciente().getId();
        dao.cadastrar(alarme, idPaciente);
    }

    public List<Alarme> listar(int idPaciente) throws MediControlException {

        AlarmeDAOSQLite daoAlarme = new AlarmeDAOSQLite();

        return daoAlarme.listar(idPaciente);
    }

    public void logout() {
        Sessao sessao = Sessao.instance;
        sessao.reset();
    }
}