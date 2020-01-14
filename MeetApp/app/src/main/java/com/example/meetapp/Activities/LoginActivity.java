package com.example.meetapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.meetapp.DialogBox.MyDialog;
import com.example.meetapp.Models.Person;
import com.example.meetapp.R;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameText;
    private Button b;
    private MyDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(android.R.style.Theme_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameText = findViewById(R.id.editTextName);

        b = findViewById(R.id.buttonLogin);
        myDialog = new MyDialog(this);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("DATA", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                Person person = new Person(usernameText.getText().toString(),false);

                Gson gson = new Gson();

                editor.putString("PERSON", gson.toJson(person));
                editor.commit();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                intent.putExtra("person", person);
                startActivity(intent);
//                myDialog.show();
            }
        });

    }

}
