package com.example.sibinthomas.wheresmydoctor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    String[] xcompany={};
    Integer[] xprice={0};
    ArrayList<String> doctor=new ArrayList<String>(Arrays.asList(xcompany));
    ArrayList<Integer> rating=new ArrayList<Integer>(Arrays.asList(xprice));
    RequestQueue que;
    ListView lview;
    customadapter cs;
    Map<String, String> parameters = new HashMap<String, String>();
    CustomRequest customRequest;
    EditText location,category;
    Button search;
    String doctorurl="https://marletto.000webhostapp.com/doctorList.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        lview=(ListView)findViewById(R.id.lview);
        location=(EditText)findViewById(R.id.editText);
        category=(EditText)findViewById(R.id.editText3);
        location.setFocusable(false);
        category.setFocusable(false);
        location.setClickable(true);
        category.setClickable(true);
        que= Volley.newRequestQueue(getApplicationContext());
        cs=new customadapter();
        lview.setAdapter(cs);
        search=(Button)findViewById(R.id.button2);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressBar pbar=(ProgressBar)findViewById(R.id.progressBar4);
                pbar.setVisibility(View.VISIBLE);
                parameters.clear();
                parameters.put("category",category.getText().toString());
                parameters.put("location",location.getText().toString());
                customRequest=new CustomRequest(Request.Method.POST, doctorurl, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pbar.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = response.getJSONArray("student");
                            if (jsonArray.length() == 0) {
                                lview.setVisibility(View.INVISIBLE);
                                return;
                            }
                            else
                                lview.setVisibility(View.VISIBLE);
                            doctor.clear();
                            rating.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject doctorobject = jsonArray.getJSONObject(i);
                                    doctor.add(i,doctorobject.getString("username"));
                                    rating.add(i,doctorobject.getInt("rating"));
                                } catch (Exception es) {

                                }
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                            Log.w("w", "222");
                        }
                        lview.setAdapter(cs);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Context ctx=getApplicationContext();
                        CharSequence text =error.getLocalizedMessage();
                        Log.w("warn",error.getLocalizedMessage());
                        Toast tost=Toast.makeText(ctx,text,Toast.LENGTH_SHORT);
                        tost.show();
                    }
                });
                que.add(customRequest);
            }
        });

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = getIntent();
                String user=intent.getStringExtra("user");
                intent=new Intent(SearchActivity.this,SelectedDoctorActivity.class);
                intent.putExtra("user",user);
                TextView tview=(TextView)view.findViewById(R.id.textView11);
                intent.putExtra("doctor",tview.getText());
                startActivity(intent);
            }
        });
        location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int i=location.getInputType();
                location.setInputType(InputType.TYPE_NULL);
                location.onTouchEvent(motionEvent);
                location.setInputType(i);
                location.setFocusable(true);
                location.setCursorVisible(false);
                return true;
            }
        });
        category.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int i=category.getInputType();
                category.setInputType(InputType.TYPE_NULL);
                category.onTouchEvent(motionEvent);
                category.setInputType(i);
                category.setFocusable(true);
                category.setCursorVisible(false);
                return true;
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] xloc={"Vijay Nagar","Shukhliya","Palasia"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Select Location")
                        .setItems(xloc, new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int which) {
                                location.setText(xloc[which]);
                            }
                        });
                builder.create();
                builder.show();

            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] xloc={"Surgeon","Physichatrist"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Select Category")
                        .setItems(xloc, new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int which) {
                                category.setText(xloc[which]);
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
            return doctor.size();
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
            convertview = inflater.inflate(R.layout.doctorlist, null);
            TextView dname = (TextView) convertview.findViewById(R.id.textView11);
            RatingBar bar=(RatingBar) convertview.findViewById(R.id.ratingBar);
            dname.setText(doctor.get(position));
            bar.setRating(rating.get(position));
            return convertview;
        }

    }
}
