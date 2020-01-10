package com.example.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText classText;
    private EditText class2Text;
    private EditText name2Text;
    private EditText name3Text;
    private Button button;
    private TextView textView;

    private ArrayList<Student> students = new ArrayList<>();

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("students");

        nameText = findViewById(R.id.editText1);
        classText = findViewById(R.id.editText2);
        class2Text = findViewById(R.id.editText5);
        name2Text = findViewById(R.id.editText3);
        name3Text = findViewById(R.id.editText4);
        button = findViewById(R.id.buttonAdd);
        textView = findViewById(R.id.textView);

        readStudents();
    }



    public void readStudents(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                students.clear();
                String tekst = "Students:\n";
                for (DataSnapshot node : dataSnapshot.getChildren()) {
                    Student student = node.getValue(Student.class);
                    students.add(student);

                    tekst += "Key: " + node.getKey() + "\nName: " + student.getName() + "\nKlas: " + student.getKlas() + "\n";
                }
                textView.setText(tekst);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void addBtnClick(View view) {
        addStudent();
    }

    public void addStudent(){
        String name = nameText.getText().toString();
        String klas = classText.getText().toString();
        String uniqueID = UUID.randomUUID().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(klas)) {

            Student student = new Student(name,klas,uniqueID);

            databaseReference.child(uniqueID).setValue(student);

            nameText.setText("");
            classText.setText("");

        } else {
            Toast.makeText(this,"Naampje invullen pls", Toast.LENGTH_LONG).show();
        }

    }
    public void removeBtnClick(View view) {
        removeStudent();
    }

    public void removeStudent() {
        for (Student s : students) {
            if (s.getName().equals(name2Text.getText().toString())) {
                databaseReference.child(s.getId()).removeValue();
            }
        }
        name2Text.setText("");
    }

    public void editBtnClick(View view) {
        for (Student s : students) {
            if (s.getName().equals(name3Text.getText().toString())) {
                s.setKlas(class2Text.getText().toString());
                databaseReference.child(s.getId()).setValue(s);
            }
        }
        name3Text.setText("");
        class2Text.setText("");
    }
}
