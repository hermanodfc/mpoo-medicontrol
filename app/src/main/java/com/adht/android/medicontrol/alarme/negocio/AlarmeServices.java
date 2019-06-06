package com.adht.android.medicontrol.alarme.negocio;

import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.alarme.persistencia.AlarmeDAOSQLite;
import com.adht.android.medicontrol.infra.Sessao;
<<<<<<< HEAD
import com.adht.android.medicontrol.usuario.dominio.Usuario;

public class AlarmeServices {
    public void cadastrar(Alarme alarme) throws MediControlException {
        IAlarmeDao dao = new AlarmeDAOSQLite();
=======
import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

import java.io.IOException;

public class AlarmeServices {
    public void cadastrar(Alarme alarme) throws MediControlException, IOException {
        AlarmeDAOSQLite dao = new AlarmeDAOSQLite();
>>>>>>> versao-0.2
        Usuario usuario = Sessao.instance.getUsuario();
        // Arranjar um jeito de validar se o remédio já não está cadastro (precisa checar se é o mesmo usuário antes)
/*        if (dao.getAlarme(alarme.getNome()) != null) {
            throw new MediControlException("Remédio já cadastrado");
        }*/
        int idPaciente = usuario.getPaciente().getId();
        dao.cadastrar(alarme, idPaciente);
    }

    public void logout() {
        Sessao sessao = Sessao.instance;
        sessao.reset();
    }
}