package co.aspirasoft.sams.model.universities;

import java.util.ArrayList;

/**
 * Created by Muhammad Saifullah K on 21/01/2017.
 */

public class School {

    private String name;
    private ArrayList<Department> departments = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

}