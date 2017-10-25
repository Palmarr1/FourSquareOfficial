package com.robert.foursquareofficial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Inet4Address;
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
    public void addRecord(View v){
        String newRecord ="";
        JSONObject jObject;
        try{
            jObject = new JSONObject(newRecord);
            String code = jObject.getJSONObject("meta").getString("code");
            AllLocation allI = new AllLocation();

            if(code.equals("200")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date());

                allI = new AllLocation(currentDateandTime);

                JSONArray results = jObject.getJSONObject("response").getJSONArray("venues");

                for(int i = 0; i < results.length(); i++){
                    JSONObject jsonPart = results.getJSONObject(i);

                    String ID = jsonPart.getString("id");
                    String name = jsonPart.getString("name");
                    IndividualLocation x = new IndividualLocation(ID,name);

                    allI.addLocation(x);
                }

                listLocation.add(allI);
                adapter.notifyDataSetChanged();
            }else{
                Log.i("I", "NO INFORMATION");
            }
        }catch(Exception e){e.printStackTrace();}
    }
}
