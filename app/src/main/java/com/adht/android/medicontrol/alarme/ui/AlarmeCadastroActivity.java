package com.adht.android.medicontrol.alarme.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.alarme.dominio.Alarme;
import com.adht.android.medicontrol.alarme.negocio.AlarmeServices;
import com.adht.android.medicontrol.infra.ui.TaskResult;
import com.adht.android.medicontrol.infra.ui.TaskResultType;

public class AlarmeCadastroActivity extends AppCompatActivity {

    private EditText nomeView;
    private EditText complementoView;
    private EditText inicioView;
    private EditText frequenciaView;
    private View cadastroAlarmeFormView;
    private View progressBarAlarmCadastro;
    private AlarmeCadastroTask alarmeCadastroTask = null;

    private final AlarmeServices services = new AlarmeServices(); //não sei direito o que faz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme_cadastro);

        cadastroAlarmeFormView = findViewById(R.id.scrollViewAlarmeCadastro);
        progressBarAlarmCadastro = findViewById(R.id.progressBarAlarmeCadastro);
        nomeView = findViewById(R.id.editTextNameAlarme);
        complementoView = findViewById(R.id.editTextComplemento);
        inicioView = findViewById(R.id.editTextInicio);
        frequenciaView = findViewById(R.id.editTextFrequencia);

    }
    private void cadastrar() {
        if (alarmeCadastroTask != null) {
            return;
        }

        if (validateFields()) {
            showProgress(true);
            alarmeCadastroTask = new AlarmeCadastroTask();
            alarmeCadastroTask.execute((Void) null);
        }
    }

    private boolean validateFields() {
        boolean result = true;

        // Reset errors.
        nomeView.setError(null);
        inicioView.setError(null);
        frequenciaView.setError(null);


        // Store values at the time of the register attempt.


        String nome = nomeView.getText().toString();
        String inicio = inicioView.getText().toString();
        String frequencia = frequenciaView.getText().toString();

        View focusView = null;

        // Check for a valid name, if the user entered one.
        if (TextUtils.isEmpty(nome)) {
            nomeView.setError(getString(R.string.nome_incorreto));
            focusView = nomeView;
            result = false;
        }

        // Check for a valid alarm start, if the user entered one.
        if (TextUtils.isEmpty(nome)) {
            inicioView.setError(getString(R.string.nome_incorreto));
            focusView = inicioView;
            result = false;
        }

        // Check for a valid frequency, if the user entered one.
        if (TextUtils.isEmpty(nome)) {
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

    public class AlarmeCadastroTask extends AsyncTask<Void, Void, TaskResult> {

        private final String nome;
        private final String complemento;
        private final int inicio;
        private final int frequencia;

        AlarmeCadastroTask() {
            nome = nomeView.getText().toString();
            complemento = complementoView.getText().toString();
            inicio = inicioView.getInputType();
            frequencia = frequenciaView.getInputType();
        }

        @Override
        protected TaskResult doInBackground(Void... params) {
            TaskResult result = registerAlarme();
            return result;
        }

        private TaskResult registerAlarme() {
            TaskResult result = TaskResult.SUCCESS;
            try {
                Alarme alarme = new Alarme();
                alarme.setNome(nome);
                alarme.setComplemento(complemento);
                alarme.setInicio(inicio);
                alarme.setFrequencia(frequencia);
                services.cadastrar(alarme);
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
            alarmeCadastroTask = null;
            showProgress(false);
        }
    }





    }
