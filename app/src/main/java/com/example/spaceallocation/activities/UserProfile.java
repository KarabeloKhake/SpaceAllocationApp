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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
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

public class UserProfile extends AppCompatActivity {

    ActionBar actionBar;
    private View mProgressView;
    private View mUpdatePersonalDetailsFormView;
    private TextView tvLoad;
    AutoCompleteTextView acProvince;
    TextInputEditText etCity, etFirstName, etTelNumber, etIDNumber, etLastName;
    TextInputLayout ilCity,ilFirstName, ilTelNumber, ilIDNumber, ilLastName, ilProvince;
    Spinner spGender, spRace;
    TextView tvGender, tvRace;
    private String[] sGenderList, sProvinceList, sRaceList;     //holds list of gender & race items
    private String sGender, sProvince, sRace;                   //stores a gender & race item obtained from their respective lists
    ArrayAdapter<String> aGender, aProvince, aRace;             //adapters for holding gender & race arrays
    private final String USERS = "com.example.dynamicspaceallocation.Users";
    String userObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Update Personal Details");

        mUpdatePersonalDetailsFormView = findViewById(R.id.update_personal_details_form);
        mProgressView = findViewById(R.id.update_personal_details_progress);
        tvLoad = findViewById(R.id.tvLoad);
        acProvince = findViewById(R.id.acProvince);
        etCity = findViewById(R.id.etCity);
        etFirstName = findViewById(R.id.etFirstName);
        etTelNumber = findViewById(R.id.etTelNumber);
        etIDNumber = findViewById(R.id.etIDNumber);
        etLastName = findViewById(R.id.etLastName);
        ilCity = findViewById(R.id.ilCity);
        ilFirstName = findViewById(R.id.ilFirstName);
        ilTelNumber = findViewById(R.id.ilTelNumber);
        ilIDNumber = findViewById(R.id.ilIDNumber);
        ilLastName = findViewById(R.id.ilLastName);
        ilProvince = findViewById(R.id.ilProvince);
        spGender = findViewById(R.id.spGender);
        spRace = findViewById(R.id.spRace);
        tvGender = findViewById(R.id.tvGender);
        tvRace = findViewById(R.id.tvRace);
        genderSpinner(spGender);
        raceSpinner(spRace);
        setProvinces();

        userObjectId = UserIdStorageFactory.instance().getStorage().get();

        //test if the log in is valid
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if(response) {
                    //look for this student user in the mobile's storage
                    Backendless.Persistence.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            //get all user's info
//                            if(Objects.requireNonNull(etCity.getText()).toString().equals(""))
//                                etCity.setHint(R.string.hint_city);
//                            else
                                etCity.setText(response.getProperty("city").toString());
                            etFirstName.setText(response.getProperty("firstName").toString());
//                            if(Objects.requireNonNull(etIDNumber.getText()).toString().equals(""))
//                                etIDNumber.setHint(R.string.hint_id_number);
//                            else
                                etIDNumber.setText(response.getProperty("idNumber").toString());
                            etLastName.setText(response.getProperty("lastName").toString());
                            sGender = response.getProperty("gender").toString();
                            sProvince = response.getProperty("province").toString();
                            sRace = response.getProperty("race").toString();
//                            if(Objects.requireNonNull(etTelNumber.getText()).toString().equals(""))
//                                etTelNumber.setHint(R.string.hint_telephone_number);
//                            else
                                etTelNumber.setText(response.getProperty("telNumber").toString());

                            for(int x = 0; x < sGenderList.length; x++) {
                                if(spGender.getItemAtPosition(x).equals(sGender))
                                    spGender.setSelection(x);
                            } //end for

                            for(int y = 0; y < sRaceList.length; y++) {
                                if(spRace.getItemAtPosition(y).equals(sRace))
                                    spRace.setSelection(y);
                            } //end for

                            acProvince.setText(sProvince, false);
                        } //end handleResponse()
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(UserProfile.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        } //end handleFault()
                    });
                } //end if
            } //end handleResponse()
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(UserProfile.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
            } //end handleFault()
        });

        etCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateCity(((EditText) v).getText());
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
//        etIDNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus) {
//                    validateIDNumber(((EditText) v).getText());
//                } //end if
//            }
//        });
        etLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateNames(((EditText) v).getText(), ilLastName);
                } //end if
            }
        });
        acProvince.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validateProvince(((EditText) v).getText());
                } //end if
            }
        });
//        etTelNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus) {
//                    validateTelNumber(((EditText) v).getText());
//                } //end if
//            }
//        });
    } //end onCreate()

    //Custom Methods
    public void btnSaveProfile_onClick(View view) {
        try {
            //
            if(sGender.matches(" "))
                Toast.makeText(this, "Please select gender!", Toast.LENGTH_SHORT).show();

            if(sRace.matches(" "))
                Toast.makeText(this, "Please select race!", Toast.LENGTH_SHORT).show();

            if(!Objects.requireNonNull(etCity.getText()).toString().isEmpty() && !Objects.requireNonNull(etFirstName.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(etIDNumber.getText()).toString().isEmpty() && !Objects.requireNonNull(etTelNumber.getText()).toString().isEmpty() &&
                    !Objects.requireNonNull(etLastName.getText()).toString().isEmpty() && !Objects.requireNonNull(acProvince.getText()).toString().isEmpty()) {
                //validate id number
//                if (Objects.requireNonNull(etIDNumber.getText()).toString().length() == 13) {
//                    if (AppClass.isIDNumberValid(etIDNumber.getText().toString())) {
//                        //validate tel number
//                        if(etTelNumber.getText().toString().length() == 10) {
//                            if(AppClass.isPhoneNumberValid(etTelNumber.getText().toString())) {
                                ilLastName.setError(null);
                                ilFirstName.setError(null);
                                ilTelNumber.setError(null);
                                ilIDNumber.setError(null);
                                ilLastName.setError(null);
                                ilProvince.setError(null);

                                //do something
                                Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
                                    @Override
                                    public void handleResponse(Boolean response) {
                                        if(response) {
                                            final String userObjectId = UserIdStorageFactory.instance().getStorage().get();

                                            //look for this user in the database
                                            showProgress(true);
                                            tvLoad.setText(R.string.text_saving_user_info);
                                            Backendless.Persistence.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                                                @Override
                                                public void handleResponse(BackendlessUser response) {
                                                    AppClass.user = response;

                                                    AppClass.user.setProperty("city", etCity.getText().toString());
                                                    AppClass.user.setProperty("gender", sGender);
                                                    AppClass.user.setProperty("firstName", etFirstName.getText().toString());
                                                    AppClass.user.setProperty("idNumber", etIDNumber.getText().toString());
                                                    AppClass.user.setProperty("lastName", etLastName.getText().toString());
                                                    AppClass.user.setProperty("race", sRace);
                                                    AppClass.user.setProperty("telNumber", etTelNumber.getText().toString());

                                                    Backendless.UserService.update(AppClass.user, new AsyncCallback<BackendlessUser>() {
                                                        @Override
                                                        public void handleResponse(BackendlessUser response) {
                                                            showProgress(false);
                                                            if(userObjectId.equals(AppClass.user.getObjectId())) {
                                                                String userNames = response.getProperty("lastName") + " " + response.getProperty("firstName");

                                                                Toast.makeText(UserProfile.this, userNames + " logged in successfully!", Toast.LENGTH_SHORT).show();

                                                                if(AppClass.user.getProperty("userRole").equals("Lecturer"))
                                                                    startActivity(new Intent(UserProfile.this, LecturerHome.class));
                                                                else
                                                                    startActivity(new Intent(UserProfile.this, StudentHome.class));
                                                            } //end if
                                                        } //end handleResponse()

                                                        @Override
                                                        public void handleFault(BackendlessFault fault) {
                                                            showProgress(false);
                                                            Toast.makeText(UserProfile.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                        } //end handleFault()
                                                    });
                                                } //end handleResponse()

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    Toast.makeText(UserProfile.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                } //end handleFault()
                                            });
                                        } //end if
                                    } //end handleResponse()

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(UserProfile.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    } //end handleFault()
                                });
//                            } //end if
//                            else
//                                ilTelNumber.setError("Tel. number not in the right format");
//                        } //end if
//                        else
//                            ilTelNumber.setError("Tel. number must be 10 characters long");
//                    } //end if
//                    else
//                        ilIDNumber.setError("Invalid SA ID number.");
//                } //end if
//                else
//                    ilIDNumber.setError("ID number must be 13 characters long.");
            } //end if
            else {
                if(!Objects.requireNonNull(etCity.getText()).toString().isEmpty())
                    ilCity.setError(null);
                else
                    ilCity.setError("City/Town required");

                if(!Objects.requireNonNull(etFirstName.getText()).toString().isEmpty())
                    ilFirstName.setError(null);
                else
                    ilFirstName.setError("First name required");

                if(!Objects.requireNonNull(etTelNumber.getText()).toString().isEmpty())
                    ilTelNumber.setError(null);
                else
                    ilTelNumber.setError("Tel. number required");

                if(!Objects.requireNonNull(etIDNumber.getText()).toString().isEmpty())
                    ilIDNumber.setError(null);
                else
                    ilIDNumber.setError("Identity number required");

                if(!Objects.requireNonNull(etLastName.getText()).toString().isEmpty())
                    ilLastName.setError(null);
                else
                    ilLastName.setError("Last name required");

                if(!Objects.requireNonNull(acProvince.getText()).toString().isEmpty())
                    ilProvince.setError(null);
                else
                    ilProvince.setError("Province required");
            } //end else
        } //end try
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } //end catch()
    }

    private void genderSpinner(Spinner spinner) {
        /*
            the purpose of this method is to populate a gender spinner
            wth gender items from the gender array
        */

        //set the gender list to the array
        sGenderList = getResources().getStringArray(R.array.gender_array);
        aGender = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sGenderList);
        //set the adapter
        spinner.setAdapter(aGender);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItemPosition() == 0) {
                    sGender = " ";
                } //end if
                else {
                    sGender = parent.getSelectedItem().toString();
                } //end else
            } //end onItemSelected()
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            } //end onNothingSelected()
        });
    } //end genderSpinner()

    private void raceSpinner(Spinner spinner) {
        /*
            the purpose of this method is to populate a race spinner
            wth race items from the race array
        */

        //set the race list to the array
        sRaceList = getResources().getStringArray(R.array.ethnic_array);
        aRace = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sRaceList);
        //set the adapter
        spinner.setAdapter(aRace);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItemPosition() == 0) {
                    sRace = " ";
                } //end if
                else {
                    sRace = parent.getSelectedItem().toString();
                } //end else
            } //end onItemSelected()
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            } //end onNothingSelected()
        });

    } //end raceSpinner()

    private void setProvinces() {
        sProvinceList = getResources().getStringArray(R.array.province_array);
        aProvince = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, sProvinceList);

        acProvince.setAdapter(aProvince);
        acProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sProvince = parent.getItemAtPosition(position).toString();
            } //end onItemClick()
        });
    } //end setProvinces()

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mUpdatePersonalDetailsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mUpdatePersonalDetailsFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mUpdatePersonalDetailsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    private void validateCity(Editable city) {
        //check if input text is empty
        if(!TextUtils.isEmpty(city))
            ilCity.setError(null);
        else
            ilCity.setError("City/Town required");
    } //end validateCity()

    private void validateIDNumber(Editable idNumber) {
        //check if id number text field are empty
        if(!TextUtils.isEmpty(idNumber)) {
            if(Objects.requireNonNull(etIDNumber.getText()).toString().length() == 13) {
//                BigInteger idNumber_ = new BigInteger(Objects.requireNonNull(etIDNumber.getText()).toString());
                if(AppClass.isIDNumberValid(etIDNumber.getText().toString()))
                    ilIDNumber.setError(null);
                else
                    ilIDNumber.setError("Invalid SA ID number");
            } //end if
            else
                ilIDNumber.setError("ID number must be 13 characters long");
        } //end if
        else
            ilIDNumber.setError("Identity number required");
    } //end validateIDNumber()

    private void validateNames(Editable name, TextInputLayout textInputLayout) {
        //check if input text is empty
        if(!TextUtils.isEmpty(name))
            textInputLayout.setError(null);
        else
            textInputLayout.setError("Name required");
    } //end validateNames()

    private void validateProvince(Editable province) {
        //check if input text is empty
        if(!TextUtils.isEmpty(province))
            ilProvince.setError(null);
        else
            ilProvince.setError("Province required");
    } //end validateProvince()

    private void validateTelNumber(Editable telNumber) {
        //check if input text is empty
        if(!TextUtils.isEmpty(telNumber)) {
            //check if tel number entered is a valid tel number
            if(Objects.requireNonNull(etTelNumber.getText()).toString().length() == 10) {
                if(AppClass.isPhoneNumberValid(etTelNumber.getText().toString()))
                    ilTelNumber.setError(null);
                else
                    ilTelNumber.setError("Tel. number not in the right format");
            } //end if
            else
                ilTelNumber.setError("Tel. number must be 10 characters long");
        } //end if
        else
            ilTelNumber.setError("Tel. number required");
    } //end validateTelNumber()
} //end class UserProfile
