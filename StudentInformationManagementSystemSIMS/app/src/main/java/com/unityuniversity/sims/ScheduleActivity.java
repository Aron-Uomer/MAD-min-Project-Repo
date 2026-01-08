package com.unityuniversity.sims;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScheduleActivity extends AppCompatActivity {

    private Spinner spDay;
    private LinearLayout scheduleContainer;
    private DBHelper db;
    private String department, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        db = new DBHelper(this);
        spDay = findViewById(R.id.spDay);
        scheduleContainer = findViewById(R.id.scheduleContainer);

        department = getIntent().getStringExtra("department");
        year = getIntent().getStringExtra("year");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.days_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDay.setAdapter(adapter);

        spDay.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String day = parent.getItemAtPosition(position).toString();
                loadSchedulesForDay(day);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private void loadSchedulesForDay(String day) {
        scheduleContainer.removeAllViews();
        Cursor c = db.getSchedulesForDeptYearDay(department, year, day);
        if (c != null && c.moveToFirst()) {
            do {
                String course = c.getString(c.getColumnIndexOrThrow(DBHelper.SCH_COURSE));
                String time = c.getString(c.getColumnIndexOrThrow(DBHelper.SCH_TIME));
                String room = c.getString(c.getColumnIndexOrThrow(DBHelper.SCH_ROOM));
                TextView tv = new TextView(this);
                tv.setText(course + " — " + time + " — Room: " + room);
                tv.setTextSize(16);
                int pad = (int) getResources().getDimension(R.dimen.padding_large);
                tv.setPadding(pad, pad, pad, pad);
                tv.setBackgroundResource(R.drawable.card_background);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_medium));
                scheduleContainer.addView(tv, lp);
            } while (c.moveToNext());
            c.close();
        } else {
            TextView tv = new TextView(this);
            tv.setText("No schedule found for your department/year on this day.");
            tv.setTextSize(16);
            scheduleContainer.addView(tv);
        }
    }
}
