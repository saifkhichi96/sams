package co.aspirasoft.sams.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import co.aspirasoft.sams.R

class FragmentStudentType : StepFragment() {

    private var dayScholar: RadioButton? = null
    private var hostellerButton: RadioButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_type, container, false)
        dayScholar = view.findViewById(R.id.dayScholar)
        hostellerButton = view.findViewById(R.id.hostelite)
        return view
    }

    override val isDataValid: Boolean
        get() = if (dayScholar!!.isChecked || hostellerButton!!.isChecked) {
            data.put(R.id.student_type_field, if (dayScholar!!.isChecked) "Day Scholar" else "Hostellite")
            true
        } else {
            Toast.makeText(
                context,
                "Please select a type of student.",
                Toast.LENGTH_SHORT
            ).show()
            false
        }

    companion object {
        @JvmStatic
        fun newInstance(): StepFragment {
            return FragmentStudentType()
        }
    }
}