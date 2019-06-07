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
    public void cadastrar(Alarme alarme) throws IOException {
        AlarmeDAOSQLite dao = new AlarmeDAOSQLite();
        Usuario usuario = Sessao.instance.getUsuario();
        int idPaciente = usuario.getPaciente().getId();
        dao.cadastrar(alarme, idPaciente);
    }

    public List<Alarme> listar(int idPaciente) throws MediControlException {

        AlarmeDAOSQLite daoAlarme = new AlarmeDAOSQLite();

        return daoAlarme.listar(idPaciente);
    }

    public void atualizar(Alarme alarme, int idAlarme) throws IOException {
        AlarmeDAOSQLite dao = new AlarmeDAOSQLite();
        dao.atualizar(alarme, idAlarme);
    }

    public void deletar(int idAlarme){
        AlarmeDAOSQLite dao = new AlarmeDAOSQLite();
        dao.deletar(idAlarme);
    }

    public Alarme getAlarme(int idAlarme){
        Alarme alarme = new Alarme();
        AlarmeDAOSQLite dao = new AlarmeDAOSQLite();
        try {
            alarme = dao.getAlarme(idAlarme);
        } catch (MediControlException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return alarme;

    }
}