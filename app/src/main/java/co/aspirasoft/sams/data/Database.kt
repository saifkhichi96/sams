package co.aspirasoft.sams.data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createInstructorTable = "" +
                "CREATE TABLE Instructor (" +
                "   ID int PRIMARY KEY," +
                "   forename varchar(30) NOT NULL," +
                "   surname varchar(30) NOT NULL," +
                "   title enum('Mr.', 'Mrs.', 'Ms.', 'Sir', 'Ma'am') NOT NULL," +
                "   email varchar(35) UNIQUE," +
                "   phone varchar(11)," +
                "   office varchar(75)" +
                ");"
        val createCourseTable = "" +
                "CREATE TABLE Course (" +
                "   Code varchar(10) PRIMARY KEY," +
                "   name varchar(100) NOT NULL UNIQUE," +
                "   credits varchar(5) NOT NULL," +
                "   instructorID int NOT NULL," +
                "   CONSTRAINT FOREIGN KEY (instructorID) REFERENCES Instructor(ID) ON UPDATE CASCADE ON DELETE SET NULL" +
                ");"
        val createLectureTable = "" +
                "CREATE TABLE Lecture (" +
                "   courseCode varchar(10) NOT NULL," +
                "   day enum('Mon', 'Tue', 'Wed', 'Thur', 'Fri', 'Sat', 'Sun') NOT NULL," +
                "   startTime time NOT NULL UNIQUE," +
                "   duration time NOT NULL," +
                "   location varchar(75) NOT NULL," +
                "   type enum('Lab', 'Lecture', 'Workshop', 'Seminar', 'Other') NOT NULL DEFAULT 'Lecture'," +
                "   CONSTRAINT PRIMARY KEY (courseCode, startTime)," +
                "   FOREIGN KEY (courseCode) REFERENCES Course(Code) ON UPDATE CASCADE ON DELETE CASCADE" +
                ");"
        db.execSQL(createInstructorTable)
        db.execSQL(createCourseTable)
        db.execSQL(createLectureTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    private fun fixString(string: String): String {
        var string = string
        string = string.replace("'", " ")
        string = string.replace("\"", " ")
        string = "'$string'"
        return string
    }

    fun insertInstructor(
        id: Int,
        fname: String,
        sname: String,
        title: String,
        email: String,
        phone: String,
        office: String
    ) {
        var fname = fname
        var sname = sname
        var title = title
        var email = email
        var phone = phone
        var office = office
        fname = fixString(fname)
        sname = fixString(sname)
        title = fixString(title)
        email = fixString(email)
        phone = fixString(phone)
        office = fixString(office)
        val query = "" +
                "INSERT INTO Instructor VALUES ('" +
                id + "'" +
                fname + sname + title +
                email + phone + office +
                ");"
        this.writableDatabase.execSQL(query)
    }

    fun insertCourse(code: String, name: String, creditHours: String, instructorID: Int) {
        var code = code
        var name = name
        var creditHours = creditHours
        code = fixString(code)
        name = fixString(name)
        creditHours = fixString(creditHours)
        val query = "" +
                "INSERT INTO Course VALUES (" +
                code + name + creditHours +
                "'" + instructorID +
                "');"
        this.writableDatabase.execSQL(query)
    }

    fun insertLecture(
        courseCode: String,
        day: String,
        startTime: String,
        duration: String,
        location: String,
        type: String
    ) {
        var courseCode = courseCode
        var day = day
        var startTime = startTime
        var duration = duration
        var location = location
        var type = type
        courseCode = fixString(courseCode)
        day = fixString(day)
        startTime = fixString(startTime)
        duration = fixString(duration)
        location = fixString(location)
        type = fixString(type)
        val query = "" +
                "INSERT INTO Lecture VALUES (" +
                courseCode + day + startTime +
                duration + location + type + ");"
        this.writableDatabase.execSQL(query)
    }

    val courses: Cursor
        get() = this.writableDatabase.rawQuery("SELECT * FROM Course", null)

    fun getLecturesByCourse(courseCode: String): Cursor {
        var courseCode = courseCode
        courseCode = fixString(courseCode)
        return this.writableDatabase.rawQuery("SELECT * FROM Lecture WHERE courseCode =$courseCode", null)
    }

    fun getLecturesByDate(day: String): Cursor {
        var day = day
        day = fixString(day)
        return this.writableDatabase.rawQuery("SELECT * FROM Lecture WHERE day =$day", null)
    }

    fun getInstructorDetails(id: Int): ArrayList<String>? {
        val query = "SELECT * FROM Instructor WHERE ID = '$id';"
        val cursor = this.writableDatabase.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val details = ArrayList<String>()
            details.add(cursor.getString(1))
            details.add(cursor.getString(2))
            details.add(cursor.getString(3))
            details.add(cursor.getString(4))
            details.add(cursor.getString(5))
            details.add(cursor.getString(6))
            return details
        }
        return null
    }

    fun getCourseDetails(code: String): ArrayList<String>? {
        val query = "SELECT * FROM Course WHERE Code = " + fixString(code)
        val cursor = this.writableDatabase.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val details = ArrayList<String>()
            details.add(cursor.getString(1))
            details.add(cursor.getString(2))
            details.add(Integer.toString(cursor.getInt(3)))
            return details
        }
        return null
    }

    fun clear() {
        this.writableDatabase.delete("Lecture", null, null)
        this.writableDatabase.delete("Course", null, null)
        this.writableDatabase.delete("Instructor", null, null)
    }

    companion object {
        const val DB_NAME = "SAMS"
    }
}