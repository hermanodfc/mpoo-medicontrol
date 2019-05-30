package com.adht.android.medicontrol.alarme.persistencia;

import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.infra.MediControlException;

public interface IAlarmeDao {
    Alarme getAlarme(String nome) throws MediControlException;
    void cadastrar(Alarme alarme) throws MediControlException;
}
