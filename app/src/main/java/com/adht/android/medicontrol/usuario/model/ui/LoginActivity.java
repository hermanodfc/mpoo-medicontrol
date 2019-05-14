package com.adht.android.medicontrol.usuario.model.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager.LoaderCallbacks;

import android.database.Cursor;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adht.android.medicontrol.R;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText)findViewById(R.id.editTextEmail);
        mPasswordView = (EditText) findViewById(R.id.editTextPassword);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin(false);
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.buttonEnter);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(false);
            }
        });
        TextView mEmailRegister = (TextView) findViewById(R.id.textViewRegister);
        mEmailRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(true);
            }
        });

        mProgressView = findViewById(R.id.progressBarLogin);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(boolean register) {
        if (mUserLoginTask != null) {
            return;
        }

        if (validateFields()) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mUserLoginTask = new UserLoginTask(register);
            mUserLoginTask.execute((Void) null);
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, TaskResult> {

        private final String mEmail;
        private final String mPassword;
        private final boolean mRegister;

        UserLoginTask(boolean register) {
            mEmail = mEmailView.getText().toString();
            mPassword = mPasswordView.getText().toString();
            mRegister = register;
        }

        @Override
        protected TaskResult doInBackground(Void... params) {
            TaskResult result;
            if (mRegister) {
                result = registerUser();
            } else {
                result = loginUser();
            }
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

        private TaskResult registerUser() {
            TaskResult result = TaskResult.SUCCESS;
            try {
                Usuario usuario = new Usuario();
                usuario.setEmail(mEmail);
                usuario.setPassword(mPassword);
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
            Toast.makeText(LoginActivity.this, result.getMsg(), Toast.LENGTH_LONG).show();

            if (!mRegister && result.getType() == TaskResultType.SUCCESS) {
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

