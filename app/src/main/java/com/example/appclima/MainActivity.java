package com.example.appclima;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "41535b914af0b989829191f7338274f7";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etcity);
        etCountry = findViewById(R.id.etcountry);
        tvResult = findViewById(R.id.tvResult);
    }

    public void getWheatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if(city.equals("")){
            tvResult.setText("El campo ciudad no puede estar vacio !");
        }else{
            if(!country.equals("")){
                tempUrl = url + "?q=" + city + ", " + country + "&appid="+appid;
            }else{
                tempUrl = url + "?q=" + city + "&appid="+appid;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        int CodeResult = jsonResponse.getInt("cod");

                        if(CodeResult==200){
                            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String description = jsonObjectWeather.getString("description");
                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp") - 273.15;
                            double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                            float pressure = jsonObjectMain.getInt("pressure");
                            int humidity = jsonObjectMain.getInt("humidity");
                            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                            String clouds = jsonObjectClouds.getString("all");
                            JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                            String countryName = jsonObjectSys.getString("country");
                            String cityName = jsonResponse.getString("name");
                            tvResult.setTextColor(Color.rgb(255,255,255));
                            output += "El Clima Actual de: " + cityName + " (" + countryName + ")"
                                    + "\n Temperatura: " + df.format(temp) + " ??C"
                                    + "\n Se siente como: " + df.format(feelsLike) + " ??C"
                                    + "\n Humedad: " + humidity + "%"
                                    + "\n Descripci??n: " + description
                                    + "\n Velocidad del viento: " + wind + "m/s (metros por segundo)"
                                    + "\n Nuvosidad: " + clouds + "%"
                                    + "\n Presi??n: " + pressure + " hPa";
                        }else{
                            tvResult.setTextColor(Color.rgb(255,255,255));
                            output += "Error: " + CodeResult;

                        }
                        tvResult.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}