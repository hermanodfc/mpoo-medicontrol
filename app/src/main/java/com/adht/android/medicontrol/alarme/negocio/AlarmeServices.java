package com.adht.android.medicontrol.alarme.negocio;

import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.alarme.persistencia.AlarmeDAOSQLite;
import com.adht.android.medicontrol.alarme.persistencia.IAlarmeDao;
import com.adht.android.medicontrol.infra.MediControlException;
import com.adht.android.medicontrol.infra.Sessao;

public class AlarmeServices {
    public void cadastrar(Alarme alarme) throws MediControlException {
        IAlarmeDao dao = new AlarmeDAOSQLite();
        if (dao.getAlarme(alarme.getNome()) != null) {
            throw new MediControlException("Remédio já cadastrado");
        }
        dao.cadastrar(alarme);
    }

    public void logout() {
        Sessao sessao = Sessao.instance;
        sessao.reset();
    }
}
