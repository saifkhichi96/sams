package co.aspirasoft.sams

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.view.iterator
import co.aspirasoft.sams.core.DashboardActivity
import co.aspirasoft.sams.dao.Invite
import co.aspirasoft.sams.dao.InvitesDao
import co.aspirasoft.sams.model.School
import co.aspirasoft.sams.model.User
import co.aspirasoft.sams.tasks.InvitationTask
import co.aspirasoft.sams.view.EmailsInputDialog
import co.aspirasoft.util.InputUtils.isNotBlank
import co.aspirasoft.util.InputUtils.showError
import co.aspirasoft.util.ViewUtils.hideKeyboard
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_school.*

/**
 * SchoolDashboardActivity is the schools' homepage.
 *
 * Purpose of this activity is to provide a UI to the schools
 * which allows them to perform certain administrative tasks.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class SchoolDashboardActivity : DashboardActivity() {

    private val invitedStaff = ArrayList<Invite>()
    private val joinedStaff = ArrayList<Invite>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
    }

    override fun onStart() {
        super.onStart()
        trackSentInvites() // start the live counters
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menu?.iterator()?.forEach { it.icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP) }
        return true
    }

    /**
     * Displays the signed in school's details.
     */
    override fun updateUI(currentUser: User) {
        if (currentUser is School) {
            schoolName.text = currentUser.name
            schoolEmail.text = currentUser.email
            schoolCode.setImageBitmap(QRGEncoder(currentUser.id, null, QRGContents.Type.TEXT, 512).bitmap)

            showStaffStats(0, 0)
        } else finish()
    }

    fun onManageClassesClicked(v: View) {
        startSecurely(SchoolClassesActivity::class.java, Intent().apply {
            putExtra(MyApplication.EXTRA_INVITES, joinedStaff)
        })
    }

    fun onManageSubjectsClicked(v: View) {
        startSecurely(SchoolSubjectsActivity::class.java, Intent().apply {
            putExtra(MyApplication.EXTRA_INVITES, joinedStaff)
        })
    }

    /**
     * Handles clicks on single invite button.
     *
     * Reads an email address from [emailField] and tries generating an invite
     * for this address. A blocking progress box is displayed while the invite
     * is being sent.
     */
    fun onInviteSingleClicked(v: View) {
        if (emailField.isNotBlank()) {
            hideKeyboard()
            val email = emailField.text.toString().trim()
            val progressDialog = ProgressDialog.show(
                    this,
                    getString(R.string.status_invitation_sending),
                    String.format(getString(R.string.status_invitation_progress), email),
                    true
            )

            inviteSingleEmail(email, OnCompleteListener {
                progressDialog.dismiss()
                if (!it.isSuccessful) {
                    emailField.showError(it.exception?.message ?: getString(R.string.status_invitation_failed))
                }
            })
        }
    }

    /**
     * Handles clicks on multiple invites button.
     *
     * Opens a [EmailsInputDialog] where the user can input multiple email
     * addresses in a form. Invites for all valid emails are generated on
     * successful input.
     *
     * A blocking progress box is displayed while the invites are being
     * sent. Status of each sent invite is displayed.
     */
    fun onInviteMultipleClicked(v: View) {
        hideKeyboard()
        EmailsInputDialog(this)
                .setOnEmailsReceivedListener { emails ->
                    inviteMultipleEmails(emails)
                }
                .show()
    }

    /**
     * Handles clicks on invited staff button.
     *
     * Opens the [SchoolTeachersActivity] with a list of staff members who have pending
     * invitations.
     */
    fun onInvitedStaffClicked(v: View) {
        hideKeyboard()
        if (invitedStaff.size > 0) {
            startSecurely(SchoolTeachersActivity::class.java, Intent().apply {
                putExtra(MyApplication.EXTRA_INVITE_STATUS, getString(R.string.pending))
                putExtra(MyApplication.EXTRA_INVITES, invitedStaff)
            })
        } else {
            Snackbar.make(joinedStaffButton, getString(R.string.error_no_staff_invited), Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Handles clicks on joined staff button.
     *
     * Opens the [SchoolTeachersActivity] with a list of staff members who have accepted
     * their invitations and joined the app.
     */
    fun onJoinedStaffClicked(v: View) {
        hideKeyboard()
        if (joinedStaff.size > 0) {
            startSecurely(SchoolTeachersActivity::class.java, Intent().apply {
                putExtra(MyApplication.EXTRA_INVITE_STATUS, getString(R.string.accepted))
                putExtra(MyApplication.EXTRA_INVITES, joinedStaff)
            })
        } else {
            Snackbar.make(joinedStaffButton, getString(R.string.error_no_staff_joined), Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Sends a link to [email] which can be used to create a Teacher account.
     *
     * @param listener Optional callback to listen for completion of invitation task.
     */
    private fun inviteSingleEmail(email: String, listener: OnCompleteListener<Void?>? = null) {
        InvitationTask(this, currentUser.id, email, schoolId)
                .start { task ->
                    if (task.isSuccessful || task.exception == null) {
                        Snackbar.make(
                                schoolActivity,
                                getString(R.string.status_invitation_sent),
                                Snackbar.LENGTH_LONG
                        ).show()
                        emailField.setText("")
                    } else {
                        emailField.showError(task.exception?.message ?: getString(R.string.status_invitation_failed))
                    }

                    listener?.let { task.addOnCompleteListener(it) }
                }
    }

    /**
     * Sends links to [emails] which can be used to create Teacher accounts.
     */
    private fun inviteMultipleEmails(emails: List<String>) {
        val invitees = emails.size
        var invited = 1

        val progressDialog = ProgressDialog.show(
                this,
                String.format(getString(R.string.status_invitations_sending), invited, invitees),
                String.format(getString(R.string.status_invitations_progress), invited, invitees),
                true
        )

        var status = ""
        for (email in emails) {
            inviteSingleEmail(email, OnCompleteListener {
                progressDialog.setTitle(String.format(getString(R.string.status_invitations_sending), invited, invitees))

                synchronized(status) {
                    status += if (it.isSuccessful) {
                        "$email invited.\n\n"
                    } else {
                        "$email ${it.exception?.message ?: "already exists"}.\n\n"
                    }
                    synchronized(progressDialog) {
                        progressDialog.setMessage(status)
                    }
                }

                invited += 1
                if (invited > invitees) {
                    progressDialog.setTitle(getString(R.string.status_invitations_sent))
                    progressDialog.setCancelable(true)
                    Handler().postDelayed({ progressDialog.dismiss() }, 5000L)
                }
            })
        }
    }

    /**
     * Displays number of invited and joined staff members.
     */
    private fun showStaffStats(invited: Int, joined: Int) {
        invitedStaffButton.text = String.format(getString(R.string.label_staff_invited), invited)
        joinedStaffButton.text = String.format(getString(R.string.label_staff_joined), joined)
    }

    /**
     * Listens for changes in status of sent invites.
     *
     * Monitors all sent invites and displays the number of accepted
     * and pending invites in realtime.
     */
    private fun trackSentInvites() {
        InvitesDao.getAll(currentUser.id, OnSuccessListener {
            invitedStaff.clear()
            joinedStaff.clear()
            it.orEmpty().forEach { invite ->
                if (invite.sender == schoolId) {
                    when (invite.status) {
                        MyApplication.STATUS_INVITE_PENDING -> invitedStaff.add(invite)
                        else -> joinedStaff.add(invite)
                    }
                }
            }
            showStaffStats(invitedStaff.size, joinedStaff.size)
        })
    }

}