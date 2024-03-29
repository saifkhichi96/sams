package co.aspirasoft.sams

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import co.aspirasoft.adapter.ModelViewAdapter
import co.aspirasoft.sams.core.DashboardChildActivity
import co.aspirasoft.sams.dao.AttendanceDao
import co.aspirasoft.sams.dao.UsersDao
import co.aspirasoft.sams.model.Attendance
import co.aspirasoft.sams.model.AttendanceRecord
import co.aspirasoft.sams.model.Teacher
import co.aspirasoft.sams.model.User
import co.aspirasoft.sams.view.MarkAttendanceView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_list.contentList
import kotlinx.android.synthetic.main.activity_mark_attendance.*

class MarkAttendanceActivity : DashboardChildActivity() {

    private lateinit var attendance: Attendance

    private lateinit var currentTeacher: Teacher
    private lateinit var adapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_attendance)

        // Only allow a class teacher to access this page
        currentTeacher = when (currentUser) {
            is Teacher -> {
                val teacher = currentUser as Teacher
                if (teacher.classId == null) {
                    finish()
                    return
                }
                teacher
            }
            else -> {
                finish()
                return
            }
        }

        attendance = Attendance(currentTeacher.classId!!)
        dateView.text = attendance.date

        adapter = AttendanceAdapter(this, attendance.attendanceRecords)
        contentList.adapter = adapter

        saveButton.setOnClickListener {
            val status = Snackbar.make(contentList, getString(R.string.status_saving), Snackbar.LENGTH_INDEFINITE)
            status.show()
            AttendanceDao.add(schoolId, attendance, OnCompleteListener {
                status.setText(getString(R.string.status_saved))
                Handler().postDelayed({ status.dismiss() }, 2500L)
            })
        }
    }

    override fun updateUI(currentUser: User) {
        currentTeacher.classId?.let { classId ->
            UsersDao.getStudentsInClass(schoolId, classId, OnSuccessListener { it ->
                attendance.attendanceRecords.clear()
                it.forEach {
                    val record = AttendanceRecord().apply {
                        this.studentName = it.name
                        this.studentRollNo = it.rollNo
                        this.date = this@MarkAttendanceActivity.attendance.date
                        this.classId = classId
                        this.attendance = false
                    }
                    attendance.attendanceRecords.add(record)
                }
                adapter.notifyDataSetChanged()

                AttendanceDao.getByDate(schoolId, classId, attendance.date, OnSuccessListener { savedAttendance ->
                    savedAttendance?.attendanceRecords?.forEach { savedRecord ->
                        attendance.updateRecord(savedRecord.studentRollNo!!, savedRecord.attendance)
                    }
                    adapter.notifyDataSetChanged()
                })
            })
        }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.close_attendance))
                .setMessage(getString(R.string.confirm_close_editor))
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton(android.R.string.no) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }

    private inner class AttendanceAdapter(context: Context, val records: List<AttendanceRecord>)
        : ModelViewAdapter<AttendanceRecord>(context, records, MarkAttendanceView::class) {

        override fun notifyDataSetChanged() {
            records.sortedBy { it.studentRollNo }
            super.notifyDataSetChanged()
        }

    }

}