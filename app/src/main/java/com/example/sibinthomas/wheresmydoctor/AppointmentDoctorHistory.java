package com.example.sibinthomas.wheresmydoctor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AppointmentDoctorHistory extends AppCompatActivity {
    String[] xcompany={};
    String[] xcom={"Approve","Cancel","Shown","Give Prescription"};
    ArrayList<String> patient=new ArrayList<String>(Arrays.asList(xcompany));
    ArrayList<String> presponse=new ArrayList<String>(Arrays.asList(xcompany));
    ArrayList<String> dresponse=new ArrayList<String>(Arrays.asList(xcompany));
    ArrayList<String> date=new ArrayList<String>(Arrays.asList(xcompany));
    ArrayList<String> time=new ArrayList<String>(Arrays.asList(xcompany));
    ArrayList<String> xprescription=new ArrayList<String>(Arrays.asList(xcompany));

    String xdetails;

    customadapter cs;
    CustomRequest customRequest;
    RequestQueue que;
    String user;
    ListView lview;
    Map<String, String> parameters = new HashMap<String, String>();
    String pappointmentURL="https://marletto.000webhostapp.com/doctorAppointment.php";
    String cancelappointmentURL="https://marletto.000webhostapp.com/cancelDoctorAppointment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_appointment_doctor_history);
        lview = (ListView) findViewById(R.id.appointmenthistorydoctor);
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        parameters.put("doctor", user);
        que = Volley.newRequestQueue(getApplicationContext());
        customRequest = new CustomRequest(Request.Method.POST, pappointmentURL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("student");
                    if (jsonArray.length() == 0) {
                        lview.setVisibility(View.INVISIBLE);
                        return;
                    } else
                        lview.setVisibility(View.VISIBLE);
                    patient.clear();
                    presponse.clear();
                    dresponse.clear();
                    date.clear();
                    time.clear();
                    xprescription.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject doctorobject = jsonArray.getJSONObject(i);
                            patient.add(i, doctorobject.getString("patient"));
                            presponse.add(i, doctorobject.getString("patientResponse"));
                            dresponse.add(i, doctorobject.getString("doctorResponse"));
                            date.add(i, doctorobject.getString("date"));
                            time.add(i, doctorobject.getString("time"));
                            xprescription.add(i, doctorobject.getString("prescription"));
                        } catch (Exception es) {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.w("w", "222");
                }
                lview.setAdapter(cs);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Context ctx = getApplicationContext();
                CharSequence text = error.getLocalizedMessage();
                Log.w("warn", error.getLocalizedMessage());
                Toast tost = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
                tost.show();
            }
        });
        que.add(customRequest);

        cs = new customadapter();
        lview.setAdapter(cs);
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Your Decision")
                        .setItems(xcom, new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int which) {

                                TextView tview = (TextView) view.findViewById(R.id.textView4);
                                final String patient1 = tview.getText().toString().split(" ")[1];
                                tview = (TextView) view.findViewById(R.id.textView15);
                                final String time = tview.getText().toString().split(" ")[1]+" "+tview.getText().toString().split(" ")[2];
                                tview = (TextView) view.findViewById(R.id.textView14);
                                final String date = tview.getText().toString().split(" ")[1]+" "+tview.getText().toString().split(" ")[2]+" "+tview.getText().toString().split(" ")[3];

                                if(which==3){

                                    Intent xintent= new Intent(AppointmentDoctorHistory.this,Prescription.class);
                                    xintent.putExtra("doctor",user);
                                    xintent.putExtra("patient",patient1);
                                    xintent.putExtra("time",time);
                                    xintent.putExtra("date",date);

                                    startActivity(xintent);


                                }
                                else{

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, cancelappointmentURL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("ok")){
                                                dialog.dismiss();
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Button button3 = (Button) findViewById(R.id.button5);
                                            Map<String, String> parameters = new HashMap<String, String>();
                                            parameters.put("doctor", user);
                                            parameters.put("patient", patient1);
                                            parameters.put("time", time);
                                            parameters.put("date", date);
                                            parameters.put("type", which+"");
                                            return parameters;
                                        }
                                    };
                                    que.add(stringRequest);
                                }
                            }
                        });
                builder.create();
                builder.show();
            }
        });
    }



    class customadapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return date.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertview, ViewGroup arg2) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = getLayoutInflater();
            convertview = inflater.inflate(R.layout.appointmentpatientlayout, null);
            TextView doctorname = (TextView) convertview.findViewById(R.id.textView4);
            TextView xdate = (TextView) convertview.findViewById(R.id.textView14);
            TextView xtime = (TextView) convertview.findViewById(R.id.textView15);
            TextView xpresponse = (TextView) convertview.findViewById(R.id.textView16);
            TextView xdresponse = (TextView) convertview.findViewById(R.id.textView17);
            TextView prescription = (TextView) convertview.findViewById(R.id.textView18);

            doctorname.setText("Patient. " + patient.get(position));
            xdate.setText("Date: " + date.get(position));
            xtime.setText("Time: " + time.get(position));
            prescription.setText(xprescription.get(position));
            String string = presponse.get(position);

            if (string.equals("REQUEST"))
                xpresponse.setTextColor(Color.parseColor("#ff33b5e5"));
            else if (string.equals("CANCELLED"))
                xpresponse.setTextColor(Color.RED);

            xpresponse.setText(presponse.get(position));
            string = dresponse.get(position);

            if (string.equals("PENDING"))
                xdresponse.setTextColor(Color.WHITE);
            else if (string.equals("CANCELLED"))
                xdresponse.setTextColor(Color.RED);
            else if (string.equals("APPROVED"))
                xdresponse.setTextColor(Color.GREEN);
            else if (string.equals("SHOWN"))
                xdresponse.setTextColor(Color.MAGENTA);

            xdresponse.setText(dresponse.get(position));

            return convertview;
        }

    }

}
