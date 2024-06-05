package com.example.myweatherapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView CName;
    Button Search;
    TextView SHOW;

    EditText etlat,etlon,ethum,etpresure,ettemp;
    String APIID="8b6bfa710ba6b7d95b02657cb1174696";
    String URL;

    class getWeather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls){
            StringBuilder result = new StringBuilder();
            try{
                java.net.URL url= new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line="";
                while((line = reader.readLine()) != null){
                    result.append(line).append("\n");
                }
                return result.toString();
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
        public void fetchWeatherData(String result) throws JSONException {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject mainObject = jsonObject.getJSONObject("main");
            JSONObject coord = jsonObject.getJSONObject("coord");


            double temperature = mainObject.getDouble("temp");
            double lat = coord.getDouble("lat");
            double lon = coord.getDouble("lon");
            int pressure = mainObject.getInt("pressure");
            int humidity = mainObject.getInt("humidity");


//            StringBuilder weatherInfo = new StringBuilder();
//            weatherInfo.append("Latitude: ").append(lat + "\n").append("Longitude: ").append(lon + "\n");
//            weatherInfo.append("Temperature: ").append(temperature).append("K\n");
//            weatherInfo.append("Pressure: ").append(pressure).append(" hPa\n");
//            weatherInfo.append("Humidity: ").append(humidity).append("%");
//            SHOW.setText(weatherInfo);
            etlat.setText("Latitude : "+String.valueOf(lat));
            etlon.setText("Longitude : "+String.valueOf(lon));
            ethum.setText("Humidity : "+String.valueOf(humidity));

            ettemp.setText("Temperature : "+String.valueOf(temperature-273.5)+"°C");
            etpresure.setText("Pressure : "+String.valueOf(pressure)+"Pa");

        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try{
                fetchWeatherData(result);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CName = findViewById(R.id.et1);
        Search = findViewById(R.id.btn1);
        SHOW = findViewById(R.id.et2);
        etlat = findViewById(R.id.etlati);
        etlon = findViewById(R.id.etlong);
        etpresure = findViewById(R.id.etPressure);
        ettemp = findViewById(R.id.ettem);
        ethum = findViewById(R.id.etHumidity);
        final  String[] temp={""};
         Search.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(MainActivity.this, "Clicked Button ", Toast.LENGTH_SHORT).show();
                 String city = CName.getText().toString();
                 try{
                     if(city!=null){
                         URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APIID;
                     }else{
                         Toast.makeText(MainActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
                     }
                     getWeather task= new getWeather();
                     temp[0] = task.execute(URL).get();
                 }catch(ExecutionException e){
                     e.printStackTrace();
                 }catch(InterruptedException e){
                     e.printStackTrace();
                 }
                 if(temp[0] == null){
                     SHOW.setText("Cannot able to find Weather");
                 }

             }
         });



    }
}