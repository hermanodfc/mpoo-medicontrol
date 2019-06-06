package com.adht.android.medicontrol.alarme.ui;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.alarme.adapter.AlarmeAdapter;
import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.alarme.persistencia.AlarmeDAOSQLite;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

import java.util.ArrayList;
import java.util.List;

public class AlarmesListaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAlarmes;

    private List<Alarme> listaAlarme = new ArrayList<Alarme>();
    Usuario usuario = Sessao.instance.getUsuario();
    int idPaciente = usuario.getPaciente().getId();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmes_lista);

        recyclerViewAlarmes = findViewById(R.id.recyclerViewAlarmes);

        //configurar adapter e listando alarmes
        AlarmeDAOSQLite daoAlarme = new AlarmeDAOSQLite();
        try {
            listaAlarme = daoAlarme.listar(idPaciente);
        } catch (MediControlException e) {
            e.printStackTrace();
        }
        AlarmeAdapter alarmeAdapter = new AlarmeAdapter(listaAlarme);


        //configurar recycle view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewAlarmes.setLayoutManager(layoutManager);
        recyclerViewAlarmes.setHasFixedSize(true);
        recyclerViewAlarmes.setAdapter(alarmeAdapter);


    }

}