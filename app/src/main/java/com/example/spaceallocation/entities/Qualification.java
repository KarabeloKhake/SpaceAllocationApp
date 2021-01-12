package com.example.spaceallocation.entities;

public class Qualification {
    //Data Members
    private String department;
    private String facultyName;
    private String institutionName;
    private String qualificationName;
    private String userEmail;
    private String userStudentNumber;

    //Methods
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFacultyName() {
        return facultyName;
    }
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getInstitutionName() {
        return institutionName;
    }
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getQualificationName() {
        return qualificationName;
    }
    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserStudentNumber() {
        return userStudentNumber;
    }
    public void setUserStudentNumber(String userStudentNumber) {
        this.userStudentNumber = userStudentNumber;
    }
} //end class Qualification
