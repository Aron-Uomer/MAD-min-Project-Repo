package com.unityuniversity.sims;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdvisorActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnProvideGrade, btnProvideSchedule, btnViewMessages;
    private String advisorUsername, advisorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor);

        advisorUsername = getIntent().getStringExtra("advisor_username");
        advisorName = getIntent().getStringExtra("advisor_name");

        tvWelcome = findViewById(R.id.tvAdvisorWelcome);
        btnProvideGrade = findViewById(R.id.btnProvideGrade);
        btnProvideSchedule = findViewById(R.id.btnProvideSchedule);
        btnViewMessages = findViewById(R.id.btnViewMessages);

        tvWelcome.setText("Welcome, " + advisorName);

        btnProvideGrade.setOnClickListener(v -> {
            Intent intent = new Intent(AdvisorActivity.this, ProvideGradeActivity.class);
            intent.putExtra("advisor_username", advisorUsername);
            startActivity(intent);
        });

        btnProvideSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(AdvisorActivity.this, ProvideScheduleActivity.class);
            intent.putExtra("advisor_username", advisorUsername);
            startActivity(intent);
        });

        btnViewMessages.setOnClickListener(v -> {
            Intent intent = new Intent(AdvisorActivity.this, AdvisorMessagesActivity.class);
            intent.putExtra("advisor_username", advisorUsername);
            startActivity(intent);
        });
    }
}
