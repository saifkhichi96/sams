package co.aspirasoft.sams.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import co.aspirasoft.sams.R
import co.aspirasoft.sams.model.Student
import co.aspirasoft.sams.storage.ImageLoader
import co.aspirasoft.view.BaseView

class StudentView : BaseView<Student> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var studentImageView: ImageView
    private var studentNameView: TextView
    private var studentRollNoView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_student, this)
        studentImageView = findViewById(R.id.userImage)
        studentNameView = findViewById(R.id.nameField)
        studentRollNoView = findViewById(R.id.rollNo)
    }

    override fun updateView(model: Student) {
        ImageLoader.with(context)
                .load(model)
                .into(studentImageView)

        studentNameView.text = model.name
        studentRollNoView.text = "Roll # ${model.rollNo}"
    }

}