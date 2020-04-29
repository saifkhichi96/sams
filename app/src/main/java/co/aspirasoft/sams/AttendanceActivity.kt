package co.aspirasoft.sams

import android.content.Context
import android.os.Bundle
import android.view.View
import co.aspirasoft.adapter.ModelViewAdapter
import co.aspirasoft.sams.core.DashboardChildActivity
import co.aspirasoft.sams.dao.AttendanceDao
import co.aspirasoft.sams.model.AttendanceRecord
import co.aspirasoft.sams.model.Student
import co.aspirasoft.sams.model.User
import co.aspirasoft.sams.view.AttendanceView
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_list.*

class AttendanceActivity : DashboardChildActivity() {

    private val attendanceRecords = ArrayList<AttendanceRecord>()

    private lateinit var currentStudent: Student
    private lateinit var adapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Only allow a student to access this page
        currentStudent = when (currentUser) {
            is Student -> currentUser as Student
            else -> {
                finish()
                return
            }
        }

        adapter = AttendanceAdapter(this, attendanceRecords)
        contentList.adapter = adapter
        addButton.visibility = View.GONE
    }

    override fun updateUI(currentUser: User) {
        AttendanceDao.getByStudent(schoolId, currentStudent, OnSuccessListener { savedRecords ->
            attendanceRecords.clear()
            savedRecords?.let {
                supportActionBar?.title = String.format("%.1f%% Attendance", calculateAttendancePercentage(it))
                attendanceRecords.addAll(it)
            }
            adapter.notifyDataSetChanged()
        })
    }

    private fun calculateAttendancePercentage(list: List<AttendanceRecord>): Float {
        val presences = list.filter { it.attendance }.size
        val total = list.size
        return 100F * presences / total
    }

    private inner class AttendanceAdapter(context: Context, val records: List<AttendanceRecord>)
        : ModelViewAdapter<AttendanceRecord>(context, records, AttendanceView::class) {

        override fun notifyDataSetChanged() {
            records.sortedBy { it.date }
            super.notifyDataSetChanged()
        }

    }

}