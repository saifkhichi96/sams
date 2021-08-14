package co.aspirasoft.sams;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FragmentEducation extends StepFragment {

    public static StepFragment newInstance() {
        return new FragmentEducation();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_academic_info, container, false);

        // TODO: populateSpinner(view, R.id.selectUniversity, R.array.unis);
        // TODO: populateSpinner(view, R.id.selectSchool, R.array.schools);
        // TODO: populateSpinner(view, R.id.selectDepartment, R.array.depts);
        // TODO: populateSpinner(view, R.id.selectSemester, R.array.sems);

        return view;
    }

    @Override
    public boolean isDataValid() {
        // TODO: Process academic info
        return true;
    }

    private void populateSpinner(View view, int spinnerResId, int dataResId) {
        Spinner spinner = view.findViewById(spinnerResId);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                dataResId, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

}