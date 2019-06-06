package com.adht.android.medicontrol.alarme.persistencia;

import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.infra.exception.MediControlException;

public interface IAlarmeDao {
    Alarme getAlarmes(int idPaciente) throws MediControlException;
    void cadastrar(Alarme alarme, int idPaciente) throws MediControlException;
}
