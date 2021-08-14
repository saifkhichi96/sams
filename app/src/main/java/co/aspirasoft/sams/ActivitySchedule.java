package co.aspirasoft.sams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ActivitySchedule extends AppCompatActivity implements View.OnClickListener {
    Button classesAll;
    Button classesToday;
    Button classesOnDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        setSupportActionBar((Toolbar) findViewById(R.id.actionBar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Class Schedule");

        classesAll = findViewById(R.id.classesAll);
        classesToday = findViewById(R.id.classesToday);
        classesOnDay = findViewById(R.id.classesOnDay);

        classesAll.setOnClickListener(this);
        classesToday.setOnClickListener(this);
        classesOnDay.setOnClickListener(this);

        Course networks = new Course("Computer Networks");
        networks.addLecture("Lab at Turing Lab, SEECS", "(09:00 AM - 12:00 PM)", 2);
        networks.addLecture("Lecture at CR#5, SEECS", "(03:00 PM - 04:00 PM)", 5);
        networks.inflate((LinearLayout) findViewById(R.id.allClasses));

        DayView database = new DayView(1);
        database.addLecture("Database Systems", "Lab at Babbage Lab, SEECS", "(01:00 PM - 04:00 PM)");
        database.addLecture("Linear Algebra", "Lecture at CR#14, SEECS", "(10:00 AM - 11:00 AM)");
        database.inflate((LinearLayout) findViewById(R.id.dayClasses));

        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        DayView oop;
        switch (today) {
            case "Monday":
                oop = new DayView(1);
                break;
            case "Tuesday":
                oop = new DayView(2);
                break;
            case "Wednesday":
                oop = new DayView(3);
                break;
            case "Thursday":
                oop = new DayView(4);
                break;
            case "Friday":
                oop = new DayView(5);
                break;
            case "Saturday":
                oop = new DayView(6);
                break;
            case "Sunday":
                oop = new DayView(7);
                break;
            default:
                oop = new DayView(1);
                break;
        }
        oop.addLecture("Object Oriented Programming", "Lecture at CR#1, RIMMS", "(03:00 PM - 04:00 PM)");
        oop.addLecture("Discrete Mathematics", "Lecture at CR#11, SEECS", "(11:00 AM - 12:00 AM)");
        oop.inflate((LinearLayout) findViewById(R.id.todayClasses));
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView(R.id.classesToday);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(R.id.classesToday);
    }

    private void setActive(Button button) {
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        // button.setTextColor(getResources().getColor(R.color.colorPureWhite));
    }

    private void setPassive(Button button) {
        // button.setBackgroundColor(getResources().getColor(R.color.colorPureWhite));
        button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void updateView(int activeTab) {
        // Deactivate all buttons
        setPassive(classesAll);
        setPassive(classesToday);
        setPassive(classesOnDay);

        // Hide all views
        findViewById(R.id.allClasses).setVisibility(View.INVISIBLE);
        findViewById(R.id.todayClasses).setVisibility(View.INVISIBLE);
        findViewById(R.id.dayClasses).setVisibility(View.INVISIBLE);

        // Activate appropriate button/view
        switch (activeTab) {
            case R.id.classesAll:
                setActive(classesAll);
                findViewById(R.id.allClasses).setVisibility(View.VISIBLE);
                break;
            case R.id.classesToday:
                setActive(classesToday);
                findViewById(R.id.todayClasses).setVisibility(View.VISIBLE);
                break;
            case R.id.classesOnDay:
                setActive(classesOnDay);
                findViewById(R.id.dayClasses).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_day_schedule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signOut:
                SharedPreferences.Editor editor = getSharedPreferences(ActivitySplash.PREFS_FILE, 0).edit();
                editor.clear();
                editor.apply();
                Toast.makeText(getApplicationContext(), "You are now logged out.", Toast.LENGTH_SHORT);
                startActivity(new Intent(getApplicationContext(), ActivitySignIn.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        updateView(v.getId());
    }

    private class Course {
        String course;
        ArrayList<Lecture> lectures;

        Course(String course) {
            this.course = course;
            this.lectures = new ArrayList<>();
        }

        public void addLecture(String location, String time, int day) {
            lectures.add(new Lecture(location, time, day));
        }

        public void inflate(ViewGroup parent) {
            getLayoutInflater().inflate(R.layout.template_course_card, parent);
            LinearLayout card = (LinearLayout) parent.getChildAt(parent.getChildCount() - 1);
            setCourse(course, card);
            for (Lecture lecture : lectures)
                lecture.inflate(card);
        }

        private void setCourse(String course, ViewGroup parent) {
            getLayoutInflater().inflate(R.layout.template_course_heading, parent);
            ((TextView) parent.getChildAt(parent.getChildCount() - 1)).setText(course);
        }

        private class Lecture {
            String location;
            String time;
            int day;

            public Lecture(String location, String time, int day) {
                this.location = location;
                this.time = time;
                this.day = day;
            }

            public void inflate(ViewGroup parent) {
                getLayoutInflater().inflate(R.layout.template_course_card, parent);
                LinearLayout card = (LinearLayout) parent.getChildAt(parent.getChildCount() - 1);

                setDay(day, card);
                setVenue(location, card);
                setTime(time, card);
            }

            private void setDay(int lectureDay, LinearLayout parent) {
                getLayoutInflater().inflate(R.layout.template_day_bar, parent);
                LinearLayout linearLayout = (LinearLayout) parent.getChildAt(parent.getChildCount() - 1);

                if (lectureDay <= 0 || lectureDay > 7) lectureDay = 1;
                Button button = (Button) linearLayout.getChildAt(lectureDay - 1);
                button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                // button.setTextColor(getResources().getColor(R.color.colorPureWhite));
            }

            private void setTime(String lectureTime, LinearLayout parent) {
                getLayoutInflater().inflate(R.layout.template_lecture_time, parent);
                ((TextView) parent.getChildAt(parent.getChildCount() - 1)).setText(lectureTime);
            }

            private void setVenue(String lectureVenue, LinearLayout parent) {
                getLayoutInflater().inflate(R.layout.template_lecture_location, parent);
                ((TextView) parent.getChildAt(parent.getChildCount() - 1)).setText(lectureVenue);
            }
        }
    }

    private class DayView {
        int day;
        ArrayList<Lecture> lectures;

        public DayView(int day) {
            this.day = day;
            lectures = new ArrayList<>();
        }

        public void inflate(ViewGroup parent) {
            setDay(day, parent);
            for (Lecture lecture : lectures) {
                lecture.inflate(parent);
            }
        }

        public void addLecture(String course, String location, String time) {
            lectures.add(new Lecture(course, location, time));
        }

        private void setDay(int lectureDay, ViewGroup parent) {
            getLayoutInflater().inflate(R.layout.template_day_bar, parent);
            LinearLayout linearLayout = (LinearLayout) parent.getChildAt(parent.getChildCount() - 1);

            if (lectureDay <= 0 || lectureDay > 7) lectureDay = 1;
            Button button = (Button) linearLayout.getChildAt(lectureDay - 1);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            // button.setTextColor(getResources().getColor(R.color.colorPureWhite));
        }

        private class Lecture {
            String course;
            String location;
            String time;

            public Lecture(String course, String location, String time) {
                this.course = course;
                this.location = location;
                this.time = time;
            }

            public void inflate(ViewGroup parent) {
                getLayoutInflater().inflate(R.layout.template_course_card, parent);
                LinearLayout card = (LinearLayout) parent.getChildAt(parent.getChildCount() - 1);

                setCourse(course, card);
                setVenue(location, card);
                setTime(time, card);
            }


            private void setCourse(String course, ViewGroup parent) {
                getLayoutInflater().inflate(R.layout.template_course_heading, parent);
                ((TextView) parent.getChildAt(parent.getChildCount() - 1)).setText(course);
            }

            private void setTime(String lectureTime, LinearLayout parent) {
                getLayoutInflater().inflate(R.layout.template_lecture_time, parent);
                ((TextView) parent.getChildAt(parent.getChildCount() - 1)).setText(lectureTime);
            }

            private void setVenue(String lectureVenue, LinearLayout parent) {
                getLayoutInflater().inflate(R.layout.template_lecture_location, parent);
                ((TextView) parent.getChildAt(parent.getChildCount() - 1)).setText(lectureVenue);
            }
        }
    }
}