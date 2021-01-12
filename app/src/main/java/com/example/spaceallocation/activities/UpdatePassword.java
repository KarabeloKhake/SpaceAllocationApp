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
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.example.spaceallocation.R;
import com.example.spaceallocation.activities.lecturer.LecturerHome;
import com.example.spaceallocation.activities.student.StudentHome;
import com.example.spaceallocation.app_utilities.AppClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class UpdatePassword extends AppCompatActivity {

    ActionBar actionBar;
    private View mProgressView;
    private View mUpdatePasswordFormView;
    private TextView tvLoad;
    TextInputEditText etNewPassword, etRetypePassword;
    TextInputLayout ilNewPassword, ilRetypePassword;
    String sNewPassword, sRetypePassword;
    String sEmail,      //user email for logging in
            sPassword;  //updated user password for logging in
    String userObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password_activity);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("\t\t\t\t\t\t\tUpdate Password");

        mUpdatePasswordFormView = findViewById(R.id.layout_form);
        mProgressView = findViewById(R.id.progress_bar);
        tvLoad = findViewById(R.id.tvLoad);
        etNewPassword = findViewById(R.id.etNewPassword);
        etRetypePassword = findViewById(R.id.etRetypePassword);
        ilNewPassword = findViewById(R.id.ilNewPassword);
        ilRetypePassword = findViewById(R.id.ilRetypePassword);

        etNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validatePassword(((EditText) v).getText());
                } //end if
            }
        });
        etRetypePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateRetypePassword(((EditText) v).getText());
                } //end if
            }
        });
    } //end onCreate()

    //Custom Methods
    public void btnUpdatePassword_onClick(View view) {
        try {
            //test if all text inputs are empty
            if(!Objects.requireNonNull(etNewPassword.getText()).toString().isEmpty() && !Objects.requireNonNull(etRetypePassword.getText()).toString().isEmpty()) {

                sNewPassword = etNewPassword.getText().toString();
                sRetypePassword = etRetypePassword.getText().toString();
                //validate passwords
                if(!AppClass.isPasswordOk(sNewPassword))
                    ilNewPassword.setError("Password length too short");
                if(!AppClass.isPasswordOk(sRetypePassword))
                    ilRetypePassword.setError("Password length too short");

                //test if both passwords matches
                if(sNewPassword.matches(sRetypePassword)) {
                    ilNewPassword.setError(null);
                    ilRetypePassword.setError(null);

                    //update user password
                    showProgress(true);
                    tvLoad.setText(R.string.text_updating_password);
                    Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
                        @Override
                        public void handleResponse(Boolean response) {
                            if(response) {

                                userObjectId = UserIdStorageFactory.instance().getStorage().get();

                                //look for this student user in the mobile's storage
                                Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                                    @Override
                                    public void handleResponse(BackendlessUser response) {
                                        //user found
                                        response.setPassword(sRetypePassword);

                                        Backendless.UserService.update(response, new AsyncCallback<BackendlessUser>() {
                                            @Override
                                            public void handleResponse(BackendlessUser response) {
                                                showProgress(false);
                                                Toast.makeText(UpdatePassword.this, "Password successfully changed!!", Toast.LENGTH_SHORT).show();
                                                if(userObjectId.equals(AppClass.user.getObjectId())) {
                                                    if(AppClass.user.getProperty("userRole").equals("Lecturer"))
                                                        startActivity(new Intent(UpdatePassword.this, LecturerHome.class));
                                                    else
                                                        startActivity(new Intent(UpdatePassword.this, StudentHome.class));
                                                } //end if
                                                finish();
                                            } //end handleResponse()

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                showProgress(false);
                                                Toast.makeText(UpdatePassword.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                            } //end handleFault()
                                        });
                                    } //end handleResponse()

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(UpdatePassword.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    } //end handleFault()
                                });
                            } //end if
                        } //end handleResponse()

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(UpdatePassword.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        } //end handleFault()
                    });
                } //end if
                else if(!sNewPassword.matches(sRetypePassword) || !sRetypePassword.matches(sNewPassword)) {
                    ilNewPassword.setError("Passwords do not match");
                    ilRetypePassword.setError("Passwords do not match");
                } //end else
            } //end if
            else {
                //test if any single text input is empty
                if(!Objects.requireNonNull(etNewPassword.getText()).toString().isEmpty())
                    ilNewPassword.setError(null);
                else
                    ilNewPassword.setError("New password required");

                if(!Objects.requireNonNull(etRetypePassword.getText()).toString().isEmpty())
                    ilRetypePassword.setError(null);
                else
                    ilRetypePassword.setError("Retype password required");
            } //end else
        } //end try
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } //end catch()
    } //end btnUpdatePassword()

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mUpdatePasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mUpdatePasswordFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mUpdatePasswordFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    public void validatePassword(Editable password) {
        //check if input text is empty
        if(!TextUtils.isEmpty(password)) {
            //check if the password entered is a valid password
            if(AppClass.isPasswordOk(Objects.requireNonNull(etNewPassword.getText()).toString()))
                ilNewPassword.setError(null);
            else
                ilNewPassword.setError("Password length is too short");
        } //end if
        else
            ilNewPassword.setError("New password required");
    } //end validatePassword()

    public void validateRetypePassword(Editable retypePassword) {
        //check if input text is empty
        if(!TextUtils.isEmpty(retypePassword)) {
            //check if the password entered is a valid password
            if(AppClass.isPasswordOk(Objects.requireNonNull(etRetypePassword.getText()).toString()))
                ilRetypePassword.setError(null);
            else
                ilRetypePassword.setError("Password length is too short");
        } //end if
        else
            ilRetypePassword.setError("Retype password required");
    } //end validatePassword()
} //end UpdatePassword