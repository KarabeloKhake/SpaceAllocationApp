package com.example.spaceallocation.activities.student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import com.example.spaceallocation.app_utilities.AppClass;
import com.example.spaceallocation.entities.Course;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static com.example.spaceallocation.app_utilities.AppClass.course;
import static com.example.spaceallocation.app_utilities.AppClass.courses;
import static com.example.spaceallocation.app_utilities.AppClass.user;

public class AddCourse extends AppCompatActivity {

    ActionBar actionBar;
    private View mProgressView;
    private View mAddCourseFormView;
    private TextView tvLoad;
    private TextInputEditText etCourseCode, etCourseName, etCourseDescription;
    private TextInputLayout ilCourseCode, ilCourseName, ilCourseDescription;
    private String sCode, sDescription, sEmail, sName, sObjectId, sStudentNumber;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_activity);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("\t\t\t\t\t\t\tAdd Course");

        mAddCourseFormView = findViewById(R.id.layout_form);
        mProgressView = findViewById(R.id.progress_bar);
        tvLoad = findViewById(R.id.tvLoad);
        etCourseCode = findViewById(R.id.etCourseCode);
        etCourseDescription = findViewById(R.id.etCourseDescription);
        etCourseName = findViewById(R.id.etCourseName);
        ilCourseCode = findViewById(R.id.ilCourseCode);
        ilCourseDescription = findViewById(R.id.ilCourseDescription);
        ilCourseName = findViewById(R.id.ilCourseName);

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if(response) {

                    final String userObjectId = UserIdStorageFactory.instance().getStorage().get();

                    //look for this student user in the mobile's storage
                    Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            //user found
                            user = response;

                            sEmail = user.getEmail();
                            sObjectId = user.getObjectId();
                            sStudentNumber = user.getProperty("studentNumber").toString();
                        } //end handleResponse()
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(AddCourse.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        } //end handleFault()
                    });
                } //end if
            } //end handleResponse()
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(AddCourse.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            } //end handleFault()
        });

        etCourseCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateCourseCode(((EditText) v).getText());
                } //end if
            }
        });
        etCourseDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateCourseDes(((EditText) v).getText());
                } //end if
            }
        });
        etCourseName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateCourseName(((EditText) v).getText());
                } //end if
            }
        });
    } //end onCreate()

    //Custom Methods
    public void btnAddCourse_onClick(View view) {
        try {
            course = new Course();
            //
            if(!Objects.requireNonNull(etCourseCode.getText()).toString().isEmpty() && !Objects.requireNonNull(etCourseDescription.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(etCourseName.getText()).toString().isEmpty()) {
                //
                ilCourseCode.setError(null);
                ilCourseDescription.setError(null);
                ilCourseName.setError(null);

                //do something
                sCode = etCourseCode.getText().toString();
                sDescription = etCourseDescription.getText().toString();
                sName = etCourseName.getText().toString();

                //set up course
                course.setCourseCode(sCode);
                course.setCourseDescription(sDescription);
                course.setCourseName(sName);
                course.setUserObjectId(sObjectId);
                course.setUserEmail(sEmail);
                course.setUserStudentNumber(sStudentNumber);

                //save new course
                showProgress(true);
                tvLoad.setText(R.string.text_saving_course);
                Backendless.Data.of(Course.class).save(course, new AsyncCallback<Course>() {
                    @Override
                    public void handleResponse(Course response) {
                        showProgress(false);
                        Toast.makeText(AddCourse.this, "Course " + response.getCourseCode() + " successfully added!!", Toast.LENGTH_SHORT).show();

                        Objects.requireNonNull(etCourseCode.getText()).clear();
                        Objects.requireNonNull(etCourseDescription.getText()).clear();
                        Objects.requireNonNull(etCourseName.getText()).clear();
                    } //end handleResponse()

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(AddCourse.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    } //end handleFault()
                });

            } //end if
            else {
                //
                if(!Objects.requireNonNull(etCourseCode.getText()).toString().isEmpty())
                    ilCourseCode.setError(null);
                else
                    ilCourseCode.setError("Course code required");

                if(!Objects.requireNonNull(etCourseCode.getText()).toString().isEmpty())
                    ilCourseDescription.setError(null);
                else
                    ilCourseDescription.setError("Course description required");

                if(!Objects.requireNonNull(etCourseName.getText()).toString().isEmpty())
                    ilCourseName.setError(null);
                else
                    ilCourseName.setError("Course name required");
            } //end else
        } //end try
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } //end catch()
    } //end btnAddCourse_onClick()

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mAddCourseFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mAddCourseFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAddCourseFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    private void validateCourseCode(Editable courseCode) {
        //check if input text is empty
        if(!TextUtils.isEmpty(courseCode)) {
            ilCourseCode.setError(null);
        } //end if
        else
            ilCourseCode.setError("Course code required");
    } //end validateCourseCode()

    private void validateCourseDes(Editable courseDes) {
        //check if input text is empty
        if(!TextUtils.isEmpty(courseDes)) {
            ilCourseDescription.setError(null);
        } //end if
        else
            ilCourseDescription.setError("Course description required");
    } //end validateCourseDes()

    private void validateCourseName(Editable courseName) {
        //check if input text is empty
        if(!TextUtils.isEmpty(courseName)) {
            ilCourseName.setError(null);
        } //end if
        else
            ilCourseName.setError("Course name required");
    } //end validateCourseName()
} //end class AddCourse
