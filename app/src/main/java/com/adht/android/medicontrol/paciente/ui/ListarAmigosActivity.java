package com.adht.android.medicontrol.paciente.ui;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.infra.persistencia.AmizadeSemAmigos;
import com.adht.android.medicontrol.paciente.dominio.Amizade;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.paciente.dominio.StatusAmizade;
import com.adht.android.medicontrol.paciente.negocio.AmizadeServices;
import com.adht.android.medicontrol.util.Dialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
            listaAmigos = amizadeServices.getAmigos(Sessao.INSTANCE.getUsuario().getPaciente());
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

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        recyclerAmigos.setLayoutManager(new LinearLayoutManager(this));
        recyclerAmigos.setAdapter(new ListarAmizadeAdapter(listaAmigos));
        recyclerAmigos.setHasFixedSize(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeCallback());
        itemTouchHelper.attachToRecyclerView(recyclerAmigos);
        recyclerAmigos.addItemDecoration(new ItemDecorator());
    }

    class ItemDecorator extends RecyclerView.ItemDecoration {

        // we want to cache this and not allocate anything repeatedly in the onDraw method
        Drawable background;
        boolean initiated;

        private void init() {
            background = new ColorDrawable(Color.RED);
            initiated = true;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            if (!initiated) {
                init();
            }

            // only if animation is in progress
            if (parent.getItemAnimator().isRunning()) {

                // some items might be animating down and some items might be animating up to close the gap left by the removed item
                // this is not exclusive, both movement can be happening at the same time
                // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                // then remove one from the middle

                // find first child with translationY > 0
                // and last one with translationY < 0
                // we're after a rect that is not covered in recycler-view views at this point in time
                View lastViewComingDown = null;
                View firstViewComingUp = null;

                // this is fixed
                int left = 0;
                int right = parent.getWidth();

                // this we need to find out
                int top = 0;
                int bottom = 0;

                // find relevant translating views
                int childCount = parent.getLayoutManager().getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getLayoutManager().getChildAt(i);
                    if (child.getTranslationY() < 0) {
                        // view is coming down
                        lastViewComingDown = child;
                    } else if (child.getTranslationY() > 0) {
                        // view is coming up
                        if (firstViewComingUp == null) {
                            firstViewComingUp = child;
                        }
                    }
                }

                if (lastViewComingDown != null && firstViewComingUp != null) {
                    // views are coming down AND going up to fill the void
                    top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                    bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                } else if (lastViewComingDown != null) {
                    // views are going down to fill the void
                    top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                    bottom = lastViewComingDown.getBottom();
                } else if (firstViewComingUp != null) {
                    // views are coming up to fill the void
                    top = firstViewComingUp.getTop();
                    bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                }

                background.setBounds(left, top, right, bottom);
                background.draw(c);

            }
            super.onDraw(c, parent, state);
        }
    }

    class ListarAmizadeAdapter extends RecyclerView.Adapter<AmigoViewHolder> {

        final List<Amizade> listaAmizade;
        final List<Amizade> itemsPendingRemoval = new ArrayList<>();
        private Handler handler = new Handler();
        HashMap<Amizade, Runnable> pendingRunnables = new HashMap<>();
        private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
        int lastInsertedIndex;
        boolean undoOn;
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
        public void onBindViewHolder(@NonNull AmigoViewHolder amigoViewHolder, int position) {
            amigoViewHolder.setAmizade(listaAmigos.get(position));
            amigoViewHolder.bind();
        }

        @Override
        public int getItemCount() {
            return listaAmizade.size();
        }

        public void setUndoOn(boolean undoOn) {
            this.undoOn = undoOn;
        }

        public boolean isUndoOn() {
            return undoOn;
        }

        public boolean isPendingRemoval(int position) {
            Amizade item = listaAmigos.get(position);
            return itemsPendingRemoval.contains(item);
        }

        public void pendingRemoval(int position) {
            final Amizade item = listaAmizade.get(position);
            if (!itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.add(item);
                // this will redraw row in "undo" state
                notifyItemChanged(position);
                // let's create, store and post a runnable to remove the item
                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        remove(listaAmizade.indexOf(item));
                    }
                };
                handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
                pendingRunnables.put(item, pendingRemovalRunnable);
            }
        }

        public void remove(int position) {
            Amizade amizade = listaAmizade.get(position);
            if (itemsPendingRemoval.contains(amizade)) {
                itemsPendingRemoval.remove(amizade);
            }
            if (listaAmizade.contains(amizade)) {
                listaAmizade.remove(position);
                AmizadeServices amizadeServices = new AmizadeServices();
                try {
                    amizadeServices.desfazerAmizade(amizade);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notifyItemRemoved(position);
            }
        }
    }

    class AmigoViewHolder extends RecyclerView.ViewHolder{

        private Amizade amizade;
        private TextView textViewNomeAmigo;
        private TextView textViewStatusAmizade;
        private ImageButton buttonAdicionar;

        public AmigoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeAmigo = itemView.findViewById(R.id.amigoNome);
            textViewStatusAmizade = itemView.findViewById(R.id.textViewStatusAmizade);
            buttonAdicionar = itemView.findViewById(R.id.buttonAceitar);
            buttonAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aceitarPedido();
                }
            });
        }

        private void aceitarPedido() {
            AmizadeServices amizadeServices = new AmizadeServices();
            amizade.setStatusAmizade(StatusAmizade.ACEITO);
            amizadeServices.atualizar(amizade);
            textViewStatusAmizade.setText(textoStatus());
            buttonAdicionar.setVisibility(View.INVISIBLE);
        }

        public void setAmizade(Amizade amizade) {
            this.amizade = amizade;
        }

        public void bind() {
            boolean usuarioConvidado = isUsuarioConvidado();
            Paciente amigo = (usuarioConvidado) ? amizade.getSolicitante() : amizade.getConvidado();
            textViewNomeAmigo.setText(amigo.getNome());
            textViewStatusAmizade.setText(textoStatus());
            buttonAdicionar.setVisibility(visibilidadeBotao(usuarioConvidado));
        }

        private int visibilidadeBotao(boolean usuarioConvidado) {
            int result = View.INVISIBLE;

            if (usuarioConvidado && amizade.getStatus() == StatusAmizade.PENDENTE) {
                result = View.VISIBLE;
            }

            return result;
        }

        private boolean isUsuarioConvidado() {
            boolean result = false;

            if (Sessao.INSTANCE.getUsuario().getPaciente().getId() == amizade.getConvidado().getId()) {
                result = true;
            }

            return result;
        }

        private String textoStatus() {
            String status = null;

            switch (amizade.getStatus()) {
                case ACEITO:
                    status = "Amigos";
                    break;
                case PENDENTE:
                    status = "Aguardando confirmação";
                    break;
            }
            return status;
        }
    }

    class SwipeCallback extends ItemTouchHelper.SimpleCallback {

        public SwipeCallback() {
            super (0, ItemTouchHelper.LEFT);
        }

        Drawable background;
        Drawable xMark;
        int xMarkMargin;
        boolean initiated;

        private void init() {
            background = new ColorDrawable(Color.RED);
            xMark = ContextCompat.getDrawable(ListarAmigosActivity.this, R.drawable.ic_clear_24px);
            xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            xMarkMargin = (int) ListarAmigosActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
            initiated = true;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int position = viewHolder.getAdapterPosition();
            ListarAmizadeAdapter adapter = (ListarAmizadeAdapter) recyclerView.getAdapter();
            if (adapter.isUndoOn() && adapter.isPendingRemoval(position)) {
                return 0;
            }
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int swipedPosition = viewHolder.getAdapterPosition();
            ListarAmizadeAdapter adapter = (ListarAmizadeAdapter)recyclerAmigos.getAdapter();
            boolean undoOn = adapter.isUndoOn();
            if (undoOn) {
                adapter.pendingRemoval(swipedPosition);
            } else {
                adapter.remove(swipedPosition);
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;

            if (viewHolder.getAdapterPosition() == -1) {
                return;
            }

            if (!initiated) {
                init();
            }

            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(c);

            int itemHeight = itemView.getBottom() - itemView.getTop();
            int intrinsicWidth = xMark.getIntrinsicWidth();
            int intrinsicHeight = xMark.getIntrinsicWidth();

            int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
            int xMarkRight = itemView.getRight() - xMarkMargin;
            int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
            int xMarkBottom = xMarkTop + intrinsicHeight;
            xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

            xMark.draw(c);

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }
}


