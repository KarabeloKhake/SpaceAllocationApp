package com.example.spaceallocation.entities;

public class Qualification {
    //Data Members
    private String department;
    private String educationLevel;
    private String facultyName;
    private String institutionName;
    private String qualificationName;
    private String studentEmail;
    private String studentNumber;
    private String studentObjectId;

    //Constructor
    public Qualification() {}

    //Methods
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEducationLevel() {
        return educationLevel;
    }
    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
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

    public String getStudentEmail() {
        return studentEmail;
    }
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentNumber() {
        return studentNumber;
    }
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentObjectId() {
        return studentObjectId;
    }
    public void setStudentObjectId(String studentObjectId) {
        this.studentObjectId = studentObjectId;
    }
} //end class Qualification
