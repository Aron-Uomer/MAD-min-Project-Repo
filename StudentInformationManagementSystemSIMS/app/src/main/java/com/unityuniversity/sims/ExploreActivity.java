package com.unityuniversity.sims;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExploreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        TextView tv = findViewById(R.id.exploreTitle);
        tv.setText("System features:\n\n" +
                "• Student registration and login (studentId + password)\n" +
                "• Class schedule viewing by department/year/day\n" +
                "• Advisors can provide grades and additional schedules\n" +
                "• Students can view results/grades\n" +
                "• Students can send messages to advisors\n" +
                "• Room conflict checks when advisor adds schedule\n");
    }
}
