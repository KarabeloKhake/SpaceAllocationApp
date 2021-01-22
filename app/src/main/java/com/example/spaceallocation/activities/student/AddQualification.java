package com.example.spaceallocation.activities.student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.spaceallocation.entities.Qualification;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static com.example.spaceallocation.app_utilities.AppClass.qualification;
import static com.example.spaceallocation.app_utilities.AppClass.user;

public class AddQualification extends AppCompatActivity {

    ActionBar actionBar;
    private View mProgressView;
    private View mQualificationFormView;
    private TextView tvLoad;
    AutoCompleteTextView acDepartment, acFaculty, acEducationLevel, acQualification;
    TextInputEditText etAcademicInstitution;
    TextInputLayout ilAcademicInstitution, ilDepartment, ilFaculty, ilEducationLevel, ilQualification;
    private String[] sDepartments, sEducationList, sFaculties, sQualifications;
    private String sDepartment, sEducation, sFaculty, sQualification;
    String sStudentEmail,       //holds the logged in student's email
            sStudentNumber,     //holds the logged in student's id
            sStudentObjectId;   //student object id

    private ArrayAdapter<String> aDepartment, aEducation, aFaculty, aQualification;
    private Context context;
//    Qualification qualification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_qualification_activity);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("\t\t\t\t\tAdd Qualification");

        mQualificationFormView = findViewById(R.id.layout_form);
        mProgressView = findViewById(R.id.progress_bar);
        tvLoad = findViewById(R.id.tvLoad);
        acDepartment = findViewById(R.id.acDepartment);
        acFaculty = findViewById(R.id.acFaculty);
        acEducationLevel = findViewById(R.id.acEducationLevel);
        acQualification = findViewById(R.id.acQualification);
        etAcademicInstitution = findViewById(R.id.etAcademicInstitution);
        ilAcademicInstitution = findViewById(R.id.ilAcademicInstitution);
        ilDepartment = findViewById(R.id.ilDepartment);
        ilFaculty = findViewById(R.id.ilFaculty);
        ilEducationLevel = findViewById(R.id.ilEducationLevel);
        ilQualification = findViewById(R.id.ilQualification);
        context = getApplicationContext();
        setEducationLevels();
        setFaculties();

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
//                            user = response;
                            sStudentEmail = response.getEmail();
                            sStudentNumber = response.getProperty("studentNumber").toString();
                            sStudentObjectId = response.getObjectId();
                        } //end handleResponse()
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(AddQualification.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        } //end handleFault()
                    });
                } //end if
            } //end handleResponse()
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(AddQualification.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            } //end handleFault()
        });

        etAcademicInstitution.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateAcademicInstitution(((EditText) v).getText());
                } //end if
            }
        });
        acDepartment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateDepartment(((EditText) v).getText());
                } //end if
            }
        });
        acEducationLevel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateEducation(((EditText) v).getText());
                } //end if
            }
        });
        acFaculty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    acFaculty.clearListSelection();
                    validateFaculty(((EditText) v).getText());
                } //end if
            }
        });
        acQualification.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateQualificationName(((EditText) v).getText());
                } //end if
            }
        });
    } //end onCreate()

    //Custom Methods
    public void btnSaveQualification_onClick(View view) {
        try {
//            qualification = new Qualification();
            //
            if(!Objects.requireNonNull(etAcademicInstitution.getText()).toString().isEmpty() && !Objects.requireNonNull(acDepartment.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(acEducationLevel.getText()).toString().isEmpty() && !Objects.requireNonNull(acFaculty.getText()).toString().isEmpty() && !Objects.requireNonNull(acQualification.getText()).toString().isEmpty()) {
                //
                ilAcademicInstitution.setError(null);
                ilDepartment.setError(null);
                ilEducationLevel.setError(null);
                ilFaculty.setError(null);
                ilQualification.setError(null);

                qualification.setDepartment(acDepartment.getText().toString());
                qualification.setEducationLevel(acEducationLevel.getText().toString());
                qualification.setFacultyName(acFaculty.getText().toString());
                qualification.setInstitutionName(etAcademicInstitution.getText().toString());
                qualification.setQualificationName(acQualification.getText().toString());
                qualification.setStudentEmail(sStudentEmail);
                qualification.setStudentNumber(sStudentNumber);
                qualification.setStudentObjectId(sStudentObjectId);

                //save qualification in the database
                showProgress(true);
                tvLoad.setText(R.string.text_saving_qualification);
                Backendless.Data.of(Qualification.class).save(qualification, new AsyncCallback<Qualification>() {
                    @Override
                    public void handleResponse(Qualification response) {
                        showProgress(false);
                        Toast.makeText(context, "Qualification " + response.getQualificationName() + " successfully saved!", Toast.LENGTH_SHORT).show();
                        acDepartment.getText().clear();
                        acFaculty.getText().clear();
                        acQualification.getText().clear();
                        etAcademicInstitution.getText().clear();
                    } //end handleResponse()
                    @Override
                    public void handleFault(BackendlessFault fault) {
                        showProgress(false);
                        Toast.makeText(context, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    } //end handleFault()
                });
            } //end if
            else {
                if(!Objects.requireNonNull(etAcademicInstitution.getText()).toString().isEmpty())
                    ilAcademicInstitution.setError(null);
                else
                    ilAcademicInstitution.setError("Academic institution required");

                if(!Objects.requireNonNull(acDepartment.getText()).toString().isEmpty())
                    ilDepartment.setError(null);
                else
                    ilDepartment.setError("Department required");

                if(!Objects.requireNonNull(acEducationLevel.getText()).toString().isEmpty())
                    ilEducationLevel.setError(null);
                else
                    ilEducationLevel.setError("Education level required");

                if(!Objects.requireNonNull(acFaculty.getText()).toString().isEmpty())
                    ilFaculty.setError(null);
                else
                    ilFaculty.setError("Faculty required");

                if(!Objects.requireNonNull(acQualification.getText()).toString().isEmpty())
                    ilQualification.setError(null);
                else
                    ilQualification.setError("Qualification name required");
            } //end else
        } //end try
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } //end catch()
    } //end btnSaveQualification()

    private void setDepartments(String sFaculty_) {
        switch (sFaculty_) {
            case "Engineering, Built Environment & IT":
                sDepartments = getResources().getStringArray(R.array.engineering_built_environment_it_departments_array);
                break;
            case "Health & Environment Sciences":
                sDepartments = getResources().getStringArray(R.array.health_environment_sciences_departments_array);
                break;
            case "Humanities":
                sDepartments = getResources().getStringArray(R.array.humanities_departments_array);
                break;
            case "Management Sciences":
                sDepartments = getResources().getStringArray(R.array.management_sciences_departments_array);
                break;
        } //end switch()

        aDepartment = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, sDepartments);
        acDepartment.setAdapter(aDepartment);
        acDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sDepartment = parent.getItemAtPosition(position).toString();
                //get courses by a specific department
                setQualifications(sDepartment);
            } //end onItemClick()
        });
    } //end setDepartments()

    private void setEducationLevels() {
        sEducationList = getResources().getStringArray(R.array.educational_levels_array);
        aEducation = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, sEducationList);

        acEducationLevel.setAdapter(aEducation);
        acEducationLevel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sEducation = parent.getItemAtPosition(position).toString();
            } //end onItemClick()
        });
    } //end educationalLevels()

    private void setFaculties() {
        sFaculties = getResources().getStringArray(R.array.faculty_array);
        aFaculty = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, sFaculties);
        //set the adapter
        acFaculty.setAdapter(aFaculty);
        acFaculty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sFaculty = parent.getItemAtPosition(position).toString();
                setDepartments(sFaculty);
            } //end onItemClick()
        });
    } //end setFaculties()

    private void setQualifications(String sDepartment_) {
        switch (sDepartment_) {
            //Engineering, Built Environment & IT Qualifications
            case "Built Environment":
                sQualifications = getResources().getStringArray(R.array.built_environment_courses_array);
                break;
            case "Civil Engineering":
                sQualifications = getResources().getStringArray(R.array.civil_engineering_courses_array);
                break;
            case "Electrical, Electronic &amp; Computer Engineering":
                sQualifications = getResources().getStringArray(R.array.electrical_electronic_computer_engineering_courses_array);
                break;
            case "Information Technology":
                sQualifications = getResources().getStringArray(R.array.information_technology_courses_array);
                break;
            case "Mechanical & Mechatronic Engineering":
                sQualifications = getResources().getStringArray(R.array.mechanical_mechatronic_engineering_courses_array);
                break;
            //Health & Environment Sciences Qualifications
            case "Agriculture":
                sQualifications = getResources().getStringArray(R.array.agriculture_courses_array);
                break;
            case "Clinical Sciences":
                sQualifications = getResources().getStringArray(R.array.clinical_sciences_courses_array);
                break;
            case "Health Sciences":
                sQualifications = getResources().getStringArray(R.array.health_sciences_courses_array);
                break;
            case "Life Sciences":
                sQualifications = getResources().getStringArray(R.array.life_sciences_courses_array);
                break;
            //Humanities Qualifications
            case "Communication Sciences":
                sQualifications = getResources().getStringArray(R.array.communication_sciences_courses_array);
                break;
            case "Design & Studio Art":
                sQualifications = getResources().getStringArray(R.array.design_studio_art_courses_array);
                break;
            case "Language and Social Sciences Education":
                sQualifications = getResources().getStringArray(R.array.language_social_sciences_education_courses_array);
                break;
            case "Mathematics, Science & Technology Education":
                sQualifications = getResources().getStringArray(R.array.mathematics_science_technology_education_courses_array);
                break;
            case "Post Graduate in Education":
                sQualifications = getResources().getStringArray(R.array.post_grad_education_courses_array);
                break;
            //Management Sciences Qualifications
            case "Accounting & Auditing":
                sQualifications = getResources().getStringArray(R.array.accounting_auditing_courses_array);
                break;
            case "Business Management":
                sQualifications = getResources().getStringArray(R.array.business_management_courses_array);
                break;
            case "Business Support Studies":
                sQualifications = getResources().getStringArray(R.array.business_support_studies_courses_array);
                break;
            case "Government Management":
                sQualifications = getResources().getStringArray(R.array.government_management_courses_array);
                break;
            case "Hospitality Management":
                sQualifications = getResources().getStringArray(R.array.hospitality_management_courses_array);
                break;
            case "Tourism & Event Management":
                sQualifications = getResources().getStringArray(R.array.tourism_event_management_courses_array);
                break;
        } //end switch()

        aQualification = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, sQualifications);
        acQualification.setAdapter(aQualification);
        acQualification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sQualification = parent.getItemAtPosition(position).toString();
            } //end onItemClick()
        });
    } //end setQualifications()

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mQualificationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mQualificationFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mQualificationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    private void validateAcademicInstitution(Editable academicInst) {
        //check if input text is empty
        if(!TextUtils.isEmpty(academicInst))
            ilAcademicInstitution.setError(null);
        else
            ilAcademicInstitution.setError("Academic institution required");
    } //end validateAcademicInstitution()

    private void validateDepartment(Editable department) {
        //check if input text is empty
        if(!TextUtils.isEmpty(department)) {
            ilDepartment.setError(null);
        } //end if
        else
            ilDepartment.setError("Department required");
    } //end validateDepartment()

    private void validateEducation(Editable education) {
        //check if input text is empty
        if(!TextUtils.isEmpty(education))
            ilEducationLevel.setError(null);
        else
            ilEducationLevel.setError("Education level required");
    } //end validateEducation()

    private void validateFaculty(Editable faculty) {
        //check if input text is empty
        if(!TextUtils.isEmpty(faculty)) {
            ilFaculty.setError(null);
            //set the department list
            if(sFaculty.equals("Engineering Built Environment & IT")) {
                sDepartments = getResources().getStringArray(R.array.engineering_built_environment_it_departments_array);
                aDepartment = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, sDepartments);

                acDepartment.setAdapter(aDepartment);
                acDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    } //end onItemSelected()
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { } //end onNothingSelected()
                });
            } //end if

        } //end if
        else
            ilFaculty.setError("Faculty required");
    } //end validateFaculty()

    private void validateQualificationName(Editable qualificationName) {
        //check if input text is empty
        if(!TextUtils.isEmpty(qualificationName))
            ilQualification.setError(null);
        else
            ilQualification.setError("Qualification name required");
    } //end validateQualificationName()
} //end class AddEducation()