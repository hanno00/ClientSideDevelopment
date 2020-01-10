package com.example.meetapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.meetapp.DialogBox.MyDialog;
import com.example.meetapp.R;

public class LoginActivity extends AppCompatActivity {
    private TextView tv;
    private Button b;
    private MyDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv = findViewById(R.id.textView2);
        b = findViewById(R.id.button);
        myDialog = new MyDialog(this);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.show();
            }
        });

    }
}
