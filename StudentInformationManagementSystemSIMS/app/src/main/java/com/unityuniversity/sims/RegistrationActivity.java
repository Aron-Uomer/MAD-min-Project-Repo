package com.unityuniversity.sims;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Defensive registration activity. Will not crash the app on inflation/findViewById errors;
 * instead it shows a toast and logs the problem to help debugging.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    private static final int MIN_AGE = 18;

    private EditText etFullName, etStudentId, etPassword, etEmail, etPhone, etDob, etSubcity;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Spinner spDepartment, spYear;
    private TextView tvCity;
    private Button btnRegister, btnExploreFromRegister;

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Defensive setContentView: catch inflate issues and show user-friendly message
        try {
            setContentView(R.layout.activity_registration);
        } catch (InflateException ie) {
            Log.e(TAG, "Layout inflation failed", ie);
            Toast.makeText(this, "UI failed to load. Check layout XML (namespaces / syntax).", Toast.LENGTH_LONG).show();
            // Return to main screen instead of crashing
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        } catch (Exception ex) {
            Log.e(TAG, "Unexpected error in setContentView", ex);
            Toast.makeText(this, "Unexpected error loading screen.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        db = new DBHelper(this);

        // Find views with null checks
        try {
            etFullName = findViewById(R.id.etFullName);
            etStudentId = findViewById(R.id.etStudentId);
            etPassword = findViewById(R.id.etPassword);
            etEmail = findViewById(R.id.etEmail);
            etPhone = findViewById(R.id.etPhone);
            etDob = findViewById(R.id.etDob);
            etSubcity = findViewById(R.id.etSubcity);

            rgGender = findViewById(R.id.rgGender);
            rbMale = findViewById(R.id.rbMale);
            rbFemale = findViewById(R.id.rbFemale);

            spDepartment = findViewById(R.id.spDepartment);
            spYear = findViewById(R.id.spYear);

            tvCity = findViewById(R.id.tvCity);

            btnRegister = findViewById(R.id.btnRegister);
            btnExploreFromRegister = findViewById(R.id.btnExploreFromRegister);
        } catch (Exception e) {
            Log.e(TAG, "findViewById failed", e);
            Toast.makeText(this, "UI components missing. Check layout IDs.", Toast.LENGTH_LONG).show();
            // Stop further initialization; user will be returned to main.
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Basic sanity: if a required view is null, avoid further work.
        if (etFullName == null || etStudentId == null || etPassword == null || etEmail == null ||
                etPhone == null || etDob == null || spDepartment == null || spYear == null ||
                rgGender == null || btnRegister == null || tvCity == null) {

            Log.e(TAG, "One or more required UI views are null; aborting registration screen.");
            Toast.makeText(this, "UI incomplete. Please contact developer.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Setup UI
        setupDepartmentSpinner();
        setupYearSpinner();
        setupDatePicker();
        setupButtons();
        tvCity.setText("Addis Ababa"); // fixed city
    }

    private void setupDepartmentSpinner() {
        try {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.departments_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spDepartment.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, "Failed to setup department spinner", e);
        }
    }

    private void setupYearSpinner() {
        try {
            String[] years = {"1", "2", "3", "4", "5"};
            ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spYear.setAdapter(yearAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Failed to setup year spinner", e);
        }
    }

    private void setupDatePicker() {
        etDob.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(this, (DatePicker view, int year, int month, int dayOfMonth) -> {
                String formatted = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                etDob.setText(formatted);
            }, y, m, d);

            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            dpd.show();
        });
    }

    private void setupButtons() {
        btnRegister.setOnClickListener(v -> {
            try {
                performRegistration();
            } catch (Exception ex) {
                Log.e(TAG, "Error during registration", ex);
                Toast.makeText(this, "Registration failed: unexpected error.", Toast.LENGTH_LONG).show();
            }
        });

        btnExploreFromRegister.setOnClickListener(v -> startActivity(new Intent(RegistrationActivity.this, ExploreActivity.class)));
    }

    private void performRegistration() {
        String fullName = safeGetText(etFullName);
        String studentId = safeGetText(etStudentId);
        String password = safeGetText(etPassword);
        String email = safeGetText(etEmail);
        String phone = safeGetText(etPhone);
        String dob = safeGetText(etDob);
        String subcity = safeGetText(etSubcity);
        String city = safeGetText(tvCity);
        String department = spDepartment.getSelectedItem() == null ? "" : spDepartment.getSelectedItem().toString();
        String year = spYear.getSelectedItem() == null ? "" : spYear.getSelectedItem().toString();

        String gender = "";
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.rbMale) gender = getString(R.string.male);
        else if (selectedGenderId == R.id.rbFemale) gender = getString(R.string.female);

        // Validation
        if (fullName.isEmpty() || studentId.isEmpty() || password.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || dob.isEmpty() || gender.isEmpty() || department.isEmpty() || year.isEmpty()) {
            Toast.makeText(this, getString(R.string.all_fields_required), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.invalid_email));
            etEmail.requestFocus();
            return;
        }

        if (phone.length() < 10) {
            etPhone.setError(getString(R.string.invalid_phone));
            etPhone.requestFocus();
            return;
        }

        if (!isOldEnough(dob)) {
            Toast.makeText(this, "You must be at least " + MIN_AGE + " years old to register.", Toast.LENGTH_LONG).show();
            return;
        }

        boolean ok = db.addStudent(fullName, studentId, email, phone, dob, city, subcity, "", department, year, gender, password);
        if (ok) {
            Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_LONG).show();
            clearForm();
            // Optionally navigate to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registration failed: Student ID may already exist.", Toast.LENGTH_LONG).show();
        }
    }

    private String safeGetText(EditText et) {
        try {
            return et == null ? "" : et.getText().toString().trim();
        } catch (Exception e) {
            Log.w(TAG, "safeGetText error", e);
            return "";
        }
    }

    private String safeGetText(TextView tv) {
        try {
            return tv == null ? "" : tv.getText().toString().trim();
        } catch (Exception e) {
            Log.w(TAG, "safeGetText(textview) error", e);
            return "";
        }
    }

    private boolean isOldEnough(String dobText) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            Date dob = sdf.parse(dobText);
            Calendar born = Calendar.getInstance();
            born.setTime(dob);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) age--;
            return age >= MIN_AGE;
        } catch (ParseException e) {
            Log.w(TAG, "DOB parse failed", e);
            return false;
        }
    }

    private void clearForm() {
        try {
            etFullName.setText("");
            etStudentId.setText("");
            etPassword.setText("");
            etEmail.setText("");
            etPhone.setText("");
            etDob.setText("");
            etSubcity.setText("");
            rgGender.clearCheck();
            spDepartment.setSelection(0);
            spYear.setSelection(0);
            etFullName.requestFocus();
        } catch (Exception e) {
            Log.w(TAG, "clearForm problem", e);
        }
    }
}
