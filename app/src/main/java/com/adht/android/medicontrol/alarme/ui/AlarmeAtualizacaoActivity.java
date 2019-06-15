package com.adht.android.medicontrol.alarme.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.alarme.negocio.AlarmeServices;
import com.adht.android.medicontrol.infra.ui.MainActivity;
import com.adht.android.medicontrol.infra.ui.TaskResult;
import com.adht.android.medicontrol.infra.ui.TaskResultType;

public class AlarmeAtualizacaoActivity extends AppCompatActivity {

    private EditText nomeView;
    private EditText complementoView;
    private EditText inicioView;
    private EditText frequenciaView;
    private EditText diasView;
    private View cadastroAlarmeFormView;
    private View progressBarAlarmCadastro;

    private AlarmeAtualizacaoTask alarmeAtualizacaoTask = null;

    private final AlarmeServices services = new AlarmeServices();

    private long idAlarme;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme_atualizacao);

        Intent intent = getIntent();
        idAlarme = intent.getLongExtra("ALARME_ID", 0);

        Alarme alarme = services.getAlarme(idAlarme);



        cadastroAlarmeFormView = findViewById(R.id.scrollViewAlarmeCadastro);
        progressBarAlarmCadastro = findViewById(R.id.progressBarAlarmeCadastro);
        nomeView = findViewById(R.id.editTextNameAlarme);
        complementoView = findViewById(R.id.editTextComplemento);
        inicioView = findViewById(R.id.editTextInicio);
        frequenciaView = findViewById(R.id.editTextFrequencia);
        diasView = findViewById(R.id.editTextDias);

        nomeView.setText(alarme.getNomeMedicamento());
        complementoView.setText(alarme.getComplemento());
        frequenciaView.setText(Integer.toString(alarme.getFrequenciaHoras()));
        diasView.setText(Integer.toString(alarme.getDuracaoDias()));
        inicioView.setText(alarme.getHorarioInicial());



        Button buttonAtualizar = findViewById(R.id.buttonAtualizar);
        Button buttonCancelar = findViewById(R.id.buttonCancelAtualizacao);

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlarmeAtualizacaoActivity.this, MainActivity.class));
            }
        });

        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizar();

            }
        });

    }

    private void atualizar() {
        if (alarmeAtualizacaoTask != null) {
            return;
        }

        if (validateFields()) {
            showProgress(true);
            alarmeAtualizacaoTask = new AlarmeAtualizacaoTask();
            alarmeAtualizacaoTask.execute((Void) null);
        }

    }

    private boolean validateFields() {
        boolean result = true;

        // Reset errors.
        nomeView.setError(null);
        inicioView.setError(null);
        frequenciaView.setError(null);
        diasView.setError(null);


        // Store values at the time of the register attempt.


        String nome = nomeView.getText().toString();
        String inicio = inicioView.getText().toString();
        String frequencia = frequenciaView.getText().toString();
        String dias = diasView.getText().toString();

        View focusView = null;

        // Check for a valid name, if the user entered one.
        if (TextUtils.isEmpty(nome)) {
            nomeView.setError(getString(R.string.nome_incorreto));
            focusView = nomeView;
            result = false;
        }

        // Check for a valid alarm start, if the user entered one.
        if (TextUtils.isEmpty(inicio)) {
            inicioView.setError(getString(R.string.nome_incorreto));
            focusView = inicioView;
            result = false;
        }

        // Check for a valid frequency, if the user entered one.
        if (TextUtils.isEmpty(dias)) {
            diasView.setError(getString(R.string.nome_incorreto));
            focusView = diasView;
            result = false;
        }

        // Check for a valid frequency, if the user entered one.
        if (TextUtils.isEmpty(frequencia)) {
            frequenciaView.setError(getString(R.string.nome_incorreto));
            focusView = frequenciaView;
            result = false;
        }


        if (!result) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        return result;
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            cadastroAlarmeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            cadastroAlarmeFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cadastroAlarmeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBarAlarmCadastro.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBarAlarmCadastro.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBarAlarmCadastro.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBarAlarmCadastro.setVisibility(show ? View.VISIBLE : View.GONE);
            cadastroAlarmeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }

    public void showAlertDialogButtonClicked(String titulo, String mensagem, final Handler handler) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);

        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handler.sendMessage(handler.obtainMessage());
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class AlarmeAtualizacaoTask extends AsyncTask<Void, Void, TaskResult> {

        private final String nome;
        private final String complemento;
        private final String inicio;
        private final int dias;
        private final int frequencia;

        AlarmeAtualizacaoTask() {
            nome = nomeView.getText().toString();
            complemento = complementoView.getText().toString();
            inicio = inicioView.getText().toString();
            dias = Integer.parseInt(diasView.getText().toString());
            frequencia = Integer.parseInt(frequenciaView.getText().toString());
        }

        @Override
        protected TaskResult doInBackground(Void... params) {
            TaskResult result = atualizarAlarme();
            return result;
        }

        private TaskResult atualizarAlarme() {
            TaskResult result = TaskResult.SUCCESS;

            try {
                Alarme alarme = new Alarme();
                alarme.setNomeMedicamento(nome);
                alarme.setComplemento(complemento);
                alarme.setHorarioInicial(inicio);
                alarme.setDuracaoDias(dias);
                alarme.setFrequenciaHoras(frequencia);
                services.atualizar(alarme, idAlarme);
            } catch (Exception e) {
                result = new TaskResult(TaskResultType.FAIL, e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(final TaskResult result) {
            if (result == null) {
                resetTask();
                return;
            }

            if (result.getType() == TaskResultType.FAIL) {
                nomeView.requestFocus();
            }

            //Toast.makeText(CadastroActivity.this, result.getMsg(), Toast.LENGTH_LONG).show();
            final Handler handler = new Handler()
            {

                @Override
                public void handleMessage(Message mesg)
                {
                    throw new RuntimeException();
                }
            };


            showAlertDialogButtonClicked("Atenção", result.getMsg(), handler);

            try{ Looper.loop(); }
            catch(RuntimeException e){}

            if (result.getType() == TaskResultType.SUCCESS) {
                finish();
            }
            resetTask();
        }

        @Override
        protected void onCancelled() {
            resetTask();
        }

        private void resetTask() {
            alarmeAtualizacaoTask = null;
            showProgress(false);

            startActivity(new Intent(AlarmeAtualizacaoActivity.this, MainActivity.class));
        }
    }
}
