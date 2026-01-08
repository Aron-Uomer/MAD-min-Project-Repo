package com.unityuniversity.sims;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "sims.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_STUDENTS = "students";
    public static final String TABLE_ADVISORS = "advisors";
    public static final String TABLE_SCHEDULES = "schedules";
    public static final String TABLE_GRADES = "grades";
    public static final String TABLE_MESSAGES = "messages";
    public static final String TABLE_COURSES = "courses";

    public static final String S_ID = "id";
    public static final String S_FULLNAME = "full_name";
    public static final String S_STUDENTID = "student_id";
    public static final String S_EMAIL = "email";
    public static final String S_PHONE = "phone";
    public static final String S_DOB = "dob";
    public static final String S_CITY = "city";
    public static final String S_SUBCITY = "subcity";
    public static final String S_ADDRESS = "address";
    public static final String S_DEPARTMENT = "department";
    public static final String S_YEAR = "year";
    public static final String S_GENDER = "gender";
    public static final String S_PASSWORD = "password_hash";

    public static final String A_ID = "id";
    public static final String A_NAME = "name";
    public static final String A_USERNAME = "username";
    public static final String A_PASSWORD = "password_hash";

    public static final String SCH_ID = "id";
    public static final String SCH_DEPT = "department";
    public static final String SCH_YEAR = "year";
    public static final String SCH_DAY = "day";
    public static final String SCH_COURSE = "course";
    public static final String SCH_TIME = "time";
    public static final String SCH_ROOM = "room";

    public static final String G_ID = "id";
    public static final String G_STUDENTID = "student_id";
    public static final String G_COURSE = "course";
    public static final String G_GRADE = "grade";
    public static final String G_ADVISOR = "advisor_username";

    public static final String M_ID = "id";
    public static final String M_STUDENTID = "student_id";
    public static final String M_ADVISOR = "advisor_username";
    public static final String M_SUBJECT = "subject";
    public static final String M_CONTENT = "content";
    public static final String M_TIMESTAMP = "timestamp";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStudents = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                S_FULLNAME + " TEXT," +
                S_STUDENTID + " TEXT UNIQUE," +
                S_EMAIL + " TEXT," +
                S_PHONE + " TEXT," +
                S_DOB + " TEXT," +
                S_CITY + " TEXT," +
                S_SUBCITY + " TEXT," +
                S_ADDRESS + " TEXT," +
                S_DEPARTMENT + " TEXT," +
                S_YEAR + " TEXT," +
                S_GENDER + " TEXT," +
                S_PASSWORD + " TEXT" +
                ");";

        String createAdvisors = "CREATE TABLE " + TABLE_ADVISORS + " (" +
                A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                A_NAME + " TEXT," +
                A_USERNAME + " TEXT UNIQUE," +
                A_PASSWORD + " TEXT" +
                ");";

        String createSchedules = "CREATE TABLE " + TABLE_SCHEDULES + " (" +
                SCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SCH_DEPT + " TEXT," +
                SCH_YEAR + " TEXT," +
                SCH_DAY + " TEXT," +
                SCH_COURSE + " TEXT," +
                SCH_TIME + " TEXT," +
                SCH_ROOM + " TEXT" +
                ");";

        String createGrades = "CREATE TABLE " + TABLE_GRADES + " (" +
                G_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                G_STUDENTID + " TEXT," +
                G_COURSE + " TEXT," +
                G_GRADE + " TEXT," +
                G_ADVISOR + " TEXT" +
                ");";

        String createMessages = "CREATE TABLE " + TABLE_MESSAGES + " (" +
                M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                M_STUDENTID + " TEXT," +
                M_ADVISOR + " TEXT," +
                M_SUBJECT + " TEXT," +
                M_CONTENT + " TEXT," +
                M_TIMESTAMP + " TEXT" +
                ");";

        String createCourses = "CREATE TABLE " + TABLE_COURSES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "department TEXT," +
                "year TEXT" +
                ");";

        db.execSQL(createStudents);
        db.execSQL(createAdvisors);
        db.execSQL(createSchedules);
        db.execSQL(createGrades);
        db.execSQL(createMessages);
        db.execSQL(createCourses);

        seedAdvisors(db);
    }

    private void seedAdvisors(SQLiteDatabase db) {
        insertAdvisorRaw(db, "Yohannes", "yohannes", hash("admin123"));
        insertAdvisorRaw(db, "Aron", "aron", hash("admin123"));
        insertAdvisorRaw(db, "Nati", "nati", hash("admin123"));
    }

    private void insertAdvisorRaw(SQLiteDatabase db, String name, String username, String passwordHash) {
        ContentValues cv = new ContentValues();
        cv.put(A_NAME, name);
        cv.put(A_USERNAME, username);
        cv.put(A_PASSWORD, passwordHash);
        db.insert(TABLE_ADVISORS, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVISORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }

    public boolean addStudent(String fullName, String studentId, String email, String phone,
                              String dob, String city, String subcity, String address,
                              String department, String year, String gender, String passwordPlain) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(S_FULLNAME, fullName);
        cv.put(S_STUDENTID, studentId);
        cv.put(S_EMAIL, email);
        cv.put(S_PHONE, phone);
        cv.put(S_DOB, dob);
        cv.put(S_CITY, city);
        cv.put(S_SUBCITY, subcity);
        cv.put(S_ADDRESS, address);
        cv.put(S_DEPARTMENT, department);
        cv.put(S_YEAR, year);
        cv.put(S_GENDER, gender);
        cv.put(S_PASSWORD, hash(passwordPlain));
        long id = db.insert(TABLE_STUDENTS, null, cv);
        return id != -1;
    }

    public Cursor getStudentByCredentials(String studentId, String passwordPlain) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashed = hash(passwordPlain);
        String sql = "SELECT * FROM " + TABLE_STUDENTS + " WHERE " + S_STUDENTID + "=? AND " + S_PASSWORD + "=?";
        return db.rawQuery(sql, new String[]{studentId, hashed});
    }

    public Cursor getStudentById(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_STUDENTS + " WHERE " + S_STUDENTID + "=?";
        return db.rawQuery(sql, new String[]{studentId});
    }

    public Cursor getAdvisorByCredentials(String username, String passwordPlain) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashed = hash(passwordPlain);
        String sql = "SELECT * FROM " + TABLE_ADVISORS + " WHERE " + A_USERNAME + "=? AND " + A_PASSWORD + "=?";
        return db.rawQuery(sql, new String[]{username, hashed});
    }

    public boolean addAdvisor(String name, String username, String passwordPlain) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(A_NAME, name);
        cv.put(A_USERNAME, username);
        cv.put(A_PASSWORD, hash(passwordPlain));
        long id = db.insert(TABLE_ADVISORS, null, cv);
        return id != -1;
    }

    public boolean addSchedule(String department, String year, String day, String course, String time, String room) {
        if (isRoomOccupied(day, time, room)) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SCH_DEPT, department);
        cv.put(SCH_YEAR, year);
        cv.put(SCH_DAY, day);
        cv.put(SCH_COURSE, course);
        cv.put(SCH_TIME, time);
        cv.put(SCH_ROOM, room);
        long id = db.insert(TABLE_SCHEDULES, null, cv);
        return id != -1;
    }

    public boolean isRoomOccupied(String day, String time, String room) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_SCHEDULES + " WHERE " + SCH_DAY + "=? AND " + SCH_TIME + "=? AND " + SCH_ROOM + "=?";
        Cursor c = db.rawQuery(sql, new String[]{day, time, room});
        boolean occupied = false;
        if (c.moveToFirst()) {
            occupied = c.getInt(0) > 0;
        }
        c.close();
        return occupied;
    }

    public boolean isCourseScheduled(String department, String year, String course, String day, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_SCHEDULES + " WHERE " + SCH_DEPT + "=? AND " + SCH_YEAR + "=? AND " + SCH_COURSE + "=? AND " + SCH_DAY + "=? AND " + SCH_TIME + "=?";
        Cursor c = db.rawQuery(sql, new String[]{department, year, course, day, time});
        boolean scheduled = false;
        if (c.moveToFirst()) scheduled = c.getInt(0) > 0;
        c.close();
        return scheduled;
    }

    public Cursor getSchedulesForDeptYearDay(String department, String year, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_SCHEDULES + " WHERE " + SCH_DEPT + "=? AND " + SCH_YEAR + "=? AND " + SCH_DAY + "=?";
        return db.rawQuery(sql, new String[]{department, year, day});
    }

    public boolean addGrade(String studentId, String course, String grade, String advisorUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(G_STUDENTID, studentId);
        cv.put(G_COURSE, course);
        cv.put(G_GRADE, grade);
        cv.put(G_ADVISOR, advisorUsername);
        long id = db.insert(TABLE_GRADES, null, cv);
        return id != -1;
    }

    public Cursor getGradesForStudent(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_GRADES + " WHERE " + G_STUDENTID + "=?";
        return db.rawQuery(sql, new String[]{studentId});
    }

    public boolean addMessage(String studentId, String advisorUsername, String subject, String content, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(M_STUDENTID, studentId);
        cv.put(M_ADVISOR, advisorUsername);
        cv.put(M_SUBJECT, subject);
        cv.put(M_CONTENT, content);
        cv.put(M_TIMESTAMP, timestamp);
        long id = db.insert(TABLE_MESSAGES, null, cv);
        return id != -1;
    }

    public Cursor getMessagesForAdvisor(String advisorUsername) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_MESSAGES + " WHERE " + M_ADVISOR + "=?";
        return db.rawQuery(sql, new String[]{advisorUsername});
    }

    public Cursor getMessagesForStudent(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_MESSAGES + " WHERE " + M_STUDENTID + "=?";
        return db.rawQuery(sql, new String[]{studentId});
    }

    public static String hash(String input) {
        if (TextUtils.isEmpty(input)) return "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return input;
        }
    }
}
