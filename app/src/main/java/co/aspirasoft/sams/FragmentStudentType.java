package co.aspirasoft.sams;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

public class FragmentStudentType extends StepFragment {

    private RadioButton dayScholar;
    private RadioButton hostellerButton;

    public static StepFragment newInstance() {
        return new FragmentStudentType();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_type, container, false);

        dayScholar = view.findViewById(R.id.dayScholar);
        hostellerButton = view.findViewById(R.id.hostelite);

        return view;
    }

    @Override
    public boolean isDataValid() {
        if (dayScholar.isChecked() || hostellerButton.isChecked()) {
            data.put(R.id.student_type_field, dayScholar.isChecked() ? "Day Scholar" : "Hostellite");
            return true;
        } else {
            Toast.makeText(getContext(),
                    "Please select a type of student.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}