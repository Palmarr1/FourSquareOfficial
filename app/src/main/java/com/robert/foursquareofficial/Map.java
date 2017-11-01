package com.robert.foursquareofficial;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mainMap;
    public ArrayList<String> aList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        Bundle b = getIntent().getExtras();

        aList = b.getStringArrayList("list");

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
                Toast.makeText(getApplicationContext(), "NJIT", Toast.LENGTH_SHORT).show();
                if (marker.equals(stock)) {
                    Toast.makeText(getApplicationContext(), "40.7423, 74.1793", Toast.LENGTH_SHORT).show();
                }
                return false;
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
}
