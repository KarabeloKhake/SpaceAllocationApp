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
import com.example.spaceallocation.R;
import com.example.spaceallocation.app_utilities.AppClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static com.example.spaceallocation.app_utilities.AppClass.user;

public class Register extends AppCompatActivity {

    ActionBar actionBar;
    private View mProgressView;
    private View mPersonalDetailsFormView;
    private TextView tvLoad;
    TextView tvScanCode;
    TextInputEditText etEmail, etFirstName, etLastName, etPassword, etRetypePassword, etUserCode;
    TextInputLayout ilEmail, ilFirstName, ilLastName, ilPassword, ilRetypePassword, ilUserCode;
    private final String USERS = "com.example.dynamicspaceallocation.Users";
    String sCity, sEmail, sGender, sRace, sProvince, sFirstName, sLastName, sIdNumber, sPassword, sRetype, sTelNumber, sUserCode, sUserType;
    private final int SCAN_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("\t\t\t\t\t\t\t\t\t\tRegister");

        mPersonalDetailsFormView = findViewById(R.id.layout_form);
        mProgressView = findViewById(R.id.progress_bar);
        tvLoad = findViewById(R.id.tvLoad);
        etEmail = findViewById(R.id.etEmail);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPassword = findViewById(R.id.etPassword);
        etRetypePassword = findViewById(R.id.etRetypePassword);
        etUserCode = findViewById(R.id.etUserCode);
        ilEmail = findViewById(R.id.ilEmail);
        ilFirstName = findViewById(R.id.ilFirstName);
        ilLastName = findViewById(R.id.ilLastName);
        ilPassword = findViewById(R.id.ilPassword);
        ilRetypePassword = findViewById(R.id.ilRetypePassword);
        ilUserCode = findViewById(R.id.ilUserCode);
        tvScanCode = findViewById(R.id.tvScanCode);

        tvScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Register.this, ScanCode.class), SCAN_CODE);
            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateEmailAddress(((EditText) v).getText());
                } //end if
            }
        });
        etFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateNames(((EditText) v).getText(), ilFirstName);
                } //end if
            }
        });
        etLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateNames(((EditText) v).getText(), ilLastName);
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
        etRetypePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateRetypePassword(((EditText) v).getText());
                } //end if
            }
        });
        etUserCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateUserCode(((EditText) v).getText());
                } //end if
            }
        });
    } //end onCreate()

    //Custom Methods
    public void btnRegister_onClick(View view) {
        try {
            //
            if(!Objects.requireNonNull(etEmail.getText()).toString().isEmpty() && !Objects.requireNonNull(etFirstName.getText()).toString().isEmpty() && !Objects.requireNonNull(etPassword.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(etLastName.getText()).toString().isEmpty() && !Objects.requireNonNull(etRetypePassword.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(etUserCode.getText()).toString().isEmpty()) {
                //
                ilLastName.setError(null);
                ilFirstName.setError(null);
                ilPassword.setError(null);
                ilRetypePassword.setError(null);
                ilUserCode.setError(null);
                //
                //validate email address
                if(AppClass.isEmailValid(etEmail.getText().toString())) {
                    ilEmail.setError(null);;

                    sPassword = etPassword.getText().toString();
                    sRetype = etRetypePassword.getText().toString();
                    //validate passwords
                    if(!AppClass.isPasswordOk(sPassword))
                        ilPassword.setError("Password length too short");
                    if(!AppClass.isPasswordOk(sRetype))
                        ilRetypePassword.setError("Password length too short");
                    //test if both passwords matches
                    if(sPassword.matches(sRetype)) {
                        ilPassword.setError(null);
                        ilRetypePassword.setError(null);

                        //set user info
                        sEmail = etEmail.getText().toString();
                        sFirstName = etFirstName.getText().toString();
                        sLastName = etLastName.getText().toString();
                        sRetype = etRetypePassword.getText().toString();
                        sUserCode = etUserCode.getText().toString();

                        AppClass.user.setProperty("city", sCity = "");
                        AppClass.user.setEmail(sEmail);
                        AppClass.user.setProperty("firstName", sFirstName);
                        AppClass.user.setProperty("gender", sGender = "");
                        AppClass.user.setProperty("idNumber", sIdNumber = "");
                        AppClass.user.setProperty("lastName", sLastName);
                        AppClass.user.setProperty("province", sProvince = "");
                        AppClass.user.setProperty("race", sRace = "");
                        AppClass.user.setPassword(sRetype);
                        AppClass.user.setProperty("telNumber", sTelNumber = "");
                        if(sUserCode.length() == 5) {
                            sUserType = "Lecturer";
                            AppClass.user.setProperty("lecturerNumber", sUserCode);
                        } //end if
                        else if(sUserCode.length() == 9) {
                            sUserType = "Student";
                            AppClass.user.setProperty("studentNumber", sUserCode);
                        } //end if
                        AppClass.user.setProperty("userRole", sUserType);

                        //register new user
                        showProgress(true);
                        if(sUserType.equals("Lecturer"))
                            tvLoad.setText(R.string.text_registering_lecturer);
                        else
                            tvLoad.setText(R.string.text_registering_student);
                        Backendless.UserService.register(AppClass.user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                showProgress(false);
                                String userNames = response.getProperty("lastName") + " " + response.getProperty("firstName");

                                if(sUserType.equals("Lecturer"))
                                    Toast.makeText(Register.this, "Lecturer " + userNames + " successfully registered!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(Register.this, "Student " + userNames + " successfully registered!", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(Register.this, Login.class));
                                finish();
                            } //end handleResponse()
                            @Override
                            public void handleFault(BackendlessFault fault) {
                                showProgress(false);
                                Toast.makeText(Register.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            } //end handleFault()
                        });
                    } //end if
                    else if(!sPassword.matches(sRetype) || !sRetype.matches(sPassword)) {
                        ilPassword.setError("Passwords do not match");
                        ilRetypePassword.setError("Passwords do not match");
                    } //end else
                } //end if
                else
                    ilEmail.setError("Email address not valid");
            } //end if
            else {
                //email
                if(!etEmail.getText().toString().isEmpty())
                    ilEmail.setError(null);
                else
                    ilEmail.setError("Email address required");
                //first name
                if(!Objects.requireNonNull(etFirstName.getText()).toString().isEmpty())
                    ilFirstName.setError(null);
                else
                    ilFirstName.setError("First name required");
                //password
                if(!Objects.requireNonNull(etPassword.getText()).toString().isEmpty())
                    ilPassword.setError(null);
                else
                    ilPassword.setError("Password required");
                //retype password
                if(!Objects.requireNonNull(etRetypePassword.getText()).toString().isEmpty())
                    ilRetypePassword.setError(null);
                else
                    ilRetypePassword.setError("Retype password required");
                //last night
                if(!Objects.requireNonNull(etLastName.getText()).toString().isEmpty())
                    ilLastName.setError(null);
                else
                    ilLastName.setError("Last name required");

                if(!Objects.requireNonNull(etUserCode.getText()).toString().isEmpty())
                    ilUserCode.setError(null);
                else
                    ilUserCode.setError("User code required");
            } //end else
        } //end try
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } //end catch()
    } //end btnRegister()

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mPersonalDetailsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mPersonalDetailsFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPersonalDetailsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            if(AppClass.isEmailValid(Objects.requireNonNull(etEmail.getText()).toString()))
                ilEmail.setError(null);
            else
                ilEmail.setError("Email address not valid");
        }
        else
            ilEmail.setError("Email address required");
    } //end validateEmail()

    private void validateNames(Editable name, TextInputLayout textInputLayout) {
        //check if input text is empty
        if(!TextUtils.isEmpty(name))
            textInputLayout.setError(null);
        else
            textInputLayout.setError("Name required");
    } //end validateNames()

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

    private void validateUserCode(Editable userCode) {
        //check if input text is empty
        if(!TextUtils.isEmpty(userCode))
            ilUserCode.setError(null);
        else
            ilUserCode.setError("User code required");
    } //end validateUserCode()
} //end class Register