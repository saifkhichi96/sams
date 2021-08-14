package co.aspirasoft.sams;

import android.util.SparseArray;
import android.widget.Toast;
import co.aspirasoft.sams.model.users.Student;
import co.aspirasoft.sams.model.users.accounts.Account;

public class ActivitySignUp extends WizardActivity {

    protected ActivitySignUp() {
        super(new StepFragment[]{
                        FragmentCredentials.newInstance(),
                        FragmentUserDetails.newInstance(),
                        FragmentStudentType.newInstance(),
                        FragmentEducation.newInstance(),
                },
                new String[]{
                        "Sign Up",
                        "Say Hi",
                        "About You",
                        "Education",
                }
        );
    }

    @Override
    protected void submitForm(SparseArray<String> wizardData) {
        Student student = new Student();
        student.setName(wizardData.get(R.id.name_field));
        student.setType(wizardData.get(R.id.student_type_field));
        student.setAccount(new Account(
                wizardData.get(R.id.username_field),
                wizardData.get(R.id.password_field))
        );

        Toast.makeText(this, student.toString(), Toast.LENGTH_LONG).show();
        // TODO: Submit form data
    }

}