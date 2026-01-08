package com.unityuniversity.sims;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageActivity extends AppCompatActivity {

    private Spinner spAdvisor;
    private EditText etSubject, etContent;
    private Button btnSend;
    private DBHelper db;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        studentId = getIntent().getStringExtra("student_id");
        db = new DBHelper(this);

        spAdvisor = findViewById(R.id.spAdvisor);
        etSubject = findViewById(R.id.etSubject);
        etContent = findViewById(R.id.etContent);
        btnSend = findViewById(R.id.btnSend);

        loadAdvisors();
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void loadAdvisors() {
        ArrayList<String> advisors = new ArrayList<>();
        advisors.add("Select advisor");
        Cursor c = db.getReadableDatabase().rawQuery("SELECT " + DBHelper.A_USERNAME + " FROM " + DBHelper.TABLE_ADVISORS, null);
        if (c != null && c.moveToFirst()) {
            do {
                advisors.add(c.getString(0));
            } while (c.moveToNext());
            c.close();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, advisors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAdvisor.setAdapter(adapter);
    }

    private void sendMessage() {
        String advisor = spAdvisor.getSelectedItem().toString();
        if (advisor.equals("Select advisor")) {
            Toast.makeText(this, "Please select an advisor", Toast.LENGTH_SHORT).show();
            return;
        }
        String subject = etSubject.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        if (subject.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Subject and content required", Toast.LENGTH_SHORT).show();
            return;
        }
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        boolean ok = db.addMessage(studentId, advisor, subject, content, timestamp);
        if (ok) {
            Toast.makeText(this, getString(R.string.message_sent), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }
}
