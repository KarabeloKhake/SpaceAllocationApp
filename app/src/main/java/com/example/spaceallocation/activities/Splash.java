package com.example.spaceallocation.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.example.spaceallocation.R;
import com.example.spaceallocation.activities.student.StudentHome;

public class Splash extends AppCompatActivity {
    private View mProgressView;
    private View mSplashFormView;
    private TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        mSplashFormView = findViewById(R.id.layout_form);
        mProgressView = findViewById(R.id.progress_bar);
        tvLoad = findViewById(R.id.tvLoad);

        //check for user's log in credentials
        showProgress(true);
        tvLoad.setText(R.string.text_loading);
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if(response) {
                    tvLoad.setText(R.string.text_checking_credentials);
                    String userObjectId = UserIdStorageFactory.instance().getStorage().get();

                    //look for this user in the mobile's storage
                    Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            showProgress(false);
                            Toast.makeText(Splash.this, "Logged in successfully!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Splash.this, StudentHome.class));
                            finish();
                        } //end handleResponse()
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            showProgress(false);
                            Toast.makeText(Splash.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        } //end handleFault()
                    });
                } //end if
                else {
                    //no user logged in
                    showProgress(false);
                    startActivity(new Intent(Splash.this, Login.class));
                } //end else
            } //end handleResponse()
            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(Splash.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            } //end handleFault()
        });
    } //end onCreate()

    //Custom Method
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSplashFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mSplashFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSplashFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
} //end class Splash
