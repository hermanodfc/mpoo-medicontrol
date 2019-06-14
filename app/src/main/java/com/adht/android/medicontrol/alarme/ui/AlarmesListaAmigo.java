package com.adht.android.medicontrol.alarme.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.alarme.negocio.AlarmeServices;
import com.adht.android.medicontrol.infra.exception.MediControlException;

import java.util.ArrayList;
import java.util.List;

public class AlarmesListaAmigo extends AppCompatActivity {

    private AlarmeAdapter alarmeAdapter;
    private RecyclerView recyclerViewAlarmes;
    private List<Alarme> listaAlarme = new ArrayList<Alarme>();
    private long idAmigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmes_lista_amigo);
        Intent intent = getIntent();
        idAmigo = intent.getLongExtra("AMIGO_ID", 0);

        recyclerViewAlarmes = findViewById(R.id.recyclerViewAlarmes);



        AlarmeServices alarmeServices = new AlarmeServices();
        try {
            listaAlarme = alarmeServices.listar(idAmigo);
        } catch (MediControlException e) {
            e.printStackTrace();
        }
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerViewAlarmes.setHasFixedSize(true);
        alarmeAdapter = new AlarmeAdapter(listaAlarme);
        recyclerViewAlarmes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAlarmes.setAdapter(alarmeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.alarmes_menu, menu);

        MenuItem buscaItem = menu.findItem(R.id.action_search);
        SearchView buscaView = (SearchView) buscaItem.getActionView();
        buscaView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                alarmeAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public class AlarmeAdapter extends RecyclerView.Adapter<AlarmeAdapter.MyViewHolder> implements Filterable {

        private List<Alarme> listaAlarmes;
        private List<Alarme> listaAlarmesFull;

        public AlarmeAdapter(List<Alarme> lista) {
            this.listaAlarmes = lista;
            listaAlarmesFull = new ArrayList<>(listaAlarmes);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_alarme_adapter, parent, false);

            return new MyViewHolder(itemLista);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            Alarme alarme = listaAlarmes.get(position);
            final String diasComplemento = " Dias";
            final String frequenciaComplemento1 = "A cada ";
            final String frequenciaComplemento2 = " hora(s)";


            myViewHolder.idAlarme = alarme.getId();

            myViewHolder.nomeRemedio.setText(alarme.getNomeMedicamento());
            myViewHolder.inicio.setText(alarme.getHorarioInicial());
            myViewHolder.complemento.setText(alarme.getComplemento());
            myViewHolder.frequencia.setText(frequenciaComplemento1 + Integer.toString(alarme.getFrequenciaHoras()) + frequenciaComplemento2);
            myViewHolder.dias.setText(Integer.toString(alarme.getDuracaoDias()) + diasComplemento);

        }

        @Override
        public int getItemCount() {
            return listaAlarmes.size();
        }


        public Filter getFilter(){
            return alarmesFilter;
        }

        private Filter alarmesFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Alarme> listaFiltrada = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    listaFiltrada.addAll(listaAlarmesFull);
                } else {
                    String filterPadrao = constraint.toString().toLowerCase().trim();

                    for (Alarme item : listaAlarmesFull){
                        if (item.getNomeMedicamento().toLowerCase().contains(filterPadrao)){
                            listaFiltrada.add(item);
                        }
                    }
                }

                FilterResults resultado = new FilterResults();
                resultado.values = listaFiltrada;

                return resultado;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults resultado) {
                listaAlarmes.clear();

                listaAlarmes.addAll((List) resultado.values);
                notifyDataSetChanged();
            }
        };



        public class MyViewHolder extends RecyclerView.ViewHolder {

            long idAlarme;
            TextView nomeRemedio;
            TextView inicio;
            TextView frequencia;
            TextView complemento;
            TextView dias;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                nomeRemedio = itemView.findViewById(R.id.textRemedio);
                inicio = itemView.findViewById(R.id.textHora);
                frequencia = itemView.findViewById(R.id.textFrequencia);
                complemento = itemView.findViewById(R.id.textComplemento);
                dias = itemView.findViewById(R.id.textDias);
            }

        }
    }
}
