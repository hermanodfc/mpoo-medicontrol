package com.adht.android.medicontrol.alarme.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AlarmeCadastroActivity extends AppCompatActivity {

    private EditText nomeView;
    private EditText complementoView;
    private EditText inicioView;
    private EditText frequenciaView;
    private EditText diasView;
    private View cadastroAlarmeFormView;
    private View progressBarAlarmCadastro;
    private AlarmeCadastroTask alarmeCadastroTask = null;

    private final AlarmeServices services = new AlarmeServices();

    public static int broadcastCode=0;

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
        diasView = findViewById(R.id.editTextDias);

        Button buttonCadastrar = findViewById(R.id.buttonRegister);
        Button buttonCancelar = findViewById(R.id.buttonCancel);

        //chamando o cadastrar com o botão
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
                broadcastCode ++;

            }
        });

        //chamando o cancelar com o botão
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(AlarmeCadastroActivity.this, MainActivity.class));

            }
        });

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

    public class AlarmeCadastroTask extends AsyncTask<Void, Void, TaskResult> {

        private final String nome;
        private final String complemento;
        private final String inicio;
        private final int dias;
        private final int frequencia;


        AlarmeCadastroTask() {
            nome = nomeView.getText().toString();
            complemento = complementoView.getText().toString();
            inicio = inicioView.getText().toString();
            dias = Integer.parseInt(diasView.getText().toString());
            frequencia = Integer.parseInt(frequenciaView.getText().toString());
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
                alarme.setNomeMedicamento(nome);
                alarme.setComplemento(complemento);
                alarme.setHorarioInicial(inicio);
                alarme.setDuracaoDias(dias);
                alarme.setFrequenciaHoras(frequencia);
                alarme.setRequestCode(broadcastCode);
                services.cadastrar(alarme);
                //configurando hora
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String diaString = format.format(Calendar.getInstance().getTime());
                Date data = format.parse(diaString);
                long dia = data.getTime();
                long hora = new Integer(inicio.substring(0, 2)) * 3600000; //1 hora = 3.600.000 milisegundos
                long minuto = Integer.parseInt(inicio.substring(3)) * 60000; //1 minuto = 60.000 milisegundos
                long milis = dia + hora + minuto;
                Intent intent = new Intent(AlarmeCadastroActivity.this, Alarm.class);

                intent.putExtra("ALARME_NOME", alarme.getNomeMedicamento());
                intent.putExtra("ALARME_REQUEST", alarme.getRequestCode());

                PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(),broadcastCode, intent,0);
                AlarmManager a = (AlarmManager)getSystemService(ALARM_SERVICE);
                /*String array[] = new String [2];
                array[0] = alarme.getNomeMedicamento();
                array[1] = Integer.toString(alarme.getRequestCode());
                intent.putExtra("ALARME_INFO", array);*/


                //a.setRepeating(AlarmManager.RTC_WAKEUP, milis, 10000, p1);
                a.setExact(AlarmManager.RTC_WAKEUP,milis, p1);
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
            startActivity(new Intent(AlarmeCadastroActivity.this, MainActivity.class));
        }
    }





}