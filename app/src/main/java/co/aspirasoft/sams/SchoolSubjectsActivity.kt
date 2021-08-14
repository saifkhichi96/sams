package co.aspirasoft.sams

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import co.aspirasoft.adapter.ModelViewAdapter
import co.aspirasoft.sams.core.DashboardChildActivity
import co.aspirasoft.sams.dao.ClassesDao
import co.aspirasoft.sams.dao.Invite
import co.aspirasoft.sams.model.Subject
import co.aspirasoft.sams.model.User
import co.aspirasoft.sams.view.AddSubjectDialog
import co.aspirasoft.sams.view.SubjectView
import co.aspirasoft.util.ViewUtils.showError
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_list.*

class SchoolSubjectsActivity : DashboardChildActivity() {

    private val classes: ArrayList<String> = ArrayList()
    private val teachers: ArrayList<String> = ArrayList()
    private val subjects: ArrayList<Subject> = ArrayList()

    private lateinit var adapter: SubjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Read staff list from intent
        val invites = intent.getParcelableArrayListExtra<Invite>(MyApplication.EXTRA_INVITES)
        if (invites == null) {
            finish()
            return
        }
        invites.forEach { this.teachers.add(it.invitee) }

        adapter = SubjectAdapter(this, subjects)
        contentList.adapter = adapter
        contentList.divider = null

        addButton.setOnClickListener { onAddClassClicked() }
    }

    override fun updateUI(currentUser: User) {
        ClassesDao.getClassesAtSchool(schoolId, OnSuccessListener {
            classes.clear()
            it?.forEach { schoolClass ->
                classes.add(schoolClass.name)
                schoolClass.subjects?.values?.forEach { subject ->
                    if (!subjects.contains(subject)) subjects.add(subject)
                }
            }
            adapter.notifyDataSetChanged()
        })
    }

    private fun onAddClassClicked() {
        if (classes.isNotEmpty()) {
            val dialog = AddSubjectDialog.newInstance(classes, teachers, currentUser.id)
            dialog.onOkListener = { subject ->
                subjects.add(subject)
                adapter.notifyDataSetChanged()
            }
            dialog.show(supportFragmentManager, dialog.toString())
        } else {
            showError(contentList, getString(R.string.error_missing_classes))
        }
    }

    private inner class SubjectAdapter(context: Context, val subjects: ArrayList<Subject>)
        : ModelViewAdapter<Subject>(context, subjects, SubjectView::class) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val v = super.getView(position, convertView, parent)
            v.findViewById<View>(R.id.subjectColor)?.apply {
                layoutParams = layoutParams.apply {
                    this.width = 25
                }
            }
            v.findViewById<MaterialCardView>(R.id.subjectCard)?.apply {
                useCompatPadding = false
                cardElevation = 0f
                radius = 0f
                setCardBackgroundColor(ColorStateList.valueOf(Color.TRANSPARENT))
            }
            v.setOnClickListener {
                val dialog = AddSubjectDialog.newInstance(classes, teachers, currentUser.id, subjects[position])
                dialog.onOkListener = { subject ->
                    subjects[position] = subject
                    adapter.notifyDataSetChanged()
                }
                dialog.show(supportFragmentManager, dialog.toString())
            }
            (v as SubjectView).setSubjectTeacherVisible(true)
            return v
        }

    }

}