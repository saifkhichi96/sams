package co.aspirasoft.sams.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import co.aspirasoft.sams.R
import java.text.SimpleDateFormat
import java.util.*

class ActivitySchedule : AppCompatActivity(), View.OnClickListener {

    private lateinit var classesAll: Button
    private lateinit var classesToday: Button
    private lateinit var classesOnDay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_schedule)

        setSupportActionBar(findViewById<View>(R.id.actionBar) as Toolbar)
        val actionBar = supportActionBar
        actionBar!!.title = "Class Schedule"

        classesAll = findViewById(R.id.classesAll)
        classesToday = findViewById(R.id.classesToday)
        classesOnDay = findViewById(R.id.classesOnDay)
        classesAll.setOnClickListener(this)
        classesToday.setOnClickListener(this)
        classesOnDay.setOnClickListener(this)

        val networks = Course("Computer Networks")
        networks.addLecture("Lab at Turing Lab, SEECS", "(09:00 AM - 12:00 PM)", 2)
        networks.addLecture("Lecture at CR#5, SEECS", "(03:00 PM - 04:00 PM)", 5)
        networks.inflate(findViewById<View>(R.id.allClasses) as LinearLayout)

        val database = DayView(1)
        database.addLecture("Database Systems", "Lab at Babbage Lab, SEECS", "(01:00 PM - 04:00 PM)")
        database.addLecture("Linear Algebra", "Lecture at CR#14, SEECS", "(10:00 AM - 11:00 AM)")
        database.inflate(findViewById<View>(R.id.dayClasses) as LinearLayout)

        val today = SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis())
        val oop: DayView = when (today) {
            "Monday" -> DayView(1)
            "Tuesday" -> DayView(2)
            "Wednesday" -> DayView(3)
            "Thursday" -> DayView(4)
            "Friday" -> DayView(5)
            "Saturday" -> DayView(6)
            "Sunday" -> DayView(7)
            else -> DayView(1)
        }
        oop.addLecture("Object Oriented Programming", "Lecture at CR#1, RIMMS", "(03:00 PM - 04:00 PM)")
        oop.addLecture("Discrete Mathematics", "Lecture at CR#11, SEECS", "(11:00 AM - 12:00 AM)")
        oop.inflate(findViewById<View>(R.id.todayClasses) as LinearLayout)
    }

    public override fun onStart() {
        super.onStart()
        updateView(R.id.classesToday)
    }

    public override fun onResume() {
        super.onResume()
        updateView(R.id.classesToday)
    }

    private fun setActive(button: Button?) {
        button!!.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        // button.setTextColor(getResources().getColor(R.color.colorPureWhite));
    }

    private fun setPassive(button: Button?) {
        // button.setBackgroundColor(getResources().getColor(R.color.colorPureWhite));
        button!!.setTextColor(resources.getColor(R.color.colorPrimaryDark))
    }

    private fun updateView(activeTab: Int) {
        // Deactivate all buttons
        setPassive(classesAll)
        setPassive(classesToday)
        setPassive(classesOnDay)

        // Hide all views
        findViewById<View>(R.id.allClasses).visibility = View.INVISIBLE
        findViewById<View>(R.id.todayClasses).visibility = View.INVISIBLE
        findViewById<View>(R.id.dayClasses).visibility = View.INVISIBLE
        when (activeTab) {
            R.id.classesAll -> {
                setActive(classesAll)
                findViewById<View>(R.id.allClasses).visibility = View.VISIBLE
            }
            R.id.classesToday -> {
                setActive(classesToday)
                findViewById<View>(R.id.todayClasses).visibility = View.VISIBLE
            }
            R.id.classesOnDay -> {
                setActive(classesOnDay)
                findViewById<View>(R.id.dayClasses).visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actions_day_schedule, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_signOut -> {
                val editor = getSharedPreferences(ActivitySplash.Companion.PREFS_FILE, 0).edit()
                editor.clear()
                editor.apply()
                Toast.makeText(applicationContext, "You are now logged out.", Toast.LENGTH_SHORT)
                startActivity(Intent(applicationContext, ActivitySignIn::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        updateView(v.id)
    }

    private inner class Course(var course: String) {
        var lectures: ArrayList<Lecture> = ArrayList()

        fun addLecture(location: String, time: String, day: Int) {
            lectures.add(Lecture(location, time, day))
        }

        fun inflate(parent: ViewGroup) {
            layoutInflater.inflate(R.layout.template_course_card, parent)
            val card = parent.getChildAt(parent.childCount - 1) as LinearLayout
            setCourse(course, card)
            for (lecture in lectures) lecture.inflate(card)
        }

        private fun setCourse(course: String, parent: ViewGroup) {
            layoutInflater.inflate(R.layout.template_course_heading, parent)
            (parent.getChildAt(parent.childCount - 1) as TextView).text = course
        }

        private inner class Lecture(var location: String, var time: String, var day: Int) {
            fun inflate(parent: ViewGroup) {
                layoutInflater.inflate(R.layout.template_course_card, parent)
                val card = parent.getChildAt(parent.childCount - 1) as LinearLayout
                setDay(day, card)
                setVenue(location, card)
                setTime(time, card)
            }

            private fun setDay(lectureDay: Int, parent: LinearLayout) {
                var lectureDay = lectureDay
                layoutInflater.inflate(R.layout.template_day_bar, parent)
                val linearLayout = parent.getChildAt(parent.childCount - 1) as LinearLayout
                if (lectureDay <= 0 || lectureDay > 7) lectureDay = 1
                val button = linearLayout.getChildAt(lectureDay - 1) as Button
                button.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                // button.setTextColor(getResources().getColor(R.color.colorPureWhite));
            }

            private fun setTime(lectureTime: String, parent: LinearLayout) {
                layoutInflater.inflate(R.layout.template_lecture_time, parent)
                (parent.getChildAt(parent.childCount - 1) as TextView).text = lectureTime
            }

            private fun setVenue(lectureVenue: String, parent: LinearLayout) {
                layoutInflater.inflate(R.layout.template_lecture_location, parent)
                (parent.getChildAt(parent.childCount - 1) as TextView).text = lectureVenue
            }
        }

    }

    private inner class DayView(var day: Int) {
        var lectures: ArrayList<Lecture>
        fun inflate(parent: ViewGroup) {
            setDay(day, parent)
            for (lecture in lectures) {
                lecture.inflate(parent)
            }
        }

        fun addLecture(course: String, location: String, time: String) {
            lectures.add(Lecture(course, location, time))
        }

        private fun setDay(lectureDay: Int, parent: ViewGroup) {
            var lectureDay = lectureDay
            layoutInflater.inflate(R.layout.template_day_bar, parent)
            val linearLayout = parent.getChildAt(parent.childCount - 1) as LinearLayout
            if (lectureDay <= 0 || lectureDay > 7) lectureDay = 1
            val button = linearLayout.getChildAt(lectureDay - 1) as Button
            button.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            // button.setTextColor(getResources().getColor(R.color.colorPureWhite));
        }

        private inner class Lecture(var course: String, var location: String, var time: String) {
            fun inflate(parent: ViewGroup) {
                layoutInflater.inflate(R.layout.template_course_card, parent)
                val card = parent.getChildAt(parent.childCount - 1) as LinearLayout
                setCourse(course, card)
                setVenue(location, card)
                setTime(time, card)
            }

            private fun setCourse(course: String, parent: ViewGroup) {
                layoutInflater.inflate(R.layout.template_course_heading, parent)
                (parent.getChildAt(parent.childCount - 1) as TextView).text = course
            }

            private fun setTime(lectureTime: String, parent: LinearLayout) {
                layoutInflater.inflate(R.layout.template_lecture_time, parent)
                (parent.getChildAt(parent.childCount - 1) as TextView).text = lectureTime
            }

            private fun setVenue(lectureVenue: String, parent: LinearLayout) {
                layoutInflater.inflate(R.layout.template_lecture_location, parent)
                (parent.getChildAt(parent.childCount - 1) as TextView).text = lectureVenue
            }
        }

        init {
            lectures = ArrayList()
        }
    }
}