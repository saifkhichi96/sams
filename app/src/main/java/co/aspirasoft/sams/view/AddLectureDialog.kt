package co.aspirasoft.sams.view

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import co.aspirasoft.sams.R
import co.aspirasoft.sams.model.Subject
import co.aspirasoft.util.InputUtils.showError
import co.aspirasoft.util.ViewUtils.showError
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddLectureDialog : BottomSheetDialogFragment() {

    private lateinit var schoolId: String
    private lateinit var subject: Subject

    private lateinit var lectureDayField: TabLayout
    private lateinit var lectureTimeField: MaterialButton
    private lateinit var lectureDurationField: TextInputEditText
    private lateinit var okButton: Button

    var onDismissListener: ((dialog: DialogInterface) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.dialog_add_lecture, container, false)

        try {
            val args = requireArguments()
            schoolId = args.getString(ARG_SCHOOL_ID)!!
            subject = args.getSerializable(ARG_SUBJECT) as Subject
        } catch (ex: Exception) {
            ex.message?.let { showError(v, it) }
            dismiss()
            return null
        }

        // Get UI references
        lectureDayField = v.findViewById(R.id.lectureDayField)
        lectureTimeField = v.findViewById(R.id.lectureTimeField)
        lectureDurationField = v.findViewById(R.id.lectureDurationField)
        okButton = v.findViewById(R.id.okButton)

        // Set up click handlers
        okButton.setOnClickListener { onOk() }

        // Init views
        lectureDayField.selectTab(lectureDayField.getTabAt(0))
        lectureTimeField.text = String.format(getString(R.string.format_date), 0, 0)
        lectureTimeField.setOnClickListener {
            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                lectureTimeField.text = String.format(getString(R.string.format_date), hourOfDay, minute)
            }, 0, 0, true).show()
        }

        return v
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.let { it(dialog) }
    }

    private fun onOk() {
        val duration = lectureDurationField.text.toString().toIntOrNull()
        if (duration == null) lectureDurationField.showError(getString(R.string.error_required))
        else {
            val startTime = SimpleDateFormat(getString(R.string.format_date_24), Locale.getDefault()).parse(lectureTimeField.text.toString())
            val endTime = Date(startTime!!.time + (duration * 60000))
            val dayOfWeek = lectureDayField.selectedTabPosition + 1

            subject.addAppointment(dayOfWeek, startTime, endTime)
            subject.notifyObservers()
            dismiss()
        }
    }

    companion object {
        private const val ARG_SCHOOL_ID = "school_id"
        private const val ARG_SUBJECT = "teacher_id"

        fun newInstance(
                schoolId: String,
                subject: Subject
        ): AddLectureDialog {
            return AddLectureDialog().also {
                it.arguments = Bundle().apply {
                    putString(ARG_SCHOOL_ID, schoolId)
                    putSerializable(ARG_SUBJECT, subject)
                }
            }
        }
    }

}