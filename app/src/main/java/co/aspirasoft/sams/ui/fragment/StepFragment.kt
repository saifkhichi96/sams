package co.aspirasoft.sams.ui.fragment

import android.util.SparseArray
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 05/04/2019 6:18 PM
 */
abstract class StepFragment : Fragment() {

    var data = SparseArray<String>()
        protected set

    abstract val isDataValid: Boolean

    protected fun checkFieldNotEmpty(wrapper: TextInputLayout, field: TextInputEditText): Boolean {
        wrapper.isErrorEnabled = false
        if (field.text == null || field.text.toString().trim { it <= ' ' }.isEmpty()) {
            wrapper.error = "This field is required."
            wrapper.isErrorEnabled = true
            return false
        }
        return true
    }

}