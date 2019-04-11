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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DoctorChat extends AppCompatActivity {


    Button send,xselect;
    EditText editText;
    String patientURL="https://marletto.000webhostapp.com/chatPatientList.php";
    String chatURL="https://marletto.000webhostapp.com/chatCaller.php";
    String insertChatURL="https://marletto.000webhostapp.com/insertChat.php";
    String[] doctorlist={};
    RequestQueue que;
    customadapter cs;
    ListView lview;
    ArrayList<String> selectedDoctors=new ArrayList<String>(Arrays.asList(doctorlist));
    ArrayList<String> sender=new ArrayList<String>(Arrays.asList(doctorlist));
    ArrayList<String> chathistory=new ArrayList<String>(Arrays.asList(doctorlist));
    ArrayList<String> datetime=new ArrayList<String>(Arrays.asList(doctorlist));
    CustomRequest customRequest,customRequest1;
    Map<String, String> parameters = new HashMap<String, String>();
    Map<String, String> parameters1 = new HashMap<String, String>();
    String xuser;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_chat);
        que= Volley.newRequestQueue(getApplicationContext());
        lview=(ListView)findViewById(R.id.lview);
        cs=new customadapter();
        lview.setAdapter(cs);
        xselect=(Button)findViewById(R.id.button11);
        send=(Button)findViewById(R.id.button10);
        editText=(EditText)findViewById(R.id.editText4);
        intent=getIntent();
        parameters.put("doctor", intent.getStringExtra("user"));
        xselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                customRequest = new CustomRequest(Request.Method.POST, patientURL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("student");
                            selectedDoctors.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject doctorobject = jsonArray.getJSONObject(i);
                                    selectedDoctors.add(i, doctorobject.getString("patient"));
                                } catch (Exception es) {
                                    Toast toast=Toast.makeText(getApplicationContext(),"Exception",Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }

                            final String[] xdoctor=selectedDoctors.toArray(new String[selectedDoctors.size()]);
                            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle("Select Patient")
                                    .setItems(xdoctor, new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            xselect.setText(xdoctor[which]);
                                            xuser=xdoctor[which];
                                            chatCaller(xuser);
                                        }
                                    });
                            builder.create();
                            builder.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.w("w", "222");
                        }

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
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msg = editText.getText().toString();
                if (msg.equals(""))
                    chatCaller(xuser);
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, insertChatURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            editText.setText("");
                            chatCaller(xuser);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            String currentDateandTime = sdf.format(new Date());
                            parameters.put("sender", intent.getStringExtra("user"));
                            parameters.put("receiver", xuser);
                            parameters.put("message", msg);
                            parameters.put("datetime", currentDateandTime);
                            return parameters;
                        }
                    };
                    que.add(stringRequest);
                }
            }
        });

    }

    public void chatCaller(String patient){
        parameters1.clear();
        parameters1.put("patient", patient);
        parameters1.put("doctor",intent.getStringExtra("user"));
        customRequest1 = new CustomRequest(Request.Method.POST, chatURL, parameters1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("student");
                    if (jsonArray.length() == 0) {
                        lview.setVisibility(View.INVISIBLE);
                        return;
                    } else
                        lview.setVisibility(View.VISIBLE);
                    sender.clear();
                    chathistory.clear();
                    datetime.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject doctorobject = jsonArray.getJSONObject(i);
                            chathistory.add(i, doctorobject.getString("message"));
                            datetime.add(i,doctorobject.getString("datetime"));
                            sender.add(i,doctorobject.getString("sender"));
                        } catch (Exception es) {

                        }
                    }
                    lview.setAdapter(cs);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.w("w", "222");
                }

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
        que.add(customRequest1);
    }

    class customadapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return chathistory.size();
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
            convertview = inflater.inflate(R.layout.chat_patient_layout, null);
            TextView message=(TextView) convertview.findViewById(R.id.textView25);
            TextView datetime1=(TextView) convertview.findViewById(R.id.textView26);
            if(sender.get(position).equals(intent.getStringExtra("user")))
                message.setText("You: "+chathistory.get(position));
            else
                message.setText(xuser+": "+chathistory.get(position));
            datetime1.setText(datetime.get(position));
            if(sender.get(position).equals(intent.getStringExtra("user"))){
                message.setTextColor(Color.parseColor("#48C9B0"));
            }
            else{
                message.setTextColor(Color.parseColor("#F7DC6F"));
            }
            return convertview;
        }

    }

}
