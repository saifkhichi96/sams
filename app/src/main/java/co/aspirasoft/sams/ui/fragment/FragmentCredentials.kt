package co.aspirasoft.sams.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.aspirasoft.sams.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FragmentCredentials : StepFragment() {

    private lateinit var usernameWrapper: TextInputLayout
    private lateinit var usernameField: TextInputEditText
    private lateinit var passwordWrapper: TextInputLayout
    private lateinit var passwordField: TextInputEditText
    private lateinit var confirmWrapper: TextInputLayout
    private lateinit var confirmField: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_credentials, container, false)
        usernameWrapper = view.findViewById(R.id.username_wrapper)
        usernameField = view.findViewById(R.id.username_field)
        usernameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkFieldNotEmpty(usernameWrapper, usernameField)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        passwordWrapper = view.findViewById(R.id.password_wrapper)
        passwordField = view.findViewById(R.id.password_field)
        passwordField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkFieldNotEmpty(passwordWrapper, passwordField)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        confirmWrapper = view.findViewById(R.id.confirmation_wrapper)
        confirmField = view.findViewById(R.id.comfirmation_field)
        confirmField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validatePasswordMatch()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        return view
    }

    private fun validatePasswordMatch(): Boolean {
        confirmWrapper.isErrorEnabled = false
        if (passwordField.text != null && confirmField.text != null &&
            confirmField.text.toString() != passwordField.text.toString()
            && confirmField.text.toString().isNotEmpty()
        ) {
            confirmWrapper.error = "Passwords do not match."
            confirmWrapper.isErrorEnabled = true
            return false
        }
        return true
    }

    override val isDataValid: Boolean
        get() = if (checkFieldNotEmpty(usernameWrapper, usernameField) &&
            checkFieldNotEmpty(passwordWrapper, passwordField) &&
            checkFieldNotEmpty(confirmWrapper, confirmField) &&
            validatePasswordMatch()
        ) {
            data.put(R.id.username_field, usernameField.text.toString().trim { it <= ' ' })
            data.put(R.id.password_field, passwordField.text.toString().trim { it <= ' ' })
            true
        } else {
            false
        }

    companion object {
        @JvmStatic
        fun newInstance(): StepFragment {
            return FragmentCredentials()
        }
    }
}