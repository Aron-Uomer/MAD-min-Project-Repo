package com.unityuniversity.sims;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ProvideGradeActivity extends AppCompatActivity {

    private Spinner spDepartment, spYear, spStudentId, spCourse;
    private EditText etGrade;
    private Button btnSubmit;
    private DBHelper db;
    private String advisorUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_grade);

        advisorUsername = getIntent().getStringExtra("advisor_username");
        db = new DBHelper(this);

        spDepartment = findViewById(R.id.spDeptGrade);
        spYear = findViewById(R.id.spYearGrade);
        spStudentId = findViewById(R.id.spStudentId);
        spCourse = findViewById(R.id.spCourseGrade);
        etGrade = findViewById(R.id.etGrade);
        btnSubmit = findViewById(R.id.btnSubmitGrade);

        setupDepartment();
        setupYear();
        loadStudents();
        loadCourses();

        btnSubmit.setOnClickListener(v -> submitGrade());
    }

    private void setupDepartment() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.departments_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartment.setAdapter(adapter);
    }

    private void setupYear() {
        String[] years = {"1","2","3","4","5"};
        ArrayAdapter<String> yAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yAdapter);
    }

    private void loadStudents() {
        ArrayList<String> ids = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery("SELECT " + DBHelper.S_STUDENTID + " FROM " + DBHelper.TABLE_STUDENTS, null);
        if (c != null && c.moveToFirst()) {
            do {
                ids.add(c.getString(0));
            } while (c.moveToNext());
            c.close();
        }
        if (ids.isEmpty()) ids.add("No students");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ids);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStudentId.setAdapter(adapter);
    }

    private void loadCourses() {
        String[] courses = {"Mobile Application Development","Database Systems","Software Engineering","Computer Networks","Operating Systems","Data Structures","Web Development"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourse.setAdapter(adapter);
    }

    private void submitGrade() {
        String studentId = spStudentId.getSelectedItem().toString();
        String course = spCourse.getSelectedItem().toString();
        String grade = etGrade.getText().toString().trim();
        if (studentId.isEmpty() || course.isEmpty() || grade.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean ok = db.addGrade(studentId, course, grade, advisorUsername);
        if (ok) {
            Toast.makeText(this, "Grade added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add grade", Toast.LENGTH_SHORT).show();
        }
    }
}
