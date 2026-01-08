package com.unityuniversity.sims;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnClassSchedule, btnResults, btnMessageAdvisor;
    private String studentId, studentName, department, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnClassSchedule = findViewById(R.id.btnClassSchedule);
        btnResults = findViewById(R.id.btnResults);
        btnMessageAdvisor = findViewById(R.id.btnMessageAdvisor);

        studentId = getIntent().getStringExtra("student_id");
        studentName = getIntent().getStringExtra("student_name");
        department = getIntent().getStringExtra("department");
        year = getIntent().getStringExtra("year");

        tvWelcome.setText("Welcome, " + studentName);

        btnClassSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, ScheduleActivity.class);
            intent.putExtra("department", department);
            intent.putExtra("year", year);
            startActivity(intent);
        });

        btnResults.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, ResultsActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        btnMessageAdvisor.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, MessageActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });
    }
}
