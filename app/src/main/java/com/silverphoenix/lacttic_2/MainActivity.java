package com.silverphoenix.lacttic_2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Dialog loadingDialog;
    RoomDB database;
    private TextView noInternet,
            districtText,
            stateText, errorText;
    private Spinner genderSpinner;
    private EditText phone, name, dob, pinCode, add1, add2;
    private Button checkBtn, RegisterBtn;

    private String district = "", state = "", dobDate = "";
    private boolean valid = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Server.getmInstance(this).getRequestQueue();

        // toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Register ");

        // toolbar

        //........................ loading dialog start ......................//
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_layout);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //........................ loading dialog start ......................//

        districtText = findViewById(R.id.textView);
        stateText = findViewById(R.id.textView2);
        noInternet = findViewById(R.id.no_internet);

        phone = findViewById(R.id.number);
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        genderSpinner = findViewById(R.id.gender);
        add1 = findViewById(R.id.ad1);
        add2 = findViewById(R.id.ad2);
        pinCode = findViewById(R.id.pincode);
        errorText = findViewById(R.id.textView8);

        checkBtn = findViewById(R.id.button);
        RegisterBtn = findViewById(R.id.button2);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 10) {
                    valid = true;
                    errorText.setVisibility(View.GONE);
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Enter 10 digit Mobile Number");
                    valid = false;
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    valid = true;
                    errorText.setVisibility(View.GONE);
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Enter 10 digit Mobile Number");
                    valid = false;
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 50) {
                    valid = true;
                    errorText.setVisibility(View.GONE);
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    valid = false;
                    errorText.setText("Name Should be smaller than 50 character");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 50) {
                    valid = true;
                    errorText.setVisibility(View.GONE);
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    valid = false;
                    errorText.setText("Name Should be smaller than 50 character");
                }
            }
        });


        ArrayAdapter<String> layoutAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.gender));
        layoutAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genderSpinner.setAdapter(layoutAdapter);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dob.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month = month + 1;
                    dobDate = day + "/" + month + "/" + year;
                    dob.setText(dobDate);
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        add1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3 && s.length() < 50) {
                    valid = true;
                    errorText.setVisibility(View.GONE);
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    valid = false;
                    errorText.setText("min 3 char and max 50 char");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 3 && s.length() < 50) {
                    valid = true;
                    errorText.setVisibility(View.GONE);
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    valid = false;
                    errorText.setText("min 3 char and max 50 char");
                }
            }
        });

        add2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 50) {
                    valid = true;
                    errorText.setVisibility(View.GONE);
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    valid = false;
                    errorText.setText("mmax 50 char");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 50) {
                    valid = true;
                    errorText.setVisibility(View.GONE);
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    valid = false;
                    errorText.setText("max 50 char");
                }
            }
        });


        //Initialize database
        database = RoomDB.getInstance(this);


        pinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    checkBtn.setEnabled(true);
                } else {
                    checkBtn.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkBtn.setOnClickListener(v -> fetchData(pinCode.getText().toString()));

        RegisterBtn.setOnClickListener(v -> {
            saveData();
            //Intent intent = new Intent(this, WeatherActivity.class);
            //startActivity(intent);
        });
    }

    private void fetchData(String pinCode) {
        //clearing all data before fetching new set of data
        loadingDialog.show();
        // database.mainDao().reset(mainDataList);
        // mainDataList.clear();


        String url = "https://api.postalpincode.in/pincode/" + pinCode;
        //getting data from url
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        // String message = jsonObject.getString("Message");
                        String status = jsonObject.getString("Status");

                        JSONArray PostOffice = jsonObject.getJSONArray("PostOffice");

                        if (status.equals("Success")) {
                            StringWriter town = new StringWriter();
                            town.append(PostOffice.getString(0));

                            JSONObject townObject = (JSONObject) PostOffice.get(0);
                            district = townObject.getString("District");
                            state = townObject.getString("State");

                            districtText.setVisibility(View.VISIBLE);
                            stateText.setVisibility(View.VISIBLE);
                            districtText.setText("District: " + district);
                            stateText.setText("State: " + state);
                        }
                        //Toast.makeText(MainActivity.this, district, Toast.LENGTH_SHORT).show();

                        loadingDialog.dismiss();

                    } catch (JSONException e) {
                        loadingDialog.dismiss();
                        e.printStackTrace();
                    }
                }
                loadingDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkInternetConnection((int) (1000 - SystemClock.elapsedRealtime() % 1000));
    }

    private void checkInternetConnection(long delay) {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (networkInfo != null && networkInfo.isConnected()) {
                noInternet.setVisibility(View.GONE);
            } else {
                noInternet.setVisibility(View.VISIBLE);
            }
            checkInternetConnection(delay);
        }, delay);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveData() {
        if (valid) {
            if (!district.equals("")) {
                if (!dob.getText().toString().equals("")) {

                    if (add1.getText().length() > 3 && add1.getText().length() < 50){
                        if (add2.getText().length() <50){

                            MainData data = new MainData();
                            //Set data to the data
                            data.setNumber(phone.getText().toString());
                            data.setName(name.getText().toString());
                            data.setGender(genderSpinner.getSelectedItem().toString());
                            data.setDob(dobDate.trim());
                           // data.setAge(String.valueOf(Period.between(LocalDate.parse(dobDate), LocalDate.now()).getYears()));
                            data.setAddress_line_1(add1.getText().toString());
                            data.setAddress_line_2(add2.getText().toString());
                            data.setPincode(pinCode.getText().toString());

                            //Insert data in database
                            database.mainDao().insert(data);

                            Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, WeatherActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(this, "enter Proper Address Line 2", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "enter Proper Address Line 1", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this, "Enter Date of Birth", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Enter Proper Pin Code", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Please Enter The Detail Properly", Toast.LENGTH_SHORT).show();
        }
    }
    /*
         for (int j = 1; j < PostOffice.length();j++){
        town.append(", "+PostOffice.getString(j));
    }

    String bb = String.valueOf(town);
    //Toast.makeText(MainActivity.this, bb, Toast.LENGTH_SHORT).show();

    JSONArray lan = jsonObject.getJSONArray("languages");
    StringWriter lang = new StringWriter();

    JSONObject ll = lan.getJSONObject(0);
                        lang.append(ll.getString("name"));

                        for (int k = 1; k < lan.length();k++){
        ll = lan.getJSONObject(k);
        lang.append(", "+ll.getString("name"));
    }

    String language = String.valueOf(lang);
    //Toast.makeText(MainActivity.this, language, Toast.LENGTH_SHORT).show();

    MainData data = new MainData();
    //Set data to the data
                        data.setName(countryName);
                        data.setCapital(capital);
                        data.setFlag(flag);
                        data.setRegion(region);
                        data.setSubregion(subRegion);
                        data.setPopulation(population);
                        data.setBorder(bb);
                        data.setLanguage(language);
    //Insert data in database
                        database.mainDao().insert(data);
*/
}