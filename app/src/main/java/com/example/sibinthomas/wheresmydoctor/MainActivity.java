package com.example.sibinthomas.wheresmydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText username,password;
    TextView forgot;
    Button login,signup;
    String loginurl="https://marletto.000webhostapp.com/verifyUser.php";
    RequestQueue que;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        username=(EditText)findViewById(R.id.editText1);
        password=(EditText)findViewById(R.id.editText2);
        login=(Button)findViewById(R.id.button);
        signup=(Button)findViewById(R.id.button1);
        forgot=(TextView)findViewById(R.id.textView28);
        que= Volley.newRequestQueue(getApplicationContext());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressBar pbar=(ProgressBar)findViewById(R.id.progressBar);
                pbar.setVisibility(View.VISIBLE);
                StringRequest strre = new StringRequest(Request.Method.POST, loginurl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pbar.setVisibility(View.GONE);
                        String[] details=response.split("-");
                        if (response.equals("No")) {
                            Toast toast=Toast.makeText(getApplicationContext(),"Incorrect Details",Toast.LENGTH_SHORT);
                            toast.show();

                        } else if(details[1].equals("patient")) {

                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            String message = username.getText().toString();
                            intent.putExtra("user",details[0]);
                            startActivity(intent);
                            MainActivity.this.finish();

                        }
                        else if(details[1].equals("doctor")){

                            Intent intent = new Intent(MainActivity.this, DoctorActivity.class);
                            String message = username.getText().toString();
                            intent.putExtra("user",details[0]);
                            startActivity(intent);
                            MainActivity.this.finish();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CharSequence text = error.getLocalizedMessage();
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                        Log.e(error.getLocalizedMessage(),"TAB");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("username", username.getText().toString());
                        parameters.put("password", password.getText().toString());
                        return parameters;
                    }
                };
                que.add(strre);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
}
