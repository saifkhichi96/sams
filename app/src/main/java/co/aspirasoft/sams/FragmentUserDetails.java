package co.aspirasoft.sams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FragmentUserDetails extends StepFragment {

    private TextInputLayout nameWrapper;
    private TextInputEditText nameField;

    public static StepFragment newInstance() {
        return new FragmentUserDetails();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);

        nameWrapper = view.findViewById(R.id.name_wrapper);
        nameField = view.findViewById(R.id.name_field);

        return view;
    }

    @Override
    public boolean isDataValid() {
        if (checkFieldNotEmpty(nameWrapper, nameField)) {
            data.put(R.id.name_field, nameField.getText().toString().trim());
            return true;
        } else {
            return false;
        }
    }

}