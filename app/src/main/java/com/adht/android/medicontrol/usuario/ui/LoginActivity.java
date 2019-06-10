package com.adht.android.medicontrol.usuario.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import com.adht.android.medicontrol.infra.ui.MainActivity;
import com.adht.android.medicontrol.infra.ui.TaskResult;
import com.adht.android.medicontrol.infra.ui.TaskResultType;
import com.adht.android.medicontrol.usuario.negocio.UsuarioServices;
import com.adht.android.medicontrol.util.EmailValidator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adht.android.medicontrol.R;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mUserLoginTask = null;;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private final UsuarioServices services = new UsuarioServices();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText)findViewById(R.id.editTextEmail);
        mPasswordView = (EditText) findViewById(R.id.editTextPassword);
        mLoginFormView = findViewById(R.id.scrollViewForm);
        mProgressView = findViewById(R.id.progressBar);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mButtonEnter = (Button) findViewById(R.id.buttonEnter);
        mButtonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        TextView mEmailRegister = (TextView) findViewById(R.id.textViewRegister);
        mEmailRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mUserLoginTask != null) {
            return;
        }

        if (validateFields()) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mUserLoginTask = new UserLoginTask();
            mUserLoginTask.execute((Void) null);
        }
    }

    private void cadastrarUsuario() {
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        mEmailView.setText("");
        mPasswordView.setText("");
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean validateFields() {
        boolean result = true;

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

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

        if (!result) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        return result;
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, TaskResult> {

        private final String mEmail;
        private final String mPassword;


        UserLoginTask() {
            mEmail = mEmailView.getText().toString();
            mPassword = mPasswordView.getText().toString();
        }

        @Override
        protected TaskResult doInBackground(Void... params) {
            TaskResult result;
            result = loginUser();
            return result;
        }

        private TaskResult loginUser() {
            TaskResult result = TaskResult.SUCCESS;
            try {
                services.login(mEmail, mPassword);
            } catch (Exception e) {
                e.printStackTrace();
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
                Toast.makeText(LoginActivity.this, "Usuário ou senha inválidos.", Toast.LENGTH_LONG).show();
            }

            if (result.getType() == TaskResultType.SUCCESS) {
                LoginActivity.this.finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            resetTask();
        }

        @Override
        protected void onCancelled() {
            resetTask();
        }

        private void resetTask() {
            mUserLoginTask = null;
            showProgress(false);
        }
    }

}

