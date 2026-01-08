package com.unityuniversity.sims;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdvisorMessagesActivity extends AppCompatActivity {

    private LinearLayout container;
    private DBHelper db;
    private String advisorUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_messages);

        db = new DBHelper(this);
        container = findViewById(R.id.advisorMessagesContainer);
        advisorUsername = getIntent().getStringExtra("advisor_username");

        loadMessages();
    }

    private void loadMessages() {
        Cursor c = db.getMessagesForAdvisor(advisorUsername);
        if (c != null && c.moveToFirst()) {
            do {
                String studentId = c.getString(c.getColumnIndexOrThrow(DBHelper.M_STUDENTID));
                String subject = c.getString(c.getColumnIndexOrThrow(DBHelper.M_SUBJECT));
                String content = c.getString(c.getColumnIndexOrThrow(DBHelper.M_CONTENT));
                String timestamp = c.getString(c.getColumnIndexOrThrow(DBHelper.M_TIMESTAMP));
                TextView tv = new TextView(this);
                tv.setText("From: " + studentId + "\n" + subject + "\n" + content + "\n" + timestamp);
                tv.setPadding(8,8,8,8);
                container.addView(tv);
            } while (c.moveToNext());
            c.close();
        } else {
            TextView none = new TextView(this);
            none.setText("No messages");
            container.addView(none);
        }
    }
}
