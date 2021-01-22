package com.example.spaceallocation.app_utilities;

import android.app.Application;
import android.util.Patterns;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.spaceallocation.entities.Course;
import com.example.spaceallocation.entities.Qualification;
import com.example.spaceallocation.entities.Student;

import java.util.List;
import java.util.regex.Pattern;

public class AppClass extends Application {
    //Data Members
    public static final String APPLICATION_ID = "85F1DB86-1E1E-1C1F-FF41-17E902666A00";
    public static final String API_KEY = "39CBECF9-F598-438D-AC08-9FE79B16B102";
    public static final String SERVER_URL = "https://api.backendless.com";
    public static BackendlessUser user;
    public static Course course;
    public static List<Course> courses;
    public static Qualification qualification;
    public static Student student;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl(SERVER_URL);
        Backendless.initApp( getApplicationContext(), APPLICATION_ID, API_KEY );
        user = new BackendlessUser();
        course = new Course();
        qualification = new Qualification();
        student = new Student();
    } //end onCreate()

    //Custom Methods
    public static boolean isEmailValid(String sEmail) {
        /*
            the purpose of this method is to validate the email address entered by the user
            to check if it is in the right format
        */
//        String sRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//
//        return sEmail.matches(sRegex);
        return Patterns.EMAIL_ADDRESS.matcher(sEmail).matches();
    } //end isEmailValid()
    public static Boolean isIDNumberValid(String identities) {
        char[] idChars = identities.toCharArray();
        int sum = 0;
        // loop over each digit right-to-left, including the check-digit
        for (int i = 1; i <= idChars.length; i++) {
            int digit = Character.getNumericValue(idChars[idChars.length - i]);

            if ((i % 2) != 0) {
                sum += digit;
            } //end if
            else {
                sum += digit < 5 ? digit * 2 : digit * 2 - 9;
            } //end else
        } //end for
        return (sum % 10) == 0;
    } //end isIDNumberValid()
    public static boolean isPasswordOk(String sPassword) {
        /*
            this method validates if the password is 8 characters long or not
        */

        return sPassword.length() >= 8;
    } //end isPasswordOk()
    public static boolean isPhoneNumberValid(String sPhoneNumber) {
        /*
            the purpose of this method is to determine if the phone number provided
            by the user is a valid South African phone number.
         */
        Pattern pattern = Pattern.compile("(0)?[1-8]{2}[0-9]{7}");
        return pattern.matcher(sPhoneNumber).matches();
    } //end isPhoneNumberValid()
} //end class AppClass
