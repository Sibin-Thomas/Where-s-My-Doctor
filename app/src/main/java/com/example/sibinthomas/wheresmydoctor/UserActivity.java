package com.example.sibinthomas.wheresmydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    TextView tview;
    Button search,appointment,chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user);
        tview=(TextView)findViewById(R.id.textView);
        final Intent intent=getIntent();
        tview.setText("Welcome "+intent.getStringExtra("user"));
        search=(Button)findViewById(R.id.button2);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(UserActivity.this,SearchActivity.class);
                intent1.putExtra("user",intent.getStringExtra("user"));
                startActivity(intent1);
            }
        });
        appointment=(Button)findViewById(R.id.button3);
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(UserActivity.this,AppointmentPatientHistory.class);
                intent2.putExtra("user",intent.getStringExtra("user"));
                startActivity(intent2);
            }
        });
        chat=(Button)findViewById(R.id.button4);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(UserActivity.this,PatientChat.class);
                intent2.putExtra("user",intent.getStringExtra("user"));
                startActivity(intent2);
            }
        });
    }
}
