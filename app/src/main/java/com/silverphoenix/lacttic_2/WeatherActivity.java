package com.silverphoenix.lacttic_2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class WeatherActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Dialog loadingDialog;
    private EditText cityEditText;
    private TextView latitude, longitude, celsius, fahrenheit;
    private Button checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        requestQueue = Server.getmInstance(this).getRequestQueue();

        Toolbar toolbar = findViewById(R.id.w_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //........................ loading dialog start ......................//
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_layout);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //........................ loading dialog start ......................//

        cityEditText = findViewById(R.id.city_text);
        latitude = findViewById(R.id.textView3);
        longitude = findViewById(R.id.textView4);
        celsius = findViewById(R.id.textView5);
        fahrenheit = findViewById(R.id.textView6);
        checkBtn = findViewById(R.id.button3);


        checkBtn.setOnClickListener(v -> {
            fetchData(cityEditText.getText().toString().trim());
        });


    }

    private void fetchData(String CityName) {
        loadingDialog.show();
        String url = "https://api.weatherapi.com/v1/current.json?key=35c9f92ac5bf4df0811144140212307&q=" + CityName + "&aqi=no";
        //getting data from url
        @SuppressLint("SetTextI18n") JsonObjectRequest jsonArrayRequest =
                new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                    try {

                        // String message = jsonObject.getString("Message");
                        JSONObject location = response.getJSONObject("location");
                        JSONObject current = response.getJSONObject("current");

                        // JSONObject error = response.getJSONObject("error");

                        String lat = location.getString("lat");
                        String lon = location.getString("lon");

                        String temp_c = current.getString("temp_c");
                        String temp_f = current.getString("temp_f");


                        latitude.setText("latitude :" + lat);
                        longitude.setText("longitude :" + lon);
                        celsius.setText("Temperature in celsius: " + temp_c);
                        fahrenheit.setText("Temperature in fahrenheit: " + temp_f);
                        Toast.makeText(WeatherActivity.this, lat + " / " + temp_c, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        // loadingDialog.dismiss();
                        e.printStackTrace();
                    }

                    loadingDialog.dismiss();

                }, error -> {
                    loadingDialog.dismiss();
                    Toast.makeText(WeatherActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wether_nemu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
           //do nothing
            return true;
        }else if (id == R.id.one_item) {
//            Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
//            startActivity(intent);
            return true;
        }
        return true;
    }

}