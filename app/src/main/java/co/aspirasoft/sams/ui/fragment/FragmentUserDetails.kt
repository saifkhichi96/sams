package co.aspirasoft.sams.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.aspirasoft.sams.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FragmentUserDetails : StepFragment() {

    private var nameWrapper: TextInputLayout? = null
    private var nameField: TextInputEditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_personal_info, container, false)
        nameWrapper = view.findViewById(R.id.name_wrapper)
        nameField = view.findViewById(R.id.name_field)
        return view
    }

    override val isDataValid: Boolean
        get() = if (checkFieldNotEmpty(nameWrapper!!, nameField!!)) {
            data.put(R.id.name_field, nameField!!.text.toString().trim { it <= ' ' })
            true
        } else {
            false
        }

    companion object {
        @JvmStatic
        fun newInstance(): StepFragment {
            return FragmentUserDetails()
        }
    }
}