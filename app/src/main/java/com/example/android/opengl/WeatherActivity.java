package com.example.android.opengl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;


public class WeatherActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    String url = "https://api.openweathermap.org/data/2.5/weather?q=Astana&appid=7c44ba7b06f6aaccb3e8e7c6f87953b2";
    String Wmain;
    String Wdescription;
    String min_temp;
    String max_temp;
    String wind_speed;
    String clouds;


    TextView tvWmain;
    TextView tvWdescription;
    TextView tvmin_temp;
    TextView tvmax_temp;
    TextView tvwind_speed;
    TextView tvclouds;
    TextView location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        try{
            getData();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        location = (TextView)findViewById(R.id.location);
        tvWmain = (TextView)findViewById(R.id.Wmain);
        tvWdescription = (TextView)findViewById(R.id.Wdescription);
        tvmin_temp = (TextView)findViewById(R.id.Wmin_temp);
        tvmax_temp = (TextView)findViewById(R.id.Wmax_temp);
        tvwind_speed = (TextView)findViewById(R.id.Wwind);
        tvclouds = (TextView)findViewById(R.id.Wclouds);
        tvWmain.setText(Wmain);
        tvWdescription.setText(Wdescription);
        tvmin_temp.setText(min_temp+"°C");
        tvmin_temp.setTextColor(Color.BLUE);
        tvmax_temp.setText(max_temp+"°C");
        tvmax_temp.setTextColor(Color.RED);
        tvwind_speed.setText("Wind: "+wind_speed+"m/s");
        tvclouds.setText("Clouds: "+clouds+" cloud units");


        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            Log.d("location","no f permission");
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    1);
        }
        onRequestPermissionsResult(1,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},new int[]{PackageManager.PERMISSION_GRANTED});

        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            Log.d("location","no c permission");
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                    2);
        }
        onRequestPermissionsResult(2,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},new int[]{PackageManager.PERMISSION_GRANTED});



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        Location loc = null;
        Location loc2 = null;
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null) {
                Log.d("location", "Last: " + String.valueOf(loc.getLatitude()) + ":" + loc.getLongitude());
            }
        }
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
            loc2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(loc2 != null) {
                Log.d("location", "LastN: " + String.valueOf(loc2.getLatitude()) + ":" + loc2.getLongitude());
            }
            Location bai = new Location("");
            bai.setLatitude(51.128287);
            bai.setLongitude(71.430495);
            if(loc != null) {
                Log.d("location","gps not null");
                location.setText(String.valueOf(loc.distanceTo(bai)) + "m");
                if (loc.distanceTo(bai) < 50) {
                    location.setTextColor(Color.RED);
                } else {
                    location.setTextColor(Color.BLUE);
                }
            }else if(loc2 != null){
                Log.d("location","network not null");
                location.setText(String.valueOf(loc2.distanceTo(bai))+"m");
                if(loc2.distanceTo(bai) < 50){
                    location.setTextColor(Color.RED);
                }else{
                    location.setTextColor(Color.BLUE);
                }
            }else{
                location.setText("Sorry, no data for now :(");
            }
        }
    }


    public void getData() throws Exception {
        String response = (String) new getRawDate().execute().get();
        JsonArray jArray = new Gson().fromJson(response, JsonObject.class).getAsJsonObject().get("weather").getAsJsonArray();
        for (int i = 0 ; i < jArray.size() ; i++ ){
            Wmain = jArray.get(i).getAsJsonObject().get("main").getAsString();
            System.out.println(Wmain);
            Wdescription = jArray.get(i).getAsJsonObject().get("description").getAsString();
            System.out.println(Wdescription);
        }
        JsonObject main = new Gson().fromJson(response, JsonObject.class).getAsJsonObject().get("main").getAsJsonObject();
        min_temp = String.valueOf(main.get("temp_min").getAsDouble()-273.15);
        System.out.println(min_temp);
        max_temp = String.valueOf(main.get("temp_max").getAsDouble()-273.15);
        System.out.println(max_temp);
        JsonObject wind = new Gson().fromJson(response, JsonObject.class).getAsJsonObject().get("wind").getAsJsonObject();
        wind_speed = String.valueOf(wind.get("speed").getAsInt());
        System.out.println(wind_speed);
        JsonObject cloud = new Gson().fromJson(response, JsonObject.class).getAsJsonObject().get("clouds").getAsJsonObject();
        clouds = String.valueOf(cloud.get("all").getAsInt());
        System.out.println(clouds);


    }

    public class getRawDate extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects){
            try {
                URL weatherUrl = new URL(url);
                Scanner scanner = new Scanner(weatherUrl.openStream());
                String response = scanner.useDelimiter("\\Z").next();
                return response;
            } catch (Exception ex){
                System.out.println("EXCEPTION in getRawDate"+ex.getMessage());
                return "EXCEPTION";
            }
        }


    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            try{
                double lat = loc.getLatitude();
                double lon = loc.getLongitude();
                Location bai = new Location("");
                bai.setLatitude(51.128287);
                bai.setLongitude(71.430495);
                location.setText(loc.distanceTo(bai)+"m");
                if(loc.distanceTo(bai) < 50){
                    location.setTextColor(Color.RED);
                }else{
                    location.setTextColor(Color.BLUE);
                }
                Log.d("location","Changed: "+String.valueOf(loc.getLatitude())+":"+loc.getLongitude());
            }
            catch (
        Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

    }
}


