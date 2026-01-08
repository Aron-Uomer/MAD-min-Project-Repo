package com.unityuniversity.sims;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ResultsActivity extends AppCompatActivity {

    private String studentId;
    private DBHelper db;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        studentId = getIntent().getStringExtra("student_id");
        db = new DBHelper(this);
        container = findViewById(R.id.resultsContainer);

        loadGrades();
    }

    private void loadGrades() {
        container.removeAllViews();

        // Student ID header card
        TextView header = new TextView(this);
        header.setText("Student ID: " + (studentId == null ? "N/A" : studentId));
        header.setTextSize(18);
        header.setPadding(32, 24, 32, 24);
        header.setBackgroundResource(R.drawable.card_background);
        header.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        LinearLayout.LayoutParams headerLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        headerLp.setMargins(0, 0, 0, 24);
        container.addView(header, headerLp);

        Cursor c = db.getGradesForStudent(studentId);
        if (c != null && c.moveToFirst()) {
            do {
                String course = c.getString(c.getColumnIndexOrThrow(DBHelper.G_COURSE));
                String grade = c.getString(c.getColumnIndexOrThrow(DBHelper.G_GRADE));

                // Create a card-like row
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setPadding(20, 20, 20, 20);
                row.setBackgroundResource(R.drawable.card_background);

                // Course (left)
                TextView tvCourse = new TextView(this);
                tvCourse.setText(course);
                tvCourse.setTextSize(16);
                tvCourse.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                tvCourse.setTextColor(ContextCompat.getColor(this, R.color.text_primary));

                // Grade (right) - color depending on value
                TextView tvGrade = new TextView(this);
                tvGrade.setText(grade);
                tvGrade.setTextSize(16);
                tvGrade.setPadding(16, 0, 0, 0);
                tvGrade.setTextColor(getColorForGrade(grade));
                tvGrade.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                row.addView(tvCourse);
                row.addView(tvGrade);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 16);
                container.addView(row, lp);

            } while (c.moveToNext());
            c.close();
        } else {
            TextView none = new TextView(this);
            none.setText("No grades found");
            none.setTextSize(16);
            none.setPadding(16, 16, 16, 16);
            container.addView(none);
        }
    }

    private int getColorForGrade(String grade) {
        if (grade == null) return ContextCompat.getColor(this, R.color.text_primary);
        String g = grade.trim().toUpperCase();
        // Simple mapping: A/B green, C orange, D/E/F red, otherwise default
        if (g.startsWith("A") || g.startsWith("B")) {
            return ContextCompat.getColor(this, R.color.accent_green);
        } else if (g.startsWith("C")) {
            return 0xFFFF9800; // orange (fallback)
        } else {
            return ContextCompat.getColor(this, R.color.accent_red);
        }
    }
}
