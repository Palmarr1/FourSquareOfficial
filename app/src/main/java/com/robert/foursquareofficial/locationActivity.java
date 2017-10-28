package com.robert.foursquareofficial;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IntegerRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Robert on 10/23/2017.
 */

public class locationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<AllLocation> listLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public String currentUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        Bundle b = getIntent().getExtras();
        currentUser = b.getString("user");

        Log.i("USER",currentUser);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listLocation = new ArrayList<>();

        DownloadJSON dJson = new DownloadJSON();

        String resultsTemp = "";
        try{
            resultsTemp = dJson.execute().get();
        }catch(Exception e){e.printStackTrace();}

        JSONObject jObject;
        try{
            jObject = new JSONObject(resultsTemp);

            for(int x = 0; x < jObject.length();x++){
                JSONObject jsonPart = new JSONObject(jObject.getString(jObject.names().get(x).toString()));

                AllLocation allI = new AllLocation();

                allI.dateTime = jsonPart.getString("datetime");
                allI.longitude = jsonPart.getString("longitude");
                allI.latitude = jsonPart.getString("latitude");
                allI.id = jObject.names().get(x).toString();

                if(jsonPart.has("location")){
                    JSONObject finalLocation = jsonPart.getJSONObject("location");

                    String ID = finalLocation.getString("id");
                    String name = finalLocation.getString("name");
                    IndividualLocation newLocation = new IndividualLocation(ID,name);

                    allI.addLocation(newLocation);

                    allI.setLocation(ID);



                }else{
                    JSONArray possibleLocations = jsonPart.getJSONArray("possibleLocations");

                    for(int j = 0; j < possibleLocations.length(); j++){
                        JSONObject indLocation = possibleLocations.getJSONObject(j);


                        String ID = indLocation.getString("id");
                        String name = indLocation.getString("name");
                        IndividualLocation newLocation = new IndividualLocation(ID,name);

                        allI.addLocation(newLocation);

                        if(possibleLocations.length() == 1){
                            allI.setLocation(indLocation.getString("id"));
                        }
                    }
                }



                JSONArray commentsJSON = jsonPart.getJSONArray("comments");
                for(int j = 0; j < commentsJSON.length(); j++){
                    JSONObject indComment = commentsJSON.getJSONObject(j);

                    comment c = new comment(indComment.getString("date"),indComment.getString("user"),indComment.getString("comment"));
                    allI.addComment(c);
                }

                listLocation.add(allI);
                Log.i("I","Location Added");

            }
        }catch(Exception e){e.printStackTrace();}
        adapter = new MyAdapter(listLocation,this,getApplicationContext());
        recyclerView.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                DecimalFormat df = new DecimalFormat("#.##");
                DownloadTask task = new DownloadTask();
                String apiOutput = "";


                try{
                    apiOutput = task.execute(df.format(location.getLongitude()),df.format(location.getLatitude())).get();
                    Log.i("OUTPUT",apiOutput);

                    JSONObject jObject;
                    try{
                        jObject = new JSONObject(apiOutput);
                        String code = jObject.getJSONObject("meta").getString("code");
                        AllLocation allI = new AllLocation();

                        if(code.equals("200")){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            String currentDateandTime = sdf.format(new Date());

                            allI = new AllLocation(currentDateandTime);

                            JSONArray results = jObject.getJSONObject("response").getJSONArray("venues");
                            if(results.length() != 0) {

                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject jsonPart = results.getJSONObject(i);

                                    String ID = jsonPart.getString("id");
                                    String name = jsonPart.getString("name");
                                    IndividualLocation x = new IndividualLocation(ID,name);

                                    allI.addLocation(x);
                                }

                                listLocation.add(allI);
                                adapter.notifyDataSetChanged();
                            }
                        }else{
                            Log.i("I", "NO INFORMATION");
                        }
                    }catch(Exception e){ Log.i("ERROR", e.toString());}
                }catch(Exception e){e.printStackTrace();}

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET},10);
                return;
            }
            configureButton();
        }else {
            configureButton();
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AllLocation item = adapter.getItem(position);

        if(item.getLocationDetermined() == false){
            Bundle b = new Bundle();
            b.putStringArrayList("Locations",item.returnArrayList());

            Intent i = new Intent(this,Pop.class);

            i.putExtras(b);

            startActivityForResult(i,position);

        }else{
            Log.i("I", Integer.toString(position));
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Req",Integer.toString(requestCode));
        Log.i("Res",Integer.toString(resultCode));
        if(resultCode == RESULT_OK) {
            Bundle b = data.getExtras();
            String strEditText = b.getString("ItemSelected");

            if(strEditText.equals("-1")) {
                Log.i("F", strEditText);
            }
            else if(strEditText.equals("-2")){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("locations");

                myRef.child(listLocation.get(requestCode).id).removeValue();
                listLocation.remove(requestCode);
                adapter.notifyDataSetChanged();

            }else{
                adapter.setLocation(requestCode,Integer.parseInt(strEditText));
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission,int[] grantResults){
        switch (requestCode){
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureButton();
                }
                return;
        }
    }
    public void configureButton(){
        Log.i("Cong","HERE");
        locationManager.requestLocationUpdates("gps", 60000, 30, locationListener);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String clientID = "KMU12DAHPZ3DTNYVA5RY10A2O0X5P1W331FMGVJ4LFYTAXX2";
            String clientSecret = "1XTMV3OOY5ZDV3ADVNRHVNXNXZG4FUWO0UB3HFRU0HY4H54R";

            String finalLink = "https://api.foursquare.com/v2/venues/search?v=20131016&ll=" + strings[1] + "%2C" + strings[0] + "&intent=checkin&client_id=" + clientID + "&client_secret=" + clientSecret;
            Log.i("Link",finalLink);
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;


            try{
                url = new URL(finalLink);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in  = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data != -1){
                    result += (char)data;
                    data = reader.read();
                }
            }catch (Exception e){
                return e.toString();
            }

            return result;

        }
    }

    public class DownloadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String finalLink = "https://foursquarenjit.firebaseio.com/locations.json?orderBy=\"user\"&equalTo=\"" + currentUser + "\"";

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;


            try{
                url = new URL(finalLink);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in  = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data != -1){
                    result += (char)data;
                    data = reader.read();
                }
            }catch (Exception e){
                return e.toString();
            }
            Log.i("Results",result);
            return result;

        }
    }
    public void addRecord(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.robert.foursquareofficial", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
    }
}
