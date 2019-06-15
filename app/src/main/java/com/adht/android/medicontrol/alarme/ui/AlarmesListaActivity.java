package com.adht.android.medicontrol.alarme.ui;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.alarme.negocio.AlarmeServices;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlarmesListaActivity extends AppCompatActivity {
    private AlarmeAdapter alarmeAdapter;
    private RecyclerView recyclerViewAlarmes;
    private List<Alarme> listaAlarme = new ArrayList<Alarme>();

    Usuario usuario = Sessao.INSTANCE.getUsuario();
    long idPaciente = usuario.getPaciente().getId();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmes_lista);

        recyclerViewAlarmes = findViewById(R.id.recyclerViewAlarmes);


        AlarmeServices alarmeServices = new AlarmeServices();
        try {
            listaAlarme = alarmeServices.listar(idPaciente);
        } catch (MediControlException e) {
            e.printStackTrace();
        }
        setUpRecyclerView();


    }




    private void setUpRecyclerView() {
        recyclerViewAlarmes.setHasFixedSize(true);
        alarmeAdapter = new AlarmeAdapter(listaAlarme);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeCallback());
        itemTouchHelper.attachToRecyclerView(recyclerViewAlarmes);
        recyclerViewAlarmes.addItemDecoration(new ItemDecorator());
        recyclerViewAlarmes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAlarmes.setAdapter(alarmeAdapter);
        //novo swipe
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(new SwipeCallbackRight());
        itemTouchHelper2.attachToRecyclerView(recyclerViewAlarmes);
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

    public class AlarmeAdapter extends RecyclerView.Adapter<AlarmeAdapter.MyViewHolder> implements Filterable {

        private List<Alarme> listaAlarmes;
        private List<Alarme> listaAlarmesFull;
        final List<Alarme> itemsPendingRemoval = new ArrayList<>();
        private Handler handler = new Handler();
        HashMap<Alarme, Runnable> pendingRunnables = new HashMap<>();
        private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
        boolean undoOn;

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

        public void setUndoOn(boolean undoOn) {
            this.undoOn = undoOn;
        }

        public boolean isUndoOn() {
            return undoOn;
        }

        public boolean isPendingRemoval(int position) {
            Alarme item = listaAlarme.get(position);
            return itemsPendingRemoval.contains(item);
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

        public void pendingRemoval(int position) {
            final Alarme item = listaAlarme.get(position);
            if (!itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.add(item);
                // this will redraw row in "undo" state
                notifyItemChanged(position);
                // let's create, store and post a runnable to remove the item
                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        remove(listaAlarmes.indexOf(item));
                    }
                };
                handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
                pendingRunnables.put(item, pendingRemovalRunnable);
            }
        }

        public void remove(int position) {
            Alarme alarme = listaAlarmes.get(position);
            if (itemsPendingRemoval.contains(alarme)) {
                itemsPendingRemoval.remove(alarme);
            }
            if (listaAlarmes.contains(alarme)) {
                listaAlarmes.remove(position);
                AlarmeServices alarmeServices = new AlarmeServices();
                alarmeServices.deletar(alarme.getId());
                notifyItemRemoved(position);
            }
        }



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
            xMark = ContextCompat.getDrawable(AlarmesListaActivity.this, R.drawable.ic_clear_24px);
            xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            xMarkMargin = (int) AlarmesListaActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
            initiated = true;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int position = viewHolder.getAdapterPosition();
            AlarmeAdapter adapter = (AlarmeAdapter) recyclerView.getAdapter();
            if (adapter.isUndoOn() && adapter.isPendingRemoval(position)) {
                return 0;
            }
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int swipedPosition = viewHolder.getAdapterPosition();
            AlarmeAdapter adapter = (AlarmeAdapter)recyclerViewAlarmes.getAdapter();
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

    class SwipeCallbackRight extends ItemTouchHelper.SimpleCallback{


        public SwipeCallbackRight() {
            super(0, ItemTouchHelper.RIGHT);
        }

        Drawable background;
        Drawable xMark;
        int xMarkMargin;
        boolean initiated;

        private void init() {
            background = new ColorDrawable(Color.RED);
            xMark = ContextCompat.getDrawable(AlarmesListaActivity.this, R.drawable.ic_menu_manage);
            xMark.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            xMarkMargin = (int) AlarmesListaActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
            initiated = true;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            long idAlarme;
            Alarme alarme = listaAlarme.get(position);
            idAlarme = alarme.getId();
            finish();
            Intent intent = new Intent(AlarmesListaActivity.this, AlarmeAtualizacaoActivity.class);
            intent.putExtra("ALARME_ID", idAlarme);
            startActivity(intent);


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
