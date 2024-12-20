package com.example.studentlists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener
{
    EditText StudentID,Name,studentcourse,studentAddress,studentphone,studentGender;
    Button Insert,Delete,ViewAll;
    SQLiteDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StudentID= findViewById(R.id.studentID);
        Name= findViewById(R.id.Name);
        studentcourse= findViewById(R.id.studentcourse);
        studentAddress= findViewById(R.id.studentAddress);
        studentphone= findViewById(R.id.studentPhone);
        studentGender= findViewById(R.id.studentGender);
        Insert= findViewById(R.id.Insert);
        Delete= findViewById(R.id.Delete);
        ViewAll= findViewById(R.id.ViewAll);
        Insert.setOnClickListener(this);
        Delete.setOnClickListener(this);
        ViewAll.setOnClickListener(this);
        // Database Creation
        db=openOrCreateDatabase("StudentLists", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS studentinfo(StudentID VARCHAR,name VARCHAR,course VARCHAR,address VARCHAR,phone VARCHAR,gender VARCHAR);");
    }
    public void onClick(View view)
    {
        if(view==Insert)
        {
            if(StudentID.getText().toString().trim().isEmpty() ||
                    Name.getText().toString().trim().isEmpty() ||
                    studentcourse.getText().toString().trim().isEmpty() ||
                    studentAddress.getText().toString().trim().isEmpty() ||
                    studentphone.getText().toString().trim().isEmpty() ||
                    studentGender.getText().toString().trim().isEmpty())
            {
                showMessage("ERROR", "One or More Fields Are Empty!!!");
                return;
            }
            db.execSQL("INSERT INTO studentinfo (StudentID, name, course, address, phone, gender) VALUES('" + StudentID.getText() + "','" + Name.getText() + "','" + studentcourse.getText() + "','" + studentAddress.getText() + "','" + studentphone.getText() + "','" + studentGender.getText() + "');");
            showMessage("COMPLETE", "Student Added");
            clearText();
        }
        if (view == Delete) {
            if (StudentID.getText().toString().trim().isEmpty()) {
                showMessage("ERROR", "No Student ID was entered");
                return;
            }
            String studentId = StudentID.getText().toString();
            Cursor c = db.rawQuery("SELECT * FROM studentinfo WHERE StudentID = ?", new String[]{studentId});
            if (c.moveToFirst()) {
                db.execSQL("DELETE FROM studentinfo WHERE StudentID = ?", new String[]{studentId});
                showMessage("COMPLETE", "Student Record Deleted");
            } else {
                showMessage("ERROR", "Student Does not Exist Yo");
            }
            c.close();
            clearText();
        }
        if (view == ViewAll) {
            @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT * FROM studentinfo", null);
            if (c.getCount() == 0) {
                showMessage("NOTHING'S HERE!!!", "Database Empty Yo");
                return;
            }
            StringBuilder buffer = new StringBuilder();
            while (c.moveToNext()) {
                buffer.append("StudentID No: ").append(c.getString(c.getColumnIndexOrThrow("StudentID"))).append("\n");
                buffer.append("Name: ").append(c.getString(c.getColumnIndexOrThrow("name"))).append("\n");
                buffer.append("Course: ").append(c.getString(c.getColumnIndexOrThrow("course"))).append("\n");
                buffer.append("Address: ").append(c.getString(c.getColumnIndexOrThrow("address"))).append("\n");
                buffer.append("Phone #: ").append(c.getString(c.getColumnIndexOrThrow("phone"))).append("\n");
                buffer.append("Gender: ").append(c.getString(c.getColumnIndexOrThrow("gender"))).append("\n");
                buffer.append("\n");
            }
            showMessage("Student Details", buffer.toString());
        }
    }
    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        StudentID.setText("");
        Name.setText("");
        studentcourse.setText("");
        studentAddress.setText("");
        studentphone.setText("");
        studentGender.setText("");
        StudentID.requestFocus();
    }
}