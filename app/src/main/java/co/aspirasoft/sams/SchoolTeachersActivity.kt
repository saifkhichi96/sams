package co.aspirasoft.sams

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import co.aspirasoft.adapter.ModelViewAdapter
import co.aspirasoft.sams.core.DashboardChildActivity
import co.aspirasoft.sams.dao.Invite
import co.aspirasoft.sams.dao.InvitesDao
import co.aspirasoft.sams.dao.UsersDao
import co.aspirasoft.sams.model.Credentials
import co.aspirasoft.sams.model.Teacher
import co.aspirasoft.sams.model.User
import co.aspirasoft.sams.view.TeacherView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_list.*

/**
 * SchoolTeachersActivity shows a list of staff members.
 *
 * Purpose of this activity is to allow schools to manage their staff members.
 *
 * @property status Invite status (Pending/Accepted) of the teachers being displayed.
 * @property invites List of emails addresses of teachers to display.
 * @property teachers List of [Teacher]s to display.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
class SchoolTeachersActivity : DashboardChildActivity() {

    private lateinit var status: String
    private lateinit var invites: ArrayList<Invite>

    private var teachers: ArrayList<Teacher> = ArrayList()
    private var adapter: TeacherAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Read invitation status from intent
        val status = intent.getStringExtra(MyApplication.EXTRA_INVITE_STATUS)
        if (status == null) {
            finish()
            return
        }
        this.status = status

        // Read staff list from intent
        val invites = intent.getParcelableArrayListExtra<Invite>(MyApplication.EXTRA_INVITES)
        if (invites == null) {
            finish()
            return
        }
        this.invites = invites

        supportActionBar?.title = if (status == getString(R.string.accepted)) {
            getString(R.string.title_staff_joined)
        } else {
            getString(R.string.title_staff_invited)
        }

        addButton.visibility = View.GONE
    }

    override fun updateUI(currentUser: User) {
        teachers.clear()
        for (invite in invites) {
            val teacher = Teacher("", "", Credentials(invite.invitee, ""))
            if (status == getString(R.string.accepted)) {
                UsersDao.getUserByEmail(currentUser.id, invite.invitee, OnSuccessListener {
                    if (it != null && it is Teacher) {
                        teacher.updateWith(it)
                        teacher.notifyObservers()
                    }
                })
            }

            teachers.add(teacher)
        }

        adapter = TeacherAdapter(this, teachers)
        contentList.adapter = adapter
    }

    private inner class TeacherAdapter(context: Context, val teachers: List<Teacher>)
        : ModelViewAdapter<Teacher>(context, teachers, TeacherView::class) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val v = super.getView(position, convertView, parent)
            val teacher = teachers[position]
            val email = teacher.email
            v.findViewById<View>(R.id.revokeInviteButton).setOnClickListener {
                for (invite in invites) {
                    if (invite.invitee == email) {
                        Snackbar.make(
                                contentList,
                                String.format(getString(R.string.confirm_revoke_invite), email),
                                Snackbar.LENGTH_INDEFINITE
                        ).setAction(getString(R.string.label_revoke)) {
                            InvitesDao.remove(currentUser.id, invite, OnSuccessListener {
                                invites.remove(invite)
                                this@SchoolTeachersActivity.teachers.remove(teacher)
                                this@SchoolTeachersActivity.adapter?.notifyDataSetChanged()
                            })
                        }.show()
                        break
                    }
                }
            }

            v.setOnClickListener {
                startSecurely(ProfileActivity::class.java, Intent().apply {
                    putExtra(MyApplication.EXTRA_PROFILE_USER, teacher)
                })
            }
            return v
        }

    }

}