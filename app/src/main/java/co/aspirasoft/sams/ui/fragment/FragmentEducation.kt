package co.aspirasoft.sams.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import co.aspirasoft.sams.R

class FragmentEducation : StepFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: populateSpinner(view, R.id.selectUniversity, R.array.unis);
        // TODO: populateSpinner(view, R.id.selectSchool, R.array.schools);
        // TODO: populateSpinner(view, R.id.selectDepartment, R.array.depts);
        // TODO: populateSpinner(view, R.id.selectSemester, R.array.sems);
        return inflater.inflate(R.layout.fragment_academic_info, container, false)
    }

    // TODO: Process academic info
    override val isDataValid: Boolean
        get() =// TODO: Process academic info
            true

    private fun populateSpinner(view: View, spinnerResId: Int, dataResId: Int) {
        val spinner = view.findViewById<Spinner>(spinnerResId)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            view.context,
            dataResId, android.R.layout.simple_spinner_item
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(): StepFragment {
            return FragmentEducation()
        }
    }
}