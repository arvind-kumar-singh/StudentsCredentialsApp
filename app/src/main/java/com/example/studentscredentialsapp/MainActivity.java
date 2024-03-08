package com.example.studentscredentialsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName, editTextRollNo;
    RecyclerView recyclerViewStudents;
    StudentAdapter studentAdapter;
    StudentDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextRollNo = findViewById(R.id.editTextRollNo);

        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new StudentDatabaseHelper(this);

        retrieveStudents();
    }

    public void saveStudent(View view) {
        String name = editTextName.getText().toString().trim();
        String rollNo = editTextRollNo.getText().toString().trim();

        if (name.isEmpty() || rollNo.isEmpty()) {
            Toast.makeText(this, "Please enter both name and roll number", Toast.LENGTH_SHORT).show();
            return;
        }

        Student student = new Student(name, rollNo);
        long newRowId = databaseHelper.saveStudent(student);

        if (newRowId != -1) {
            Toast.makeText(this, "Student details saved!", Toast.LENGTH_SHORT).show();
            clearEditTexts();
            retrieveStudents();
        } else {
            Toast.makeText(this, "Error saving student details", Toast.LENGTH_SHORT).show();
        }
    }

    public void retrieveStudents() {
        List<Student> students = databaseHelper.retrieveStudents();
        studentAdapter = new StudentAdapter(students);
        recyclerViewStudents.setAdapter(studentAdapter);
    }

    public void deleteStudent(View view) {
        String rollNo = editTextRollNo.getText().toString().trim();

        if (rollNo.isEmpty()) {
            Toast.makeText(this, "Please enter a roll number to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        int deletedRows = databaseHelper.deleteStudent(rollNo);

        if (deletedRows > 0) {
            Toast.makeText(this, "Student with Roll No " + rollNo + " deleted", Toast.LENGTH_SHORT).show();
            clearEditTexts();
            retrieveStudents();
        } else {
            Toast.makeText(this, "No student found with Roll No " + rollNo, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearEditTexts() {
        editTextName.getText().clear();
        editTextRollNo.getText().clear();
    }
}
