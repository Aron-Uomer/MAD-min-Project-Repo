package com.unityuniversity.sims;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnBack, btnExploreFromLogin;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);
        btnExploreFromLogin = findViewById(R.id.btnExploreFromLogin);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
        btnExploreFromLogin.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ExploreActivity.class)));
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (username.isEmpty()) {
            etUsername.setError("Please enter username"); etUsername.requestFocus(); return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Please enter password"); etPassword.requestFocus(); return;
        }

        Cursor cAdvisor = db.getAdvisorByCredentials(username, password);
        if (cAdvisor != null && cAdvisor.moveToFirst()) {
            String advisorUsername = cAdvisor.getString(cAdvisor.getColumnIndexOrThrow(DBHelper.A_USERNAME));
            String advisorName = cAdvisor.getString(cAdvisor.getColumnIndexOrThrow(DBHelper.A_NAME));
            cAdvisor.close();
            Toast.makeText(this, "Advisor: " + advisorName, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, AdvisorActivity.class);
            intent.putExtra("advisor_username", advisorUsername);
            intent.putExtra("advisor_name", advisorName);
            startActivity(intent);
            finish();
            return;
        }
        if (cAdvisor != null && !cAdvisor.isClosed()) cAdvisor.close();

        Cursor cStudent = db.getStudentByCredentials(username, password);
        if (cStudent != null && cStudent.moveToFirst()) {
            String studentId = cStudent.getString(cStudent.getColumnIndexOrThrow(DBHelper.S_STUDENTID));
            String fullName = cStudent.getString(cStudent.getColumnIndexOrThrow(DBHelper.S_FULLNAME));
            String department = cStudent.getString(cStudent.getColumnIndexOrThrow(DBHelper.S_DEPARTMENT));
            String year = cStudent.getString(cStudent.getColumnIndexOrThrow(DBHelper.S_YEAR));
            cStudent.close();

            Toast.makeText(this, "Welcome " + fullName, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, StudentDashboardActivity.class);
            intent.putExtra("student_id", studentId);
            intent.putExtra("student_name", fullName);
            intent.putExtra("department", department);
            intent.putExtra("year", year);
            startActivity(intent);
            finish();
            return;
        }
        if (cStudent != null && !cStudent.isClosed()) cStudent.close();

        Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
    }
}
