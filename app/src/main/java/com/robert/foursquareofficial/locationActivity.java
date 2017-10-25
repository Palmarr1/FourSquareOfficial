package com.robert.foursquareofficial;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listLocation = new ArrayList<>();


        //Parse Current Records

        String currentRecords = "{\n" +
                "    \"locations\": [\n" +
                "      {\n" +
                "        \"datetime\":\"01/01/2017\",\n" +
                "        \"possibleLocations\":[\n" +
                "            {\n" +
                "              \"id\":\"1\",\n" +
                "              \"name\":\"NJIT\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\":\"2\",\n" +
                "              \"name\":\"Rutgers\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\":\"3\",\n" +
                "              \"name\":\"Prudential Center\"\n" +
                "            }\n" +
                "          ],\n" +
                "        \"comments\":{\n" +
                "          \n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"datetime\":\"02/01/2017\",\n" +
                "        \"possibleLocations\":[\n" +
                "            {\n" +
                "              \"id\":\"4\",\n" +
                "              \"name\":\"Wayne\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\":\"5\",\n" +
                "              \"name\":\"Packanack\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"id\":\"6\",\n" +
                "              \"name\":\"Valley\"\n" +
                "            }\n" +
                "          ],\n" +
                "        \"comments\":{\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"datetime\":\"03/01/2017\",\n" +
                "        \"possibleLocations\":[\n" +
                "            {\n" +
                "              \"id\":\"7\",\n" +
                "              \"name\":\"Whole Foods\"\n" +
                "            }\n" +
                "          ],\n" +
                "        \"comments\":{\n" +
                "          \n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "}";

        JSONObject jObject;
        try{
            jObject = new JSONObject(currentRecords);

            JSONArray results = jObject.getJSONArray("locations");

            for(int i = 0; i < results.length(); i++){
                JSONObject jsonPart = results.getJSONObject(i);

                AllLocation allI = new AllLocation();
                allI.setDateTime(jsonPart.getString("datetime"));

                JSONArray possibleLocations = jsonPart.getJSONArray("possibleLocations");

                for(int j = 0; j < possibleLocations.length(); j++){
                    JSONObject indLocation = possibleLocations.getJSONObject(j);

                    IndividualLocation newLocation = new IndividualLocation(indLocation.getString("id"),indLocation.getString("name"));
                    allI.addLocation(newLocation);

                    if(possibleLocations.length() == 1){
                        allI.setLocation(indLocation.getString("id"));
                    }
                }
                listLocation.add(allI);
                Log.i("Location", "Added");
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
                                    IndividualLocation x = new IndividualLocation(ID, name);

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

            if(strEditText.equals("-1")){
                Log.i("F",strEditText);
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
}
