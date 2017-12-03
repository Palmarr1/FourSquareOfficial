package com.robert.foursquareofficial;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.GeoDataClient;
//import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mainMap;
    public ArrayList<String> aList;
    public Button button;
    public String currentUser;
    public ArrayList<MarkerOptions> userMarkerArray = new ArrayList<MarkerOptions>();
    public ArrayList<MarkerOptions> friendsMarkerArray = new ArrayList<MarkerOptions>();
    public CheckBox chkTerrible;
    public CheckBox chkGreat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        button = (Button)findViewById(R.id.friendsList);
        chkTerrible = (CheckBox)findViewById(R.id.chkTerrible);
        chkGreat = (CheckBox)findViewById(R.id.chkGreat);

        chkGreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friendsMarkerArray.size() != 0){
                    mainMap.clear();
                    for(MarkerOptions m : friendsMarkerArray){
                        if(chkTerrible.isChecked()){
                            if(m.getTitle().toLowerCase().contains("terrible")){
                                mainMap.addMarker(m);
                            }
                        }else if(chkGreat.isChecked()){
                            if(m.getTitle().toLowerCase().contains("great")){
                                mainMap.addMarker(m);
                            }
                        }else{
                            mainMap.addMarker(m);
                        }

                    }
                }

            }
        });

        chkTerrible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friendsMarkerArray.size() != 0){
                    mainMap.clear();
                    for(MarkerOptions m : friendsMarkerArray){
                        if(chkTerrible.isChecked()){
                            if(m.getTitle().toLowerCase().contains("terrible")){
                                mainMap.addMarker(m);
                            }
                        }else if(chkGreat.isChecked()){
                            if(m.getTitle().toLowerCase().contains("great")){
                                mainMap.addMarker(m);
                            }
                        }else{
                            mainMap.addMarker(m);
                        }

                    }
                }

            }
        });
        Bundle b = getIntent().getExtras();

        aList = b.getStringArrayList("list");
        currentUser = b.getString("user");

    }
    @Override
    public void onMapReady(GoogleMap map) {

        mainMap = map;
        updateLocationUI();
        getDeviceLocation();

        GoogleMapOptions mapOptions = new GoogleMapOptions();
        mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL);
        mapOptions.zoomControlsEnabled(true);

        mainMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.info_box, null);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        for(String s : aList){
            String[] temp = s.split(":");

            LatLng stock = new LatLng(Double.parseDouble(temp[1]),Double.parseDouble(temp[0]));
            mainMap.addMarker(new MarkerOptions().position(stock).title(temp[2]));
            userMarkerArray.add(new MarkerOptions().position(stock).title(temp[2]));
        }

        final LatLng stock = new LatLng(40.7423, -74.1793);
        mainMap.moveCamera(CameraUpdateFactory.newLatLng(stock));



        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mainMap.addMarker(new MarkerOptions().position(latLng));
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i("LINK", "HERE");
                DownloadYelp yelp = new DownloadYelp();

                String resultsTemp = "";
                try {
                    String title = "";
                    if (marker.getTitle().contains(")")) {
                        title = marker.getTitle().substring(marker.getTitle().indexOf(")"), marker.getTitle().length());
                    } else {
                        title = marker.getTitle();
                    }
                    resultsTemp = yelp.execute(title, Double.toString(marker.getPosition().latitude), Double.toString(marker.getPosition().longitude)).get();
                    Log.d("LINK", resultsTemp);

                    ;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(resultsTemp));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error Finding Yelp", Toast.LENGTH_SHORT);
                }
            }
        });
    }
    private boolean locationPerms(){
        //request permission for location enabling
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mainMap.setMyLocationEnabled(true);
            return true;
        }
        return false;
    }

    private void updateLocationUI() {
        if(mainMap == null)
            return;
        try{
            if(locationPerms()){
                mainMap.getUiSettings().setMyLocationButtonEnabled(true);
            }else{
                mainMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        }catch (SecurityException e){
            //ignore for now
        }
    }

    private void getDeviceLocation(){
        try{
            if(locationPerms()){
                //Task location = fusedLocationProviderClient
            }
        } catch(Exception e){

        }
    }
    public void getFriends(View v){

        if(button.getText().equals("Show Friends")){
            if(friendsMarkerArray.size() == 0){
                DownloadJSON dJson = new DownloadJSON();
                String resultsTemp = "";
                try{
                    resultsTemp = dJson.execute().get();
                }catch(Exception e){e.printStackTrace();}
            }else{
                for(MarkerOptions m : friendsMarkerArray){
                    mainMap.addMarker(m);

                }
            }
            button.setText("Hide Friends");
        }else{
            mainMap.clear();
            for(MarkerOptions m : userMarkerArray){
                mainMap.addMarker(m);
            }
            button.setText("Show Friends");
        }


    }
    public class DownloadJSON extends AsyncTask<String, Void, String> {

                @Override
                protected String doInBackground(String... strings) {

            String finalLink = "https://foursquarenjit.firebaseio.com/locations.json";

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(finalLink);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    result += (char) data;
                    data = reader.read();
                }
            } catch (Exception e) {
                return e.toString();
            }
            //Log.i("Results",result);
            Log.i("TEST!!!","HERE");
            return result;


        }

        @Override
        protected void onPostExecute(String result){
            Log.i("TEST!!!","HERE");
            Log.d("readPlaces", "onPostExecute Entered");
            List<HashMap<String, String>> friendPlacesList = null;
            DataParser dataParser = new DataParser();
            friendPlacesList =  dataParser.parse(result);
            ShowFriendPlaces(friendPlacesList);
        }

        private void ShowFriendPlaces(List<HashMap<String, String>> friendPlacesList){
            Log.i("TEST!!!","HERE");
            for(int i = 0; i < friendPlacesList.size(); i++){
                Log.d("onPostExecute","Entered into showing locations");
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = friendPlacesList.get(i);
                if(googlePlace.containsKey("lat")){
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));

                    String name = googlePlace.get("place_name");
                    String comment = googlePlace.get("comment");
                    String user = googlePlace.get("user");
                    String rating = googlePlace.get("rating");

                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title("User: " + user + "\nComment: (\"" + comment +"\")\nRating: " + rating + "Location \n" + name);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                    mainMap.addMarker(markerOptions);
                    friendsMarkerArray.add(markerOptions);
                }
            }
        }
    }

    class DataParser {
        public List<HashMap<String, String>> parse(String jsonData) {
            JSONArray jsonArray = null;
            JSONObject jsonObject = null;

            try {
                Log.d("Places", "parse");
                jsonObject = new JSONObject((String) jsonData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getPlaces(jsonObject);
        }

        private List<HashMap<String, String>> getPlaces(JSONObject jObject) {
            List<HashMap<String, String>> placesList = new ArrayList<>();
            HashMap<String, String> placeMap = null;

            Log.d("Places", "getPlaces");

            for(int x = 0; x < jObject.length();x++){
                try {
                    JSONObject jsonPart = new JSONObject(jObject.getString(jObject.names().get(x).toString()));

                    if(jsonPart.has("location")){
                        placeMap = getPlace(jsonPart);
                        placesList.add(placeMap);
                        Log.d("Places", "Adding");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
            HashMap<String, String> placeMap = new HashMap<String, String>();

            //string is displayed as -NA- in default when there is no name available
            String placeName = "-NA-";
            String latitude = "";
            String longitude = "";
            String comment = "";
            String user = "";
            String rating = "";

            Log.d("getPlace", "Entered");

            try {
                if(!googlePlaceJson.getString("user").equals(currentUser)){
                    placeName = googlePlaceJson.getJSONObject("location").getString("name");
                    if(googlePlaceJson.has("comment")){
                        comment = googlePlaceJson.getString("comment");
                    }
                    if(googlePlaceJson.has("rating")){
                        rating = googlePlaceJson.getString("rating");
                    }

                    latitude = googlePlaceJson.getString("latitude");
                    longitude = googlePlaceJson.getString("longitude");
                    user = googlePlaceJson.getString("user");

                    placeMap.put("place_name", placeName);
                    placeMap.put("lat", latitude);
                    placeMap.put("lng", longitude);
                    placeMap.put("comment",comment);
                    placeMap.put("user",user);
                    placeMap.put("rating",rating);
                    Log.d("getPlace", "Putting Places");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return placeMap;
        }
    }
    public class DownloadYelp extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String begLink = "https://www.yelp.com/search?find_desc=";
            String temp = "";

            String[] split = strings[0].split(" ");

            for(String s : split){
                temp += s + "+";
            }
            String finalLink = begLink + temp.substring(0, temp.length() - 1) + "&find_loc=" + strings[1] +"%2C" + strings[2];
            Log.i("LINK",finalLink);
            String result = "";
            Document doc;

            try{
                doc = Jsoup.connect(finalLink).get();

                Elements links = doc.getElementsByClass("indexed-biz-name");

                for(Element e : links){
                    if(e.text().toString().contains("1.")){
                        result = e.getElementsByAttributeValue("class","biz-name js-analytics-click").attr("abs:href");
                        break;
                    }
                }

            }catch(Exception e){
                e.printStackTrace(System.out);
            }
            return result;

        }
    }

}
