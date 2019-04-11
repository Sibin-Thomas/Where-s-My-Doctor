package com.example.sibinthomas.wheresmydoctor;

import android.content.Intent;
import android.os.Binder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Prescription extends AppCompatActivity {

    EditText editText;
    Button button;
    RequestQueue que;
    String prescriptionURL="https://marletto.000webhostapp.com/updatePrescription.php";
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_prescription);

        editText=(EditText)findViewById(R.id.editText12);
        button=(Button)findViewById(R.id.button16);
        que= Volley.newRequestQueue(getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, prescriptionURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("ok")){

                            Intent intent=new Intent(Prescription.this,DoctorActivity.class);
                            intent.putExtra("user",user);
                            startActivity(intent);

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Intent intent = getIntent();
                        Map<String, String> parameters = new HashMap<String, String>();
                        user=intent.getStringExtra("doctor");
                        parameters.put("doctor", intent.getStringExtra("doctor"));
                        parameters.put("patient", intent.getStringExtra("patient"));
                        parameters.put("time", intent.getStringExtra("time"));
                        parameters.put("date", intent.getStringExtra("date"));
                        parameters.put("prescription",editText.getText().toString());
                        return parameters;
                    }
                };
                que.add(stringRequest);
            }
        });
    }
}
