package co.aspirasoft.sams.ui.activity

import android.util.SparseArray
import android.widget.Toast
import co.aspirasoft.sams.R
import co.aspirasoft.sams.model.Account
import co.aspirasoft.sams.model.Student
import co.aspirasoft.sams.ui.fragment.FragmentCredentials
import co.aspirasoft.sams.ui.fragment.FragmentEducation
import co.aspirasoft.sams.ui.fragment.FragmentStudentType
import co.aspirasoft.sams.ui.fragment.FragmentUserDetails

class ActivitySignUp : WizardActivity(
    arrayOf(
        FragmentCredentials.newInstance(),
        FragmentUserDetails.newInstance(),
        FragmentStudentType.newInstance(),
        FragmentEducation.newInstance()
    ),
    arrayOf(
        "Sign Up",
        "Say Hi",
        "About You",
        "Education"
    )
) {
    override fun submitForm(wizardData: SparseArray<String?>) {
        val student = Student()
        student.name = wizardData[R.id.name_field]
        student.type = wizardData[R.id.student_type_field]
        student.account = Account(
            wizardData[R.id.username_field]!!,
            wizardData[R.id.password_field]!!
        )

        Toast.makeText(this, student.toString(), Toast.LENGTH_LONG).show()
        // TODO: Submit form data
    }
}