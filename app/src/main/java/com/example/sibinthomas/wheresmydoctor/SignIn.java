package com.example.sibinthomas.wheresmydoctor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    EditText name,pass,location,allergy;
    Button button;
    static Button button3;
    RequestQueue que;
    String insertURL="https://marletto.000webhostapp.com/insertPatient.php";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        name=(EditText)findViewById(R.id.editText9);
        pass=(EditText)findViewById(R.id.editText8);
        location=(EditText)findViewById(R.id.editText7);
        allergy=(EditText)findViewById(R.id.editText5);
        button=(Button)findViewById(R.id.button12);

        que= Volley.newRequestQueue(getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        button.setText("Thanks for Joining");
                        Intent intent=new Intent(SignIn.this,MainActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("username",name.getText().toString());
                        parameters.put("password",pass.getText().toString());
                        parameters.put("location",location.getText().toString());
                        parameters.put("allergy",allergy.getText().toString());
                        Button button4=(Button)findViewById(R.id.button13);
                        parameters.put("dob",button4.getText().toString());
                        return parameters;
                    }
                };
                que.add(stringRequest);
            }
        });
    }

    public void showDatePickerDialog1(View v) {
        DialogFragment newFragment = new SignIn.DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker1");
        button3=(Button)findViewById(R.id.button13);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String xmonth=new DateFormatSymbols().getMonths()[month];
            button3.setText(day+" "+xmonth+" "+year);
        }
    }

}
