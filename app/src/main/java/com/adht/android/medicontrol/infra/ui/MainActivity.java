package com.adht.android.medicontrol.infra.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.adht.android.medicontrol.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.adht.android.medicontrol.alarme.ui.AlarmeCadastroActivity;
<<<<<<< HEAD
=======
//import com.adht.android.medicontrol.alarme.ui.AlarmesListaActivity;
>>>>>>> versao-0.2
import com.adht.android.medicontrol.alarme.ui.AlarmesListaActivity;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.paciente.ui.AdicionarAmigoActivity;
import com.adht.android.medicontrol.paciente.ui.ListarAmigosActivity;
import com.adht.android.medicontrol.usuario.dominio.Usuario;
import com.adht.android.medicontrol.usuario.negocio.UsuarioServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.adht.android.medicontrol.usuario.ui.LoginActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        TextView headerEmailView = (TextView)findViewById(R.id.header_email_view);
        TextView headerNameView = (TextView)findViewById(R.id.textViewHeaderName);
        ImageView imageView = (ImageView)findViewById(R.id.imageViewFoto);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Usuario usuario = Sessao.instance.getUsuario();
        headerEmailView.setText(usuario.getEmail());
        headerNameView.setText(usuario.getPaciente().getNome());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_alarme) {
            startActivity(new Intent(this, AlarmeCadastroActivity.class));
            //Abre a atividade de cadastro de alarme
        } else if (id == R.id.nav_lista_alarmes) {
            startActivity(new Intent(this, AlarmesListaActivity.class));
        } else if (id == R.id.nav_add_amigo) {
            intent = new Intent(MainActivity.this, AdicionarAmigoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lista_amigos) {
            intent = new Intent(MainActivity.this, ListarAmigosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_exit) {
            UsuarioServices services = new UsuarioServices();
            services.logout();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}