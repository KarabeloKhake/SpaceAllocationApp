package com.example.spaceallocation.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.spaceallocation.R;
import com.example.spaceallocation.app_utilities.AppClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ResetPassword extends AppCompatActivity {

    ActionBar actionBar;
    private View mProgressView;
    private View mRecoverPasswordFormView;
    private TextView tvLoad;
    TextInputEditText etRecoveryEmail;
    TextInputLayout ilRecoveryEmail;
    TextView tvRecoveryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("\t\t\t\t\t\t\tReset Password");

        mRecoverPasswordFormView = findViewById(R.id.layout_form);
        mProgressView = findViewById(R.id.progress_bar);
        tvLoad = findViewById(R.id.tvLoad);
        etRecoveryEmail = findViewById(R.id.etRecoveryEmail);
        ilRecoveryEmail = findViewById(R.id.ilRecoveryEmail);
        tvRecoveryText = findViewById(R.id.tvRecoveryText);

        etRecoveryEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateEmailAddress(((EditText) v).getText());
                } //end if
            }
        });
    } //end onCreate()

    //Custom Methods
    public void btnReset_onClick(View view) {
        /*
            the purpose of this method is to reset the password of a specific
            registered user
        */
        String sRecoveryEmail;

        try {
            //check if input text is empty
            if(!Objects.requireNonNull(etRecoveryEmail.getText()).toString().isEmpty()) {
                //check if the email address entered is a valid email address
                if(AppClass.isEmailValid(etRecoveryEmail.getText().toString())) {
                    ilRecoveryEmail.setError(null);
                    sRecoveryEmail = etRecoveryEmail.getText().toString();

                    //send reset link to the provided email address
                    showProgress(true);
                    tvLoad.setText(R.string.text_reset_link);
                    Backendless.UserService.restorePassword(sRecoveryEmail, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {
                            showProgress(false);
                            Toast.makeText(ResetPassword.this, "Reset link successfully sent to your email address!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPassword.this, Login.class));
                            finish();
                        } //end handleResponse()
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            showProgress(false);
                            Toast.makeText(ResetPassword.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        } //end handleFault()
                    });
                } //end if
                else
                    ilRecoveryEmail.setError("Email address not valid");
            } //end if
            else
                ilRecoveryEmail.setError("Email address required");
        } //end try
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } //end catch()
    } //end btnReset()

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRecoverPasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mRecoverPasswordFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRecoverPasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    } //end showProgress()

    public void validateEmailAddress(Editable email) {
        //check if input text is empty
        if(!TextUtils.isEmpty(email)) {
            //check if the email address entered is a valid email address
            if(AppClass.isEmailValid(Objects.requireNonNull(etRecoveryEmail.getText()).toString()))
                ilRecoveryEmail.setError(null);
            else
                ilRecoveryEmail.setError("Email address not valid");
        }
        else
            ilRecoveryEmail.setError("Email address required");
    } //end validateEmail()
} //end class RecoverPassword
