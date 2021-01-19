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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.spaceallocation.R;
import com.example.spaceallocation.activities.student.StudentHome;
import com.example.spaceallocation.app_utilities.AppClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;

public class Login extends AppCompatActivity {

    ActionBar actionBar;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    ImageView ivLogo, ivRegister;
    private TextInputEditText etEmailAddress, etPassword;
    private TextInputLayout ilEmailAddress, ilPassword;
    TextView tvForgotPassword, tvRegisterHere;
    String sEmail, sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tLogin");

        mLoginFormView = findViewById(R.id.layout_form);
        mProgressView = findViewById(R.id.progress_bar);
        tvLoad = findViewById(R.id.tvLoad);
        ivLogo = findViewById(R.id.ivLogo);
        ivRegister = findViewById(R.id.ivRegister);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        ilEmailAddress = findViewById(R.id.ilEmailAddress);
        ilPassword = findViewById(R.id.ilPassword);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        etEmailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateEmailAddress(((EditText) v).getText());
                } //end if
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validatePassword(((EditText) v).getText());
                } //end if
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ResetPassword.class));
            } //end onClick()
        });
        ivRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            } //end onClick()
        });
    } //end onCreate()

    //Custom Methods
    public void btnLogin_onClick(View view) {
        /*
            purpose of this is to login registered user, either a lecturer or a student
        */
        try {
            //check if text inputs are empty
            if(!Objects.requireNonNull(etEmailAddress.getText()).toString().isEmpty() && !Objects.requireNonNull(etPassword.getText()).toString().isEmpty()) {
                //validate the email address
                if(AppClass.isEmailValid(etEmailAddress.getText().toString())) {
                    ilEmailAddress.setError(null);
                    //validate password
                    if(AppClass.isPasswordOk(etPassword.getText().toString())) {
                        ilPassword.setError(null);
                        sEmail = etEmailAddress.getText().toString();
                        sPassword = etPassword.getText().toString();

                        //log in user
                        showProgress(true);
                        tvLoad.setText(R.string.text_logging_in);
                        Backendless.UserService.login(sEmail, sPassword, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                showProgress(false);
                                String userNames = response.getProperty("lastName") + " " + response.getProperty("firstName");

                                //first check the user role of the user
                                Toast.makeText(Login.this, userNames + " logged in successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, StudentHome.class));
                                finish();
                            } //end handleResponse()
                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(Login.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            } //end handleFault()
                        }, true);
                    } //end if
                    else
                        ilPassword.setError("Password length is too short");
                } //end if
                else
                    ilEmailAddress.setError("Email address not valid");
            } //end if
            else {
                if(Objects.requireNonNull(etEmailAddress.getText()).toString().isEmpty() && Objects.requireNonNull(etPassword.getText()).toString().isEmpty()) {
                    ilEmailAddress.setError("Email address required");
                    ilPassword.setError("Password required");
                } //end if
                else if(Objects.requireNonNull(etEmailAddress.getText()).toString().isEmpty() && !Objects.requireNonNull(etPassword.getText()).toString().isEmpty()) {
                    ilEmailAddress.setError("Email address required");
                    ilPassword.setError(null);
                } //end if
                else if(!Objects.requireNonNull(etEmailAddress.getText()).toString().isEmpty() && Objects.requireNonNull(etPassword.getText()).toString().isEmpty()){
                    ilEmailAddress.setError(null);
                    ilPassword.setError("Password required");
                } //end if
            } //end else
        } //end try
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } //end catch()
    } //end btnLogin()

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            if(AppClass.isEmailValid(Objects.requireNonNull(etEmailAddress.getText()).toString()))
                ilEmailAddress.setError(null);
            else
                ilEmailAddress.setError("Email address not valid");
        }
        else
            ilEmailAddress.setError("Email address required");
    } //end validateEmail()

    public void validatePassword(Editable password) {
        //check if input text is empty
        if(!TextUtils.isEmpty(password)) {
            //check if the password entered is a valid password
            if(AppClass.isPasswordOk(Objects.requireNonNull(etPassword.getText()).toString()))
                ilPassword.setError(null);
            else
                ilPassword.setError("Password length is too short");
        } //end if
        else
            ilPassword.setError("Password required");
    } //end validatePassword()
} //end class Login

