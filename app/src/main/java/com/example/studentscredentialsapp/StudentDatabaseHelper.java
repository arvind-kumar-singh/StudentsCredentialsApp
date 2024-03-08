package com.example.studentscredentialsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class StudentDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "students.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ROLL_NO = "roll_no";

    private static final String CREATE_TABLE_STUDENTS =
            "CREATE TABLE " + TABLE_STUDENTS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_ROLL_NO + " TEXT);";

    public StudentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    public long saveStudent(Student student) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_ROLL_NO, student.getRollNo());
        long newRowId = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return newRowId;
    }

    public List<Student> retrieveStudents() {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_NAME, COLUMN_ROLL_NO};
        Cursor cursor = db.query(TABLE_STUDENTS, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String rollNo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLL_NO));
            students.add(new Student(name, rollNo));
        }

        cursor.close();
        db.close();
        return students;
    }

    public int deleteStudent(String rollNo) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = COLUMN_ROLL_NO + " = ?";
        String[] selectionArgs = {rollNo};
        int deletedRows = db.delete(TABLE_STUDENTS, selection, selectionArgs);
        db.close();
        return deletedRows;
    }
}
