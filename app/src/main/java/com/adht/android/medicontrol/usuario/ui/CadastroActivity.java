package com.adht.android.medicontrol.usuario.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adht.android.medicontrol.R;
import com.adht.android.medicontrol.infra.ui.TaskResult;
import com.adht.android.medicontrol.infra.ui.TaskResultType;
import com.adht.android.medicontrol.usuario.dominio.Sexo;
import com.adht.android.medicontrol.usuario.dominio.Usuario;
import com.adht.android.medicontrol.usuario.negocio.UsuarioServices;
import com.adht.android.medicontrol.util.EmailValidator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CadastroActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText mNascimentoView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private RadioButton mMasculinoView;
    private RadioButton mFemininoView;
    private RadioGroup mRadioGroupGenero;
    private EditText mNomeView;
    private RadioButton mLastRadioButton;
    private View mCadastroFormView;
    private View mProgressView;
    private UserRegisterTask mUserRegisterTask = null;

    private final UsuarioServices services = new UsuarioServices();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mCadastroFormView = findViewById(R.id.scrollViewCadastro);
        Button mButtonCancel = findViewById(R.id.buttonCancel);
        Button mButtonCadastrar = findViewById(R.id.buttonRegister);
        mButtonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
        mNascimentoView = (EditText) findViewById(R.id.editTextNascimento);
        mEmailView = (EditText) findViewById(R.id.editTextEmail);
        mPasswordView = (EditText) findViewById(R.id.editTextPassword);
        mMasculinoView = (RadioButton) findViewById(R.id.radioButtonMale);
        mFemininoView = (RadioButton) findViewById(R.id.radioButtonFemale);
        mRadioGroupGenero = (RadioGroup) findViewById(R.id.radioGroupGenero);
        mRadioGroupGenero.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mLastRadioButton.setError(null);
            }
        });
        mNomeView = (EditText) findViewById(R.id.editTextName);
        mProgressView = findViewById(R.id.progressBarCadastro);
        int ultimo = mRadioGroupGenero.getChildCount() - 1;
        mLastRadioButton = ((RadioButton)mRadioGroupGenero.getChildAt(ultimo));
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mNascimentoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

//        mNascimentoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    showDatePicker();
//                }
//            }
//        });
    }

    private void cadastrar() {
        if (mUserRegisterTask != null) {
            return;
        }

        if (validateFields()) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mUserRegisterTask = new UserRegisterTask();
            mUserRegisterTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }


    private boolean validateFields() {
        boolean result = true;

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mLastRadioButton.setError(null);
        mNascimentoView.setError(null);
        mNomeView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String nascimento = mNascimentoView.getText().toString();
        String nome = mNomeView.getText().toString();

        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            result = false;
        }

        EmailValidator emailValidator = new EmailValidator();

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            result = false;
        } else if (!emailValidator.isValidEmail(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            result = false;
        }

        if (TextUtils.isEmpty(nascimento)) {
            mNascimentoView.setError(getString(R.string.error_field_required));
            focusView = mNascimentoView;
            result = false;
        }

        if (mRadioGroupGenero.getCheckedRadioButtonId() == -1){

            mLastRadioButton.setError(getString(R.string.error_field_required));
            focusView = mLastRadioButton;
            result = false;
        }

        if (TextUtils.isEmpty(nome)) {
            mNomeView.setError(getString(R.string.error_field_required));
            focusView = mNomeView;
            result = false;
        }

        if (!result) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        return result;
    }

    private void showDatePicker() {
        Calendar calendario = Calendar.getInstance();
        DatePickerDialog pickerDialog = new DatePickerDialog(
                this,
                this,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );
        pickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        pickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1;
        mNascimentoView.setText((day < 10 ? "0" : "") + day + "/" + (month < 10 ? "0" : "") + month + "/" + year);
        mNascimentoView.setError(null);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCadastroFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCadastroFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCadastroFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCadastroFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    public class UserRegisterTask extends AsyncTask<Void, Void, TaskResult> {

        private final String mEmail;
        private final String mPassword;
        private final String mNome;
        private Sexo mGenero;
        private GregorianCalendar mNascimento;

        UserRegisterTask() {
            mEmail = mEmailView.getText().toString();
            mPassword = mPasswordView.getText().toString();
            mNome = mNomeView.getText().toString();
            if (mFemininoView.isChecked()) {
                mGenero = Sexo.FEMININO;
            } else {
                mGenero = Sexo.MASCULINO;
            }

            String nascimentoString = mNascimentoView.getText().toString();
            int dia = Integer.parseInt(nascimentoString.substring(0, 2));
            int mes = Integer.parseInt(nascimentoString.substring(3, 5)) - 1;
            int ano = Integer.parseInt(nascimentoString.substring(7));
            mNascimento = new GregorianCalendar(ano, mes, dia);
        }

        @Override
        protected TaskResult doInBackground(Void... params) {
            TaskResult result = registerUser();
            return result;
        }

        private TaskResult registerUser() {
            TaskResult result = TaskResult.SUCCESS;
            try {
                Usuario usuario = new Usuario();
                usuario.setEmail(mEmail);
                usuario.setSenha(mPassword);
                usuario.setNome(mNome);
                usuario.setSexo(mGenero);
                usuario.setNascimento(mNascimento);
                services.cadastrar(usuario);
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
                mEmailView.requestFocus();
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
            mUserRegisterTask = null;
            showProgress(false);
        }
    }
}
