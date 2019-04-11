package com.example.sibinthomas.wheresmydoctor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SelectedDoctorActivity extends AppCompatActivity {

    TextView name,address,qualification;
    RatingBar ratingBar;
    Button button;
    String[] xparameters;
    static String pickertime;
    static Button button1,button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_selected_doctor);
        Intent intent = getIntent();
        final String user=intent.getStringExtra("user");
        final String doctor=intent.getStringExtra("doctor");
        final RequestQueue que = Volley.newRequestQueue(getApplicationContext());
        name=(TextView)findViewById(R.id.textView2);
        address=(TextView)findViewById(R.id.textView5);
        qualification=(TextView)findViewById(R.id.textView6);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar2);
        button=(Button)findViewById(R.id.button2);
        String detailsURL = "https://marletto.000webhostapp.com/doctorDetails.php";
        final String appointmentURL = "https://marletto.000webhostapp.com/insertAppointment.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, detailsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] parameters=response.split("-");
                xparameters=parameters;
                name.setText(doctor);
                address.setText(parameters[0]);
                qualification.setText(parameters[2]);
                ratingBar.setRating(Integer.parseInt(parameters[3]));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("doctor",doctor);
                return parameters;
            }
        };
        que.add(stringRequest);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (button.getText().equals("Appointment Placed")) {
                    Intent intent2 = new Intent(SelectedDoctorActivity.this, UserActivity.class);
                    intent2.putExtra("user", user);
                    startActivity(intent2);
                } else {

                    final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, appointmentURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("ok"))
                                button.setText("Appointment Placed");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Button button3=(Button)findViewById(R.id.button5);
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("patient", user);
                            parameters.put("doctor", doctor);
                            parameters.put("time", button3.getText().toString());
                            button3=(Button)findViewById(R.id.button6);
                            parameters.put("date", button3.getText().toString());
                            parameters.put("patientResponse","REQUEST");
                            parameters.put("doctorResponse","PENDING");
                            return parameters;
                        }
                    };
                    que.add(stringRequest);
                }
            }
        });
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"timepicker");
        button1=(Button)findViewById(R.id.button5);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        button2=(Button)findViewById(R.id.button6);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance() ;
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timezone;
            if(hourOfDay>12)
                timezone="PM";
            else
                timezone="AM";
            pickertime=hourOfDay%12+":"+minute+" "+timezone ;
            button1.setText(pickertime);
        }

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
            button2.setText(day+" "+xmonth+" "+year);
        }
    }

}
