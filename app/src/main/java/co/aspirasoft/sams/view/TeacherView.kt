package co.aspirasoft.sams.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import co.aspirasoft.sams.R
import co.aspirasoft.sams.model.Teacher
import co.aspirasoft.sams.storage.ImageLoader
import co.aspirasoft.view.BaseView

class TeacherView : BaseView<Teacher> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var teacherImage: ImageView
    private val teacherName: TextView
    private val teacherEmail: TextView
    private val revokeInviteButton: Button

    init {
        LayoutInflater.from(context).inflate(R.layout.view_teacher, this)
        teacherImage = findViewById(R.id.userImage)
        teacherName = findViewById(R.id.teacherName)
        teacherEmail = findViewById(R.id.teacherEmail)
        revokeInviteButton = findViewById(R.id.revokeInviteButton)
    }

    /**
     * Displays teacher details.
     */
    override fun updateView(model: Teacher) {
        // if the Teacher has not completed sign up yet, show pending view
        if (model.name.isBlank()) {
            this.setOnClickListener(null)
            teacherName.text = model.email
            teacherImage.visibility = View.GONE
            teacherEmail.visibility = View.GONE
            revokeInviteButton.visibility = View.VISIBLE
        }

        // if the Teacher details are completed, show accepted view
        else {
            teacherImage.visibility = View.VISIBLE
            teacherEmail.visibility = View.VISIBLE
            revokeInviteButton.visibility = View.GONE

            ImageLoader.with(context)
                    .load(model)
                    .into(teacherImage)

            teacherName.text = model.name
            teacherEmail.text = when (model.classId.isNullOrBlank()) {
                true -> context.getString(R.string.ph_class)
                else -> model.classId
            }
        }
    }

}