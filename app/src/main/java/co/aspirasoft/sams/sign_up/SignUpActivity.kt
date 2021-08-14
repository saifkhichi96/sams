package co.aspirasoft.sams.sign_up

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import co.aspirasoft.sams.MyApplication
import co.aspirasoft.sams.R
import co.aspirasoft.sams.SignInActivity
import co.aspirasoft.sams.dao.Invite
import co.aspirasoft.sams.dao.InvitesDao
import co.aspirasoft.sams.dao.UsersDao
import co.aspirasoft.sams.model.*
import co.aspirasoft.util.ViewUtils.showError
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.reflect.KClass


/**
 * SignUpActivity allows a user to complete their registration.
 *
 * Purpose of this activity is to let users sign up for new accounts.
 *
 * Users are only allowed to sign up if they have a valid [referralCode].
 * This code is issued by a [School] for creating [Teacher] account, and
 * by [Teacher] for creating [Student] accounts.
 *
 * @property accountType The type of user account to be created.
 * @property emailLink The magic link to use for this sign up.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class SignUpActivity : AppCompatActivity() {

    private lateinit var referralCode: String
    private lateinit var emailLink: String
    lateinit var accountType: KClass<out User>

    /**
     * Reference to the sent invite, or null if no invite found.
     *
     * This value is retrieved from database on sign up time, and must be
     * non-null for sign up to proceed.
     */
    private var invite: Invite? = null

    /**
     * The id of the [SchoolClass] to which this user belongs.
     *
     * This should be defined for all students and class teachers, and
     * `null` for regular teachers.
     */
    private var classId: String? = null

    /**
     * The roll number of the [Student] who is registring.
     *
     * This is only defined if current user is a student, else `null`.
     */
    private var rollNumber: String? = null

    /**
     * Is this a [Student] signing up for new account?
     */
    private val isStudentSignUp get() = classId != null && rollNumber != null

    /**
     * Overrides the onCreate activity lifecycle method.
     *
     * Sign up parameters are received and checked here. Account creation
     * only proceeds if all parameters are correct.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Read sign up parameters from intent
        emailLink = intent.data.toString()
        referralCode = intent.getStringExtra(MyApplication.EXTRA_REFERRAL_CODE) ?: ""
        classId = intent.getStringExtra(MyApplication.EXTRA_STUDENT_CLASS_ID)
        rollNumber = intent.getStringExtra(MyApplication.EXTRA_STUDENT_ROLL_NO)
        val accountType = User.valueOf(intent.getStringExtra(MyApplication.EXTRA_ACCOUNT_TYPE))

        // Sign up cannot proceed if no referral code or account type provided
        if (referralCode.isBlank() || accountType == null) {
            finish()
            return
        }
        this.accountType = accountType
        if (accountType == Student::class && (rollNumber.isNullOrBlank() || classId.isNullOrBlank())) {
            finish()
            return
        }

        // Set up views
        wizardView.setupWithWizardSteps(supportFragmentManager, when {
            this.isStudentSignUp -> listOf(
                    CreateAccountStep(),
                    IntroductionStep(),
                    ContactInfoStep(),
                    EmergencyInfoStep()
            )
            else -> listOf(
                    CreateAccountStep(),
                    IntroductionStep(),
                    ContactInfoStep()
            )
        })
        wizardView.setOnSubmitListener {
            findViewById<Button>(R.id.nextButton).text = getString(R.string.status_signing_up)
            findViewById<Button>(R.id.nextButton).isEnabled = false
            onSubmit(it)
        }
    }

    /**
     * Callback for when the sign up form is submitted.
     *
     * This method retrieves form data and creates a valid instance of
     * a [User] subclass representing the current user.
     */
    private fun onSubmit(data: SparseArray<Any>) {
        val classId = classId ?: ""
        val rollNumber = rollNumber ?: ""

        // Account credentials
        val password = data[R.id.passwordField, ""].toString()

        // Personal info
        val imageUri = data[R.id.userImage, ""].toString() // TODO: Upload image to FirebaseStorage
        val name = data[R.id.nameField, ""].toString()
        val dateOfBirth = data[R.id.dateOfBirthField, ""].toString()
        val gender = data[R.id.genderField, ""].toString()

        // Contact info
        val email = data[R.id.emailField, ""].toString()
        val address = data[R.id.streetField, ""].toString()
        val phone = data[R.id.phoneField, ""].toString()

        // Emergency info
        val bloodType = data[R.id.bloodTypeField, ""].toString()
        val emergencyContact = data[R.id.emergencyContactNameField, ""].toString()
        val emergencyEmail = data[R.id.emergencyContactEmailField, ""].toString()
        val emergencyPhone = data[R.id.emergencyContactPhoneField, ""].toString()

        when {
            isStudentSignUp -> Student().apply {
                this.classId = classId
                this.rollNo = rollNumber
                this.dateOfBirth = dateOfBirth
                this.bloodType = bloodType
                this.emergencyContact = emergencyContact
                this.emergencyEmail = emergencyEmail
                this.emergencyPhone = emergencyPhone
            }
            else -> Teacher().apply {
                this.classId = classId
            }
        }.also {
            it.name = name
            it.credentials = Credentials(email, "")
            it.address = address
            it.phone = phone
            it.gender = gender

            onUserCreated(it, password)
        }
    }

    /**
     * Callback for when a valid user has been created.
     *
     * This method checks the validates the invite before proceeding with
     * the sign up process. Validity checks include confirming existence
     * of school in database, checking that the invitation still exists
     * and has a `Pending` state.
     */
    private fun onUserCreated(user: User, password: String) {
        FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener {
            InvitesDao.get(
                    referralCode,
                    user.email,
                    OnSuccessListener {
                        invite = it
                        when (invite?.status) {
                            getString(R.string.pending) -> onVerificationSuccess(user, password)
                            getString(R.string.accepted) -> onFailure(getString(R.string.error_invalid_invitation))
                            else -> onFailure()
                        }
                    }
            )
        }
    }

    /**
     * Callback for when invitation verification succeeds.
     *
     * This method performs the actual sign up task.
     */
    private fun onVerificationSuccess(user: User, password: String) {
        val auth = FirebaseAuth.getInstance()
        val anonUser = auth.currentUser

        if (auth.isSignInWithEmailLink(emailLink)) {
            val credential = EmailAuthProvider.getCredentialWithLink(user.email, emailLink)
            anonUser!!.linkWithCredential(credential)
                    .addOnSuccessListener { result ->
                        result.user?.let { firebaseUser ->
                            user.id = firebaseUser.uid
                            UsersDao.add(referralCode, user, invite!!, OnCompleteListener {
                                if (it.isSuccessful) {
                                    firebaseUser.updatePassword(password).addOnSuccessListener {
                                        startActivity(Intent(this, SignInActivity::class.java).apply {
                                            putExtra(MyApplication.EXTRA_NEW_SIGN_UP, true)
                                        })
                                        finish()
                                    }
                                } else {
                                    onFailure(it.exception?.message ?: getString(R.string.status_sign_up_failed))
                                    result.credential?.let { credential ->
                                        firebaseUser.reauthenticate(credential)
                                                .addOnCompleteListener {
                                                    firebaseUser.delete()
                                                }
                                    }
                                }
                            })
                        }
                    }
                    .addOnFailureListener { ex ->
                        onFailure(ex.message ?: getString(R.string.status_sign_up_failed))
                    }
        } else onFailure()
    }

    /**
     * Called when the invitation could not be verified.
     *
     * An error message is displayed.
     *
     * @param error An (optional) description of cause of failure.
     */
    private fun onFailure(error: String? = null) {
        showError(wizardView, error ?: getString(R.string.error_missing_invitation))
        findViewById<Button>(R.id.nextButton).text = getString(R.string.label_sign_up)
        findViewById<Button>(R.id.nextButton).isEnabled = true
    }

    /**
     * Overrides back button behaviour.
     *
     * Asks for a confirmation before closing the activity and canceling sign up.
     */
    override fun onBackPressed() {
        if (!wizardView.onBackPressed()) {
            MaterialAlertDialogBuilder(this)
                    .setMessage(getString(R.string.confirm_cancel_sign_up))
                    .setPositiveButton(android.R.string.yes) { dialog, _ ->
                        super.onBackPressed()
                        dialog.dismiss()
                    }
                    .setNegativeButton(android.R.string.no) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
        }
    }

}