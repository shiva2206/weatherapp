package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView loc,hum,temp,wind,wea,msg;
    private ImageView img,ser;
    private EditText inp;
    private RelativeLayout rll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        loc = (TextView) findViewById(R.id.displaytxt);
        hum= (TextView) findViewById(R.id.humd);
        temp= (TextView) findViewById(R.id.temp);
        img= (ImageView) findViewById(R.id.img);
        inp= (EditText) findViewById(R.id.iptxt);
        wea= (TextView) findViewById(R.id.wea);
        msg= (TextView) findViewById(R.id.mesg);
        rll= (RelativeLayout) findViewById(R.id.rl);
        ser= (ImageView) findViewById(R.id.ser);
        wind= (TextView) findViewById(R.id.wind);

        if(!MainActivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("city","none").equals("none")) {
            msg.setText("Wait..");
            msg.setVisibility(View.VISIBLE);
            apicontrol( MainActivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("city","none")+"");

        }else{
            msg.setText("Weather App");
            msg.setVisibility(View.VISIBLE);

            rll.setVisibility(View.INVISIBLE);
        }
        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                apicontrol(inp.getText()+"");
            }
        });




    }
    public void apicontrol(String city){
        String url = "http://api.weatherapi.com/v1/current.json?key=45badcb06b6945f8a73151227232905&q=" + (city).trim()+"&aqi=no";

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONObject json = response.getJSONObject("current");
                    getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("city").commit();
                    getSharedPreferences("profile", MODE_PRIVATE).edit().putString("city", (city+"").trim()).commit();
                    setall(city,json.getJSONObject( "condition").getString( "icon"),
                            json.getInt("humidity")+"",json.getInt( "temp_c")+"",json.getInt( "wind_kph")+"",json.getJSONObject("condition").getString("text"));
                } catch (JSONException e) {
                    rll.setVisibility(View.INVISIBLE);
                    e.printStackTrace();

                    msg.setText("Enter Valid Location");
                    msg.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                rll.setVisibility(View.INVISIBLE);
                msg.setText("Error Occured.Make Sure you enter a valid location");
                msg.setVisibility(View.VISIBLE);


            }
        });

        requestQueue.add(jsonObjectRequest);







    }
    public void setall(String dis,String url,String humid,String tep,String windy,String wther){
        msg.setVisibility(View.INVISIBLE);
        loc.setText(dis.toUpperCase());
        Picasso.get().load("http:"+url).into(img);
        temp.setText(tep+" ^C");
        hum.setText(humid+" (humidity)");
        wind.setText(windy+" KpH");
        wea.setText(wther);
        rll.setVisibility(View.VISIBLE);
    }


}