package com.example.spaceallocation.entities;

public class Course {
    //Data Members
    private String courseCode;
    private String courseDescription;
    private String courseName;
    private String userObjectId;
    private String userStudentNumber;
    private String userEmail;

    //Constructors
    public Course() {} //end default constructor

    //Methods
    public String getCourseCode() {
        return courseCode;
    } //end getCourseCode()
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    } //end setCourseCode()

    public String getCourseDescription() {
        return courseDescription;
    } //end getCourseDescription
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    } //end setCourseDescription()

    public String getCourseName() {
        return courseName;
    } //end getCourseName()
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    } //end setCourseName()

    public String getUserObjectId() {
        return userObjectId;
    } //end getUserObjectId()
    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    } //end setUserObjectId()

    public String getUserStudentNumber() {
        return userStudentNumber;
    }
    public void setUserStudentNumber(String userStudentNumber) {
        this.userStudentNumber = userStudentNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
} //end class Course
