package co.aspirasoft.sams.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static final String DB_NAME = "SAMS";

    public Database(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createInstructorTable = "" +
                "CREATE TABLE Instructor (" +
                "   ID int PRIMARY KEY," +
                "   forename varchar(30) NOT NULL," +
                "   surname varchar(30) NOT NULL," +
                "   title enum('Mr.', 'Mrs.', 'Ms.', 'Sir', 'Ma\'am') NOT NULL," +
                "   email varchar(35) UNIQUE," +
                "   phone varchar(11)," +
                "   office varchar(75)" +
                ");";

        String createCourseTable = "" +
                "CREATE TABLE Course (" +
                "   Code varchar(10) PRIMARY KEY," +
                "   name varchar(100) NOT NULL UNIQUE," +
                "   credits varchar(5) NOT NULL," +
                "   instructorID int NOT NULL," +
                "   CONSTRAINT FOREIGN KEY (instructorID) REFERENCES Instructor(ID) ON UPDATE CASCADE ON DELETE SET NULL" +
                ");";

        String createLectureTable = "" +
                "CREATE TABLE Lecture (" +
                "   courseCode varchar(10) NOT NULL," +
                "   day enum('Mon', 'Tue', 'Wed', 'Thur', 'Fri', 'Sat', 'Sun') NOT NULL," +
                "   startTime time NOT NULL UNIQUE," +
                "   duration time NOT NULL," +
                "   location varchar(75) NOT NULL," +
                "   type enum('Lab', 'Lecture', 'Workshop', 'Seminar', 'Other') NOT NULL DEFAULT 'Lecture'," +
                "   CONSTRAINT PRIMARY KEY (courseCode, startTime)," +
                "   FOREIGN KEY (courseCode) REFERENCES Course(Code) ON UPDATE CASCADE ON DELETE CASCADE" +
                ");";

        db.execSQL(createInstructorTable);
        db.execSQL(createCourseTable);
        db.execSQL(createLectureTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private String fixString(String string) {
        string = string.replace("'", " ");
        string = string.replace("\"", " ");
        string = "'" + string + "'";
        return string;
    }

    public void insertInstructor(int id, String fname, String sname, String title, String email, String phone, String office) {
        fname = fixString(fname);
        sname = fixString(sname);
        title = fixString(title);
        email = fixString(email);
        phone = fixString(phone);
        office = fixString(office);

        String query = "" +
                "INSERT INTO Instructor VALUES ('" +
                Integer.toString(id) + "'" +
                fname + sname + title +
                email + phone + office +
                ");";
        this.getWritableDatabase().execSQL(query);
    }

    public void insertCourse(String code, String name, String creditHours, int instructorID) {
        code = fixString(code);
        name = fixString(name);
        creditHours = fixString(creditHours);
        String query = "" +
                "INSERT INTO Course VALUES (" +
                code + name + creditHours +
                "'" + Integer.toString(instructorID) +
                "');";
        this.getWritableDatabase().execSQL(query);
    }

    public void insertLecture(String courseCode, String day, String startTime, String duration, String location, String type) {
        courseCode = fixString(courseCode);
        day = fixString(day);
        startTime = fixString(startTime);
        duration = fixString(duration);
        location = fixString(location);
        type = fixString(type);

        String query = "" +
                "INSERT INTO Lecture VALUES (" +
                courseCode + day + startTime +
                duration + location + type + ");";
        this.getWritableDatabase().execSQL(query);
    }

    public Cursor getCourses() {
        return this.getWritableDatabase().rawQuery("SELECT * FROM Course", null);
    }

    public Cursor getLecturesByCourse(String courseCode) {
        courseCode = fixString(courseCode);
        return this.getWritableDatabase().rawQuery("SELECT * FROM Lecture WHERE courseCode =" + courseCode, null);
    }

    public Cursor getLecturesByDate(String day) {
        day = fixString(day);
        return this.getWritableDatabase().rawQuery("SELECT * FROM Lecture WHERE day =" + day, null);
    }

    public ArrayList<String> getInstructorDetails(int id) {
        String query = "SELECT * FROM Instructor WHERE ID = '" + Integer.toString(id) + "';";
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            ArrayList<String> details = new ArrayList<>();
            details.add(cursor.getString(1));
            details.add(cursor.getString(2));
            details.add(cursor.getString(3));
            details.add(cursor.getString(4));
            details.add(cursor.getString(5));
            details.add(cursor.getString(6));
            return details;
        }
        return null;
    }

    public ArrayList<String> getCourseDetails(String code) {
        String query = "SELECT * FROM Course WHERE Code = " + fixString(code);
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            ArrayList<String> details = new ArrayList<>();
            details.add(cursor.getString(1));
            details.add(cursor.getString(2));
            details.add(Integer.toString(cursor.getInt(3)));
            return details;
        }
        return null;
    }

    public void clear() {
        this.getWritableDatabase().delete("Lecture", null, null);
        this.getWritableDatabase().delete("Course", null, null);
        this.getWritableDatabase().delete("Instructor", null, null);
    }
}