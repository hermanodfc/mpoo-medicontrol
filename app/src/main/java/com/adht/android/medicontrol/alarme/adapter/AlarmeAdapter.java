package com.adht.android.medicontrol.alarme.adapter;


import android.app.Activity;
import android.content.Intent;
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
import com.adht.android.medicontrol.alarme.negocio.AlarmeServices;
import com.adht.android.medicontrol.alarme.ui.AlarmeAtualizacaoActivity;
import com.adht.android.medicontrol.alarme.ui.AlarmesListaActivity;


import java.util.List;

public class AlarmeAdapter extends RecyclerView.Adapter<AlarmeAdapter.MyViewHolder> {

    private final List<Alarme> listaAlarmes;
    private Activity activity;
    private final AlarmeServices services = new AlarmeServices();

    public AlarmeAdapter(Activity activity, List<Alarme> lista) {
        this.activity = activity;
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        int idAlarme;
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
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            MenuItem edit = menu.add(menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(menu.NONE, 2, 2, "Delete");
            edit.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);

        }


        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        Intent intent = new Intent(activity, AlarmeAtualizacaoActivity.class);
                        intent.putExtra("ALARME_ID", idAlarme);
                        activity.startActivity(intent);
                        break;

                    case 2:
                        services.deletar(idAlarme);
                        activity.finish();
                        Intent intent2 = new Intent(activity, AlarmesListaActivity.class);
                        activity.startActivity(intent2);
                        break;
                }
                return true;
            }
        };

    }
}