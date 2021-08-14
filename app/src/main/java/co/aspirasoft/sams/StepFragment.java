package co.aspirasoft.sams;

import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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