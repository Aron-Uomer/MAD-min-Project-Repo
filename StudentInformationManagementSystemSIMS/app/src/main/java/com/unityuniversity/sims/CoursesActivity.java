package com.unityuniversity.sims;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CoursesActivity extends AppCompatActivity {

    private Spinner spinnerAvailableCourses;
    private Spinner spinnerAddedCourses;
    private Button btnAddCourse;
    private Button btnDropCourse;

    private ArrayList<String> availableCoursesList;
    private ArrayList<String> addedCoursesList;
    private ArrayAdapter<String> availableAdapter;
    private ArrayAdapter<String> addedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        // Initialize views
        initializeViews();
        setupCourseLists();
        setupAdapters();
        setupButtonListeners();
    }

    private void initializeViews() {
        spinnerAvailableCourses = findViewById(R.id.spinnerAvailableCourses);
        spinnerAddedCourses = findViewById(R.id.spinnerAddedCourses);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        btnDropCourse = findViewById(R.id.btnDropCourse);
    }

    private void setupCourseLists() {
        // Get courses from string array
        String[] coursesArray = getResources().getStringArray(R.array.courses_array);

        // Initialize lists
        availableCoursesList = new ArrayList<>();
        addedCoursesList = new ArrayList<>();

        // Add all courses to available list initially
        for (String course : coursesArray) {
            availableCoursesList.add(course);
        }
    }

    private void setupAdapters() {
        // Setup adapter for available courses
        availableAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                availableCoursesList
        );
        availableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAvailableCourses.setAdapter(availableAdapter);

        // Setup adapter for added courses
        addedAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                addedCoursesList
        );
        addedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddedCourses.setAdapter(addedAdapter);
    }

    private void setupButtonListeners() {
        btnAddCourse.setOnClickListener(v -> addCourse());
        btnDropCourse.setOnClickListener(v -> dropCourse());
    }

    private void addCourse() {
        int selectedPosition = spinnerAvailableCourses.getSelectedItemPosition();

        if (selectedPosition >= 0 && selectedPosition < availableCoursesList.size()) {
            String selectedCourse = availableCoursesList.get(selectedPosition);

            // Add to added courses list
            addedCoursesList.add(selectedCourse);
            addedAdapter.notifyDataSetChanged();

            // Remove from available courses list
            availableCoursesList.remove(selectedPosition);
            availableAdapter.notifyDataSetChanged();

            Toast.makeText(this, R.string.course_added, Toast.LENGTH_SHORT).show();

            // Reset spinner selection
            if (availableCoursesList.size() > 0) {
                spinnerAvailableCourses.setSelection(0);
            }
        }
    }

    private void dropCourse() {
        int selectedPosition = spinnerAddedCourses.getSelectedItemPosition();

        if (selectedPosition >= 0 && selectedPosition < addedCoursesList.size()) {
            String selectedCourse = addedCoursesList.get(selectedPosition);

            // Add back to available courses list
            availableCoursesList.add(selectedCourse);
            availableAdapter.notifyDataSetChanged();

            // Remove from added courses list
            addedCoursesList.remove(selectedPosition);
            addedAdapter.notifyDataSetChanged();

            Toast.makeText(this, R.string.course_dropped, Toast.LENGTH_SHORT).show();

            // Reset spinner selection
            if (addedCoursesList.size() > 0) {
                spinnerAddedCourses.setSelection(0);
            }
        }
    }
}