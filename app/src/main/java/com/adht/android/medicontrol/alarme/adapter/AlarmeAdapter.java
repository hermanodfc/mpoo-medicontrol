package com.adht.android.medicontrol.alarme.adapter;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull ;
import androidx.recyclerview.widget.RecyclerView;
import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.alarme.dominio.Alarme;

import java.util.List;

public class AlarmeAdapter extends RecyclerView.Adapter<AlarmeAdapter.MyViewHolder> {

    private final List<Alarme> listaAlarmes;

    public AlarmeAdapter(List<Alarme> lista) {
        this.listaAlarmes = lista;
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

        myViewHolder.nomeRemedio.setText(alarme.getNomeMedicamento());
        //myViewHolder.inicio.setText(alarme.getHorarioInicial().toString());
        myViewHolder.complemento.setText(alarme.getComplemento());
        myViewHolder.frequencia.setText(frequenciaComplemento1 + Integer.toString(alarme.getFrequenciaHoras()) + frequenciaComplemento2);
        myViewHolder.dias.setText(Integer.toString(alarme.getDuracaoDias()) + diasComplemento);

    }

    @Override
    public int getItemCount() {
        return listaAlarmes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView nomeRemedio;
        TextView inicio;
        TextView frequencia;
        TextView complemento;
        TextView dias;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeRemedio = itemView.findViewById(R.id.textRemedio);
            //inicio = itemView.findViewById(R.id.textHora);
            frequencia = itemView.findViewById(R.id.textFrequencia);
            complemento = itemView.findViewById(R.id.textComplemento);
            dias = itemView.findViewById(R.id.textDias);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            MenuItem Edit = menu.add(menu.NONE, 1, 1, "Edit");
            MenuItem Delete = menu.add(menu.NONE, 2, 2, "Delete");
//            Edit.setOnMenuItemClickListener(onEditMenu);
//            Delete.setOnMenuItemClickListener(onEditMenu);

        }

    }
}