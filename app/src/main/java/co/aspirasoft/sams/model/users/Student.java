package co.aspirasoft.sams.model.users;

import co.aspirasoft.sams.model.universities.Department;
import co.aspirasoft.sams.model.universities.School;
import co.aspirasoft.sams.model.universities.University;

public class Student extends User {

    private String type;
    private String universityName;

    private transient Department department;
    private transient School school;
    private transient University university;

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}