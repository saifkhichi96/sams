package co.aspirasoft.sams;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

/**
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 05/04/2019 6:18 PM
 */
public abstract class StepFragment extends Fragment {

    protected SparseArray<String> data = new SparseArray<>();

    public abstract boolean isDataValid();

    @NonNull
    public SparseArray<String> getData() {
        return data;
    }

    protected boolean checkFieldNotEmpty(TextInputLayout wrapper, TextInputEditText field) {
        wrapper.setErrorEnabled(false);
        if (field.getText() == null || field.getText().toString().trim().isEmpty()) {
            wrapper.setError("This field is required.");
            wrapper.setErrorEnabled(true);
            return false;
        }

        return true;
    }

}