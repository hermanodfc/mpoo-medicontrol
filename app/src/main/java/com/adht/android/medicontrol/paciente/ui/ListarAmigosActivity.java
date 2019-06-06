package com.adht.android.medicontrol.paciente.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.infra.persistencia.AmizadeSemAmigos;
import com.adht.android.medicontrol.paciente.dominio.Amizade;
import com.adht.android.medicontrol.paciente.negocio.AmizadeServices;
import com.adht.android.medicontrol.util.Dialog;
import java.io.IOException;
import java.util.List;

public class ListarAmigosActivity extends AppCompatActivity {
    private RecyclerView recyclerAmigos;

    private List<Amizade> listaAmigos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_amigos);

        recyclerAmigos = findViewById(R.id.recyclerViewAmigos);
        AmizadeServices amizadeServices = new AmizadeServices();

        try {
            listaAmigos = amizadeServices.getAmigos(Sessao.instance.getUsuario().getPaciente());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AmizadeSemAmigos amizadeSemAmigos) {
            final Handler handler = new Handler()
            {

                @Override
                public void handleMessage(Message mesg)
                {
                    throw new RuntimeException();
                }
            };
            android.app.AlertDialog dialog = Dialog.alertDialogOkButton("Amigos",
                    "Você ainda não tem amigos.", this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            handler.handleMessage(handler.obtainMessage());
                        }
                    });
            dialog.show();
            try {
                Looper.loop();
            } catch (RuntimeException runtimeException) { }
           finish();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerAmigos.setLayoutManager(layoutManager);
        recyclerAmigos.setHasFixedSize(true);
        recyclerAmigos.setAdapter(new ListarAmizadeAdapter(listaAmigos));
    }
}

class ListarAmizadeAdapter extends RecyclerView.Adapter<AmigoViewHolder> {

    private final List<Amizade> listaAmizade;

    public ListarAmizadeAdapter(List<Amizade> lista) {
        this.listaAmizade = lista;
    }

    @NonNull
    @Override
    public AmigoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.amigo_fragment, parent, false);
        return new AmigoViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AmigoViewHolder myViewHolder, int position) {
        Amizade amizade = listaAmizade.get(position);
        myViewHolder.nomeAmigo.setText(amizade.getAmigo().getNome());

        String status = null;

        switch (amizade.getStatusAmizade()) {
            case ENVIADO_ACEITO:
            case RECEBIDO_ACEITO:
                status = "Amigos";
                break;
            case ENVIADO_PENDENTE:
            case RECEBIDO_PENDENTE:
                status = "Aguardando confirmação";
                break;
        }

        myViewHolder.statusAmizade.setText(status);

    }

    @Override
    public int getItemCount() {
        return listaAmizade.size();
    }
}

class AmigoViewHolder extends RecyclerView.ViewHolder{

    TextView nomeAmigo;
    TextView statusAmizade;

    public AmigoViewHolder(@NonNull View itemView) {
        super(itemView);

        nomeAmigo = itemView.findViewById(R.id.amigoNome);
        statusAmizade = itemView.findViewById(R.id.textViewStatusAmizade);
    }
}
