package com.adht.android.medicontrol.paciente.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.infra.exception.MediControlException;
import com.adht.android.medicontrol.infra.Sessao;
import com.adht.android.medicontrol.paciente.dominio.Amizade;
import com.adht.android.medicontrol.paciente.dominio.Paciente;
import com.adht.android.medicontrol.paciente.dominio.StatusAmizade;
import com.adht.android.medicontrol.paciente.negocio.AmizadeServices;
import com.adht.android.medicontrol.paciente.negocio.PacienteServices;
import com.adht.android.medicontrol.util.Dialog;
import com.adht.android.medicontrol.util.EmailValidator;

public class AdicionarAmigoActivity extends AppCompatActivity {

    private EditText emailView;
    private Button buscar;
    private final PacienteServices pacienteServices = new PacienteServices();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adicionar_amigo);

        emailView = (EditText) findViewById(R.id.editTextEmail);
        buscar = (Button) findViewById(R.id.buttonBuscarAmigo);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarAmigo();
            }
        });

        emailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                habilitarBotao();
            }
        });

    }

    private void habilitarBotao() {
        boolean result = false;

        if (dadosValidos()) {
            result = true;
        }

        buscar.setEnabled(result);
    }

    private void adicionarAmigo() {

        String email = emailView.getText().toString();

        Paciente paciente;

        final Handler handler = new Handler()
        {

            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        try {
            paciente = pacienteServices.getPaciente(email);
        } catch (MediControlException exception) {
            paciente = null;
            AlertDialog dialog = Dialog.alertDialogOkButton("Adicionar Amigo",
                    "Amigo não localizado.", this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            handler.handleMessage(handler.obtainMessage());
                        }
                    });
            dialog.show();
            try {
                Looper.loop();
            } catch (RuntimeException runtimeException) { }
            emailView.requestFocus();
        }

        if (paciente != null) {
            final Paciente amigo = paciente;
            AlertDialog dialog = Dialog.alertDialoOkCancelButton("Adicionar Amigo",
                    "Deseja enviar pedido de amizade?\n\nNome: " + amigo.getNome()
                    + "\nEmail: " + email, this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            enviarPedido(amigo);
                            handler.handleMessage(handler.obtainMessage());
                        }
                    }, new DialogInterface.OnClickListener() {
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
    }

    private void enviarPedido(Paciente amigo) {
        AmizadeServices amizadeServices = new AmizadeServices();
        Amizade amizade = new Amizade();
        amizade.setAmigo(amigo);
        amizade.setStatusAmizade(StatusAmizade.ENVIADO_PENDENTE);
        Paciente usuarioPaciente = Sessao.instance.getUsuario().getPaciente();

        try {
            amizadeServices.cadastrarPedidoAmizade(usuarioPaciente, amizade);
        } catch (MediControlException e) {
            e.printStackTrace();
        }

        usuarioPaciente.adicionarAmizade(amizade);
    }

    private boolean dadosValidos() {

        boolean result = true;

        String email = emailView.getText().toString();

        EmailValidator emailValidator = new EmailValidator();

        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            result = false;
        } else if (!emailValidator.isValidEmail(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            result = false;
        } else if (email.equals(Sessao.instance.getUsuario().getEmail())) {
            emailView.setError(getString(R.string.error_invalid_email));
            result = false;
        }

        if (!result) {
            emailView.requestFocus();
        } else {
            emailView.setError(null);
        }

        return result;
    }
}
