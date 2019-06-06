package com.adht.android.medicontrol.alarme.adapter;

<<<<<<< HEAD
import android.database.sqlite.SQLiteDatabase;
=======
>>>>>>> versao-0.2
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
<<<<<<< HEAD

=======
>>>>>>> versao-0.2
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.alarme.dominio.Alarme;
<<<<<<< HEAD
import com.adht.android.medicontrol.alarme.persistencia.AlarmeDAOSQLite;
import com.adht.android.medicontrol.infra.persistencia.DBHelper;


=======
>>>>>>> versao-0.2
import java.util.List;

public class AlarmeAdapter extends RecyclerView.Adapter<AlarmeAdapter.MyViewHolder> {

    private final List<Alarme> listaAlarmes;

<<<<<<< HEAD


=======
>>>>>>> versao-0.2
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

        myViewHolder.nomeRemedio.setText(alarme.getNomeMedicamento());
        //myViewHolder.inicio.setText(alarme.getHorarioInicial().toString());
        myViewHolder.complemento.setText(alarme.getComplemento());
        myViewHolder.frequencia.setText(Integer.toString(alarme.getFrequenciaHoras()));
        myViewHolder.dias.setText(Integer.toString(alarme.getDuracaoDias()));

    }

<<<<<<< HEAD




=======
>>>>>>> versao-0.2
    @Override
    public int getItemCount() {
        return listaAlarmes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

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
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> versao-0.2
