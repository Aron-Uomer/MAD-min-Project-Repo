package com.unityuniversity.sims;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ProvideScheduleActivity extends AppCompatActivity {

    private Spinner spDepartment, spYear, spDay, spCourse, spTime, spRoom;
    private Button btnSubmitSchedule;
    private DBHelper db;
    private String advisorUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_schedule);

        advisorUsername = getIntent().getStringExtra("advisor_username");
        db = new DBHelper(this);

        spDepartment = findViewById(R.id.spSchedDept);
        spYear = findViewById(R.id.spSchedYear);
        spDay = findViewById(R.id.spSchedDay);
        spCourse = findViewById(R.id.spSchedCourse);
        spTime = findViewById(R.id.spSchedTime);
        spRoom = findViewById(R.id.spSchedRoom);
        btnSubmitSchedule = findViewById(R.id.btnSubmitSchedule);

        ArrayAdapter<CharSequence> depAdapter = ArrayAdapter.createFromResource(this, R.array.departments_array, android.R.layout.simple_spinner_item);
        depAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartment.setAdapter(depAdapter);

        String[] years = {"1","2","3","4","5"};
        spYear.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years));

        String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        spDay.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days));

        String[] times = {"08:00-10:00","10:00-12:00","13:00-15:00","15:00-17:00"};
        spTime.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times));

        String[] courses = {"Mobile Application Development","Database Systems","Software Engineering","Computer Networks","Operating Systems","Data Structures","Web Development"};
        spCourse.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses));

        ArrayList<String> rooms = new ArrayList<>();
        fillRooms(rooms, "B");
        fillRooms(rooms, "F");
        fillRooms(rooms, "G");
        fillRooms(rooms, "K");
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rooms);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRoom.setAdapter(roomAdapter);

        btnSubmitSchedule.setOnClickListener(v -> submitSchedule());
    }

    private void fillRooms(ArrayList<String> rooms, String prefix) {
        for (int i = 1; i <= 30; i++) {
            rooms.add(prefix + "-" + i);
        }
    }

    private void submitSchedule() {
        String dept = spDepartment.getSelectedItem().toString();
        String year = spYear.getSelectedItem().toString();
        String day = spDay.getSelectedItem().toString();
        String course = spCourse.getSelectedItem().toString();
        String time = spTime.getSelectedItem().toString();
        String room = spRoom.getSelectedItem().toString();

        if (db.isRoomOccupied(day, time, room)) {
            Toast.makeText(this, "Room is already occupied at that time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.isCourseScheduled(dept, year, course, day, time)) {
            Toast.makeText(this, "Course is already scheduled for that department/year at that time", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean ok = db.addSchedule(dept, year, day, course, time, room);
        if (ok) {
            Toast.makeText(this, "Schedule added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add schedule", Toast.LENGTH_SHORT).show();
        }
    }
}
