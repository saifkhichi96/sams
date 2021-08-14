package co.aspirasoft.sams.model.universities;

import java.util.ArrayList;


public class University {

    private String name;

    private ArrayList<School> schools = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<School> getSchools() {
        return schools;
    }

    public void setSchools(ArrayList<School> schools) {
        this.schools = schools;
    }

}