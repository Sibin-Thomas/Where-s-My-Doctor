package com.example.sibinthomas.wheresmydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class DoctorActivity extends AppCompatActivity {

    TextView tview;
    Button button,button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor);
        tview=(TextView)findViewById(R.id.textView20);
        final Intent intent=getIntent();
        tview.setText("Welcome "+intent.getStringExtra("user"));
        button=(Button)findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(DoctorActivity.this,AppointmentDoctorHistory.class);
                intent2.putExtra("user",intent.getStringExtra("user"));
                startActivity(intent2);
            }
        });
        button1=(Button)findViewById(R.id.button8);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(DoctorActivity.this,DoctorChat.class);
                intent2.putExtra("user",intent.getStringExtra("user"));
                startActivity(intent2);
            }
        });

    }
}
