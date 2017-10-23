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
        String newRecord ="{\n" +
                "   \"meta\":{\n" +
                "      \"code\":200,\n" +
                "      \"requestId\":\"59e9ff84351e3d0f92c55284\"\n" +
                "   },\n" +
                "   \"response\":{\n" +
                "      \"venues\":[\n" +
                "         {\n" +
                "            \"id\":\"4c2e58cc452620a102ae1b0f\",\n" +
                "            \"name\":\"Highlands, NJ\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.4001514016634,\n" +
                "               \"lng\":-73.98640334836395,\n" +
                "               \"distance\":1152,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"state\":\"New Jersey\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"New Jersey\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"50aa9e094b90af0d42d5de0d\",\n" +
                "                  \"name\":\"City\",\n" +
                "                  \"pluralName\":\"Cities\",\n" +
                "                  \"shortName\":\"City\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/parks_outdoors\\/neighborhood_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":2196,\n" +
                "               \"usersCount\":796,\n" +
                "               \"tipCount\":3\n" +
                "            },\n" +
                "            \"venueRatingBlacklisted\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4e443a241838e44e898a1b42\",\n" +
                "            \"name\":\"M&M Auto\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"Hwy 36\",\n" +
                "               \"lat\":40.40254581894167,\n" +
                "               \"lng\":-73.99613652072439,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40254581894167,\n" +
                "                     \"lng\":-73.99613652072439\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":433,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Atl Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Hwy 36\",\n" +
                "                  \"Atl Highlands, NJ\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d124951735\",\n" +
                "                  \"name\":\"Automotive Shop\",\n" +
                "                  \"pluralName\":\"Automotive Shops\",\n" +
                "                  \"shortName\":\"Automotive\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/shops\\/automotive_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":1,\n" +
                "               \"usersCount\":1,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4f3261d019836c91c7d24df1\",\n" +
                "            \"name\":\"The Sand Witch Shop\",\n" +
                "            \"contact\":{\n" +
                "               \"phone\":\"7328724150\",\n" +
                "               \"formattedPhone\":\"(732) 872-4150\"\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"71 Waterwitch Ave\",\n" +
                "               \"lat\":40.40055,\n" +
                "               \"lng\":-73.99569,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40055,\n" +
                "                     \"lng\":-73.99569\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":370,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"71 Waterwitch Ave\",\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d146941735\",\n" +
                "                  \"name\":\"Deli \\/ Bodega\",\n" +
                "                  \"pluralName\":\"Delis \\/ Bodegas\",\n" +
                "                  \"shortName\":\"Deli \\/ Bodega\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/food\\/deli_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":1,\n" +
                "               \"usersCount\":1,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4de4498ac65b7a3e214742df\",\n" +
                "            \"name\":\"Callinans\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"200 Laurel Dr\",\n" +
                "               \"crossStreet\":\"hwy 35\",\n" +
                "               \"lat\":40.40581176,\n" +
                "               \"lng\":-74.0013029,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40581176,\n" +
                "                     \"lng\":-74.0013029\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":656,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"200 Laurel Dr (hwy 35)\",\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4d4b7105d754a06375d81259\",\n" +
                "                  \"name\":\"Professional & Other Places\",\n" +
                "                  \"pluralName\":\"Professional & Other Places\",\n" +
                "                  \"shortName\":\"Professional\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/building\\/default_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":28,\n" +
                "               \"usersCount\":3,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"venueRatingBlacklisted\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4b61f9edf964a5208a2e2ae3\",\n" +
                "            \"name\":\"Hartshorne Woods Park (Buttermilk Valley)\",\n" +
                "            \"contact\":{\n" +
                "               \"phone\":\"7328720336\",\n" +
                "               \"formattedPhone\":\"(732) 872-0336\",\n" +
                "               \"twitter\":\"moncountyparks\"\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"300 Navesink Avenue\",\n" +
                "               \"lat\":40.39637210543771,\n" +
                "               \"lng\":-74.00167465209961,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.39637210543771,\n" +
                "                     \"lng\":-74.00167465209961\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":428,\n" +
                "               \"postalCode\":\"07716\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Locust\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"300 Navesink Avenue\",\n" +
                "                  \"Locust, NJ 07716\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d163941735\",\n" +
                "                  \"name\":\"Park\",\n" +
                "                  \"pluralName\":\"Parks\",\n" +
                "                  \"shortName\":\"Park\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/parks_outdoors\\/park_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":true,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":1358,\n" +
                "               \"usersCount\":441,\n" +
                "               \"tipCount\":8\n" +
                "            },\n" +
                "            \"url\":\"http:\\/\\/www.monmouthcountyparks.com\",\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "               {\n" +
                "                  \"id\":\"5569f7e2a7c8896abe7cecd2\"\n" +
                "               }\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4d9a18657fedb60c8542e946\",\n" +
                "            \"name\":\"School Park\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"S Linden Ave\",\n" +
                "               \"crossStreet\":\"hwy 36\",\n" +
                "               \"lat\":40.4035646,\n" +
                "               \"lng\":-73.99734415,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.4035646,\n" +
                "                     \"lng\":-73.99734415\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":456,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"S Linden Ave (hwy 36)\",\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d1e7941735\",\n" +
                "                  \"name\":\"Playground\",\n" +
                "                  \"pluralName\":\"Playgrounds\",\n" +
                "                  \"shortName\":\"Playground\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/parks_outdoors\\/playground_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":4,\n" +
                "               \"usersCount\":1,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"5089287ee4b008b0aa3760b6\",\n" +
                "            \"name\":\"Monmouth Hills\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.40025554726759,\n" +
                "               \"lng\":-73.99354832557317,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40025554726759,\n" +
                "                     \"lng\":-73.99354832557317\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":547,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4f2a210c4b9023bd5841ed28\",\n" +
                "                  \"name\":\"Housing Development\",\n" +
                "                  \"pluralName\":\"Housing Developments\",\n" +
                "                  \"shortName\":\"Housing Development\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/building\\/housingdevelopment_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":18,\n" +
                "               \"usersCount\":11,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"venueRatingBlacklisted\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"5215414711d21de129969805\",\n" +
                "            \"name\":\"Anna C Little, Esq\",\n" +
                "            \"contact\":{\n" +
                "               \"phone\":\"7327081309\",\n" +
                "               \"formattedPhone\":\"(732) 708-1309\"\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"426 State Route 36 Ste 3\",\n" +
                "               \"crossStreet\":\"Linden Avenue\",\n" +
                "               \"lat\":40.40390944270046,\n" +
                "               \"lng\":-73.99994701493932,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40390944270046,\n" +
                "                     \"lng\":-73.99994701493932\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":435,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"426 State Route 36 Ste 3 (Linden Avenue)\",\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d124941735\",\n" +
                "                  \"name\":\"Office\",\n" +
                "                  \"pluralName\":\"Offices\",\n" +
                "                  \"shortName\":\"Office\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/building\\/default_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":43,\n" +
                "               \"usersCount\":2,\n" +
                "               \"tipCount\":1\n" +
                "            },\n" +
                "            \"venueRatingBlacklisted\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"520793b5498ea3ff7a859d31\",\n" +
                "            \"name\":\"Sandy Hook Beach D\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.42462968075255,\n" +
                "               \"lng\":-73.98216214776504,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.42462968075255,\n" +
                "                     \"lng\":-73.98216214776504\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":3130,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Highlands, NJ\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d1e2941735\",\n" +
                "                  \"name\":\"Beach\",\n" +
                "                  \"pluralName\":\"Beaches\",\n" +
                "                  \"shortName\":\"Beach\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/parks_outdoors\\/beach_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":561,\n" +
                "               \"usersCount\":452,\n" +
                "               \"tipCount\":5\n" +
                "            },\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"536d8f26498ec47bb0f35ab9\",\n" +
                "            \"name\":\"Gore's Grove\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"144 Marina Ct, Highlands, NJ 07732\",\n" +
                "               \"lat\":40.403811,\n" +
                "               \"lng\":-73.991026,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.403811,\n" +
                "                     \"lng\":-73.991026\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":871,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"144 Marina Ct, Highlands, NJ 07732\",\n" +
                "                  \"Highlands, NJ\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d118941735\",\n" +
                "                  \"name\":\"Dive Bar\",\n" +
                "                  \"pluralName\":\"Dive Bars\",\n" +
                "                  \"shortName\":\"Dive Bar\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/nightlife\\/divebar_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":13,\n" +
                "               \"usersCount\":5,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"5579ed3b498ea3ff8b540c7d\",\n" +
                "            \"name\":\"Joe's Green Car\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.4050178527832,\n" +
                "               \"lng\":-74.00325012207031,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.4050178527832,\n" +
                "                     \"lng\":-74.00325012207031\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":622,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Middletown\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Middletown, NJ\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"54541b70498ea6ccd0204bff\",\n" +
                "                  \"name\":\"Transportation Service\",\n" +
                "                  \"pluralName\":\"Transportation Services\",\n" +
                "                  \"shortName\":\"Transportation Services\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/travel\\/taxi_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":3,\n" +
                "               \"usersCount\":3,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"venueRatingBlacklisted\":true,\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4cafa588aef16dcb703baa54\",\n" +
                "            \"name\":\"Ming Chu\",\n" +
                "            \"contact\":{\n" +
                "               \"phone\":\"7322913882\",\n" +
                "               \"formattedPhone\":\"(732) 291-3882\"\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"2391 State Route 36\",\n" +
                "               \"lat\":40.405821419999846,\n" +
                "               \"lng\":-74.00712591776553,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.405821419999846,\n" +
                "                     \"lng\":-74.00712591776553\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":885,\n" +
                "               \"postalCode\":\"07716\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Atlantic Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"2391 State Route 36\",\n" +
                "                  \"Atlantic Highlands, NJ 07716\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d145941735\",\n" +
                "                  \"name\":\"Chinese Restaurant\",\n" +
                "                  \"pluralName\":\"Chinese Restaurants\",\n" +
                "                  \"shortName\":\"Chinese\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/food\\/asian_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":17,\n" +
                "               \"usersCount\":9,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4e594b8762e1de72f6f02b15\",\n" +
                "            \"name\":\"Beerricane\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.399341,\n" +
                "               \"lng\":-73.990376,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.399341,\n" +
                "                     \"lng\":-73.990376\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":819,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Highlands, NJ\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d1d4941735\",\n" +
                "                  \"name\":\"Speakeasy\",\n" +
                "                  \"pluralName\":\"Speakeasies\",\n" +
                "                  \"shortName\":\"Speakeasy\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/nightlife\\/secretbar_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":6,\n" +
                "               \"usersCount\":4,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4db1ef540437a93f7f95a7ac\",\n" +
                "            \"name\":\"Navesink Baseball Batting Cages\",\n" +
                "            \"contact\":{\n" +
                "               \"phone\":\"7328721010\",\n" +
                "               \"formattedPhone\":\"(732) 872-1010\"\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"2365 Hwy 36\",\n" +
                "               \"crossStreet\":\"Scenic Dr\",\n" +
                "               \"lat\":40.40581997860859,\n" +
                "               \"lng\":-74.00676439672492,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40581997860859,\n" +
                "                     \"lng\":-74.00676439672492\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":865,\n" +
                "               \"postalCode\":\"07716\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Atlantic Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"2365 Hwy 36 (Scenic Dr)\",\n" +
                "                  \"Atlantic Highlands, NJ 07716\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d18c941735\",\n" +
                "                  \"name\":\"Baseball Stadium\",\n" +
                "                  \"pluralName\":\"Baseball Stadiums\",\n" +
                "                  \"shortName\":\"Baseball\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/arts_entertainment\\/stadium_baseball_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":25,\n" +
                "               \"usersCount\":14,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4c84d691d92ea093cd4d6072\",\n" +
                "            \"name\":\"Hell\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"61 Matthew Ct\",\n" +
                "               \"lat\":40.40845602,\n" +
                "               \"lng\":-73.9986115694046,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40845602,\n" +
                "                     \"lng\":-73.9986115694046\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":948,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"61 Matthew Ct\",\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d12f951735\",\n" +
                "                  \"name\":\"Resort\",\n" +
                "                  \"pluralName\":\"Resorts\",\n" +
                "                  \"shortName\":\"Resort\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/travel\\/resort_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":2,\n" +
                "               \"usersCount\":1,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4ec16e244690d2338a12c551\",\n" +
                "            \"name\":\"Primo's\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.404479262494576,\n" +
                "               \"lng\":-73.99257272188169,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.404479262494576,\n" +
                "                     \"lng\":-73.99257272188169\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":803,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d1ca941735\",\n" +
                "                  \"name\":\"Pizza Place\",\n" +
                "                  \"pluralName\":\"Pizza Places\",\n" +
                "                  \"shortName\":\"Pizza\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/food\\/pizza_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":34,\n" +
                "               \"usersCount\":18,\n" +
                "               \"tipCount\":1\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4f90bcbae4b0ccf0708ca499\",\n" +
                "            \"name\":\"Freezebox\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.405216,\n" +
                "               \"lng\":-74.001714,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.405216,\n" +
                "                     \"lng\":-74.001714\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":598,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4f2a210c4b9023bd5841ed28\",\n" +
                "                  \"name\":\"Housing Development\",\n" +
                "                  \"pluralName\":\"Housing Developments\",\n" +
                "                  \"shortName\":\"Housing Development\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/building\\/housingdevelopment_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":2,\n" +
                "               \"usersCount\":2,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"venueRatingBlacklisted\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4c54934a72cf0f472e90c5d4\",\n" +
                "            \"name\":\"LCN Nails\",\n" +
                "            \"contact\":{\n" +
                "               \"phone\":\"7322911409\",\n" +
                "               \"formattedPhone\":\"(732) 291-1409\"\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"2353 State Route 36\",\n" +
                "               \"lat\":40.405844205582866,\n" +
                "               \"lng\":-74.00688166939491,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.405844205582866,\n" +
                "                     \"lng\":-74.00688166939491\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":873,\n" +
                "               \"postalCode\":\"07716\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Atlantic Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"2353 State Route 36\",\n" +
                "                  \"Atlantic Highlands, NJ 07716\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d110951735\",\n" +
                "                  \"name\":\"Salon \\/ Barbershop\",\n" +
                "                  \"pluralName\":\"Salons \\/ Barbershops\",\n" +
                "                  \"shortName\":\"Salon \\/ Barbershop\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/shops\\/salon_barber_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":138,\n" +
                "               \"usersCount\":42,\n" +
                "               \"tipCount\":1\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"51964a80498e283d7ba7bc3a\",\n" +
                "            \"name\":\"coconut beach tanning\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.406158,\n" +
                "               \"lng\":-74.00625,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.406158,\n" +
                "                     \"lng\":-74.00625\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":866,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"state\":\"New Jersey\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"New Jersey\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4d1cf8421a97d635ce361c31\",\n" +
                "                  \"name\":\"Tanning Salon\",\n" +
                "                  \"pluralName\":\"Tanning Salons\",\n" +
                "                  \"shortName\":\"Tanning Salon\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/shops\\/tanning_salon_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":3,\n" +
                "               \"usersCount\":2,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4e14a837d16447c40d1a411e\",\n" +
                "            \"name\":\"Dr. Smolinsky\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"Memorial Pkwy\",\n" +
                "               \"lat\":40.4062423,\n" +
                "               \"lng\":-74.0067735,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.4062423,\n" +
                "                     \"lng\":-74.0067735\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":901,\n" +
                "               \"postalCode\":\"07716\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Atlantic Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Memorial Pkwy\",\n" +
                "                  \"Atlantic Highlands, NJ 07716\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d177941735\",\n" +
                "                  \"name\":\"Doctor's Office\",\n" +
                "                  \"pluralName\":\"Doctor's Offices\",\n" +
                "                  \"shortName\":\"Doctor's Office\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/building\\/medical_doctorsoffice_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":2,\n" +
                "               \"usersCount\":1,\n" +
                "               \"tipCount\":1\n" +
                "            },\n" +
                "            \"venueRatingBlacklisted\":true,\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4ff5e209e4b0f1abbb10cfc5\",\n" +
                "            \"name\":\"Tracy's Forest Palace\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.392389,\n" +
                "               \"lng\":-74.005723,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.392389,\n" +
                "                     \"lng\":-74.005723\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":976,\n" +
                "               \"postalCode\":\"07760\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Rumson\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Rumson, NJ 07760\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d165941735\",\n" +
                "                  \"name\":\"Scenic Lookout\",\n" +
                "                  \"pluralName\":\"Scenic Lookouts\",\n" +
                "                  \"shortName\":\"Scenic Lookout\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/parks_outdoors\\/sceniclookout_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":1,\n" +
                "               \"usersCount\":1,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"50146604e4b08f147c923009\",\n" +
                "            \"name\":\"49 2nd Street Studios\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"49 2nd St\",\n" +
                "               \"lat\":40.401859,\n" +
                "               \"lng\":-73.989163,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.401859,\n" +
                "                     \"lng\":-73.989163\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":941,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"49 2nd St\",\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d10d951735\",\n" +
                "                  \"name\":\"Record Shop\",\n" +
                "                  \"pluralName\":\"Record Shops\",\n" +
                "                  \"shortName\":\"Record Shop\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/shops\\/record_shop_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":1,\n" +
                "               \"usersCount\":1,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"50eacddee4b0cc7404463248\",\n" +
                "            \"name\":\"Seashore OBGYN\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"2367 State Route 36\",\n" +
                "               \"lat\":40.405749,\n" +
                "               \"lng\":-74.006443,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.405749,\n" +
                "                     \"lng\":-74.006443\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":841,\n" +
                "               \"postalCode\":\"07716\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Atlantic Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"2367 State Route 36\",\n" +
                "                  \"Atlantic Highlands, NJ 07716\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d177941735\",\n" +
                "                  \"name\":\"Doctor's Office\",\n" +
                "                  \"pluralName\":\"Doctor's Offices\",\n" +
                "                  \"shortName\":\"Doctor's Office\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/building\\/medical_doctorsoffice_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":24,\n" +
                "               \"usersCount\":8,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"venueRatingBlacklisted\":true,\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4f95e8b6e4b058034bdab0a8\",\n" +
                "            \"name\":\"Freezi-Yo\",\n" +
                "            \"contact\":{\n" +
                "               \"phone\":\"7322918400\",\n" +
                "               \"formattedPhone\":\"(732) 291-8400\",\n" +
                "               \"twitter\":\"FreeziYo\"\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"2355 State Route 36\",\n" +
                "               \"crossStreet\":\"Eastpointe (CVS) Plaza\",\n" +
                "               \"lat\":40.405884325722845,\n" +
                "               \"lng\":-74.00687136013752,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.405884325722845,\n" +
                "                     \"lng\":-74.00687136013752\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":876,\n" +
                "               \"postalCode\":\"07716\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Atlantic Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"2355 State Route 36 (Eastpointe (CVS) Plaza)\",\n" +
                "                  \"Atlantic Highlands, NJ 07716\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d1c9941735\",\n" +
                "                  \"name\":\"Ice Cream Shop\",\n" +
                "                  \"pluralName\":\"Ice Cream Shops\",\n" +
                "                  \"shortName\":\"Ice Cream\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/food\\/icecream_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":true,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":130,\n" +
                "               \"usersCount\":50,\n" +
                "               \"tipCount\":1\n" +
                "            },\n" +
                "            \"url\":\"http:\\/\\/freeziyo.com\\/\",\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"venuePage\":{\n" +
                "               \"id\":\"36628633\"\n" +
                "            },\n" +
                "            \"storeId\":\"\",\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"513fcbb5fe70f24a6c75acd8\",\n" +
                "            \"name\":\"FRANCESCO'S ITALIAN RESTAURANT\",\n" +
                "            \"contact\":{\n" +
                "               \"phone\":\"7328720080\",\n" +
                "               \"formattedPhone\":\"(732) 872-0080\"\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"279 Bay Ave\",\n" +
                "               \"lat\":40.40395423648657,\n" +
                "               \"lng\":-73.9915680885315,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40395423648657,\n" +
                "                     \"lng\":-73.9915680885315\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":839,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"279 Bay Ave\",\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d110941735\",\n" +
                "                  \"name\":\"Italian Restaurant\",\n" +
                "                  \"pluralName\":\"Italian Restaurants\",\n" +
                "                  \"shortName\":\"Italian\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/food\\/italian_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":4,\n" +
                "               \"usersCount\":4,\n" +
                "               \"tipCount\":1\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"5400d92d498e4a0b57e442a9\",\n" +
                "            \"name\":\"Top Tomato\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"Rt 36\",\n" +
                "               \"lat\":40.4056396484375,\n" +
                "               \"lng\":-74.00703430175781,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.4056396484375,\n" +
                "                     \"lng\":-74.00703430175781\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":865,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Atlantic Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Rt 36\",\n" +
                "                  \"Atlantic Highlands, NJ\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"52f2ab2ebcbc57f1066b8b1c\",\n" +
                "                  \"name\":\"Fruit & Vegetable Store\",\n" +
                "                  \"pluralName\":\"Fruit & Vegetable Stores\",\n" +
                "                  \"shortName\":\"Fruit & Vegetable Store\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/shops\\/food_grocery_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":10,\n" +
                "               \"usersCount\":8,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4ec18765775b98876ef2e340\",\n" +
                "            \"name\":\"East Point Nail Salon\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.40589758967482,\n" +
                "               \"lng\":-74.00690785753709,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40589758967482,\n" +
                "                     \"lng\":-74.00690785753709\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":879,\n" +
                "               \"postalCode\":\"07716\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Atlantic Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Atlantic Highlands, NJ 07716\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d110951735\",\n" +
                "                  \"name\":\"Salon \\/ Barbershop\",\n" +
                "                  \"pluralName\":\"Salons \\/ Barbershops\",\n" +
                "                  \"shortName\":\"Salon \\/ Barbershop\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/shops\\/salon_barber_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":18,\n" +
                "               \"usersCount\":10,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"allowMenuUrlEdit\":true,\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4ff07ec8e4b0db68d4946821\",\n" +
                "            \"name\":\"Gravelly Point Beach\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.408513571599,\n" +
                "               \"lng\":-73.99671039518196,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.408513571599,\n" +
                "                     \"lng\":-73.99671039518196\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":987,\n" +
                "               \"postalCode\":\"07732\",\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Highlands\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Highlands, NJ 07732\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "               {\n" +
                "                  \"id\":\"4bf58dd8d48988d1e2941735\",\n" +
                "                  \"name\":\"Beach\",\n" +
                "                  \"pluralName\":\"Beaches\",\n" +
                "                  \"shortName\":\"Beach\",\n" +
                "                  \"icon\":{\n" +
                "                     \"prefix\":\"https:\\/\\/ss3.4sqi.net\\/img\\/categories_v2\\/parks_outdoors\\/beach_\",\n" +
                "                     \"suffix\":\".png\"\n" +
                "                  },\n" +
                "                  \"primary\":true\n" +
                "               }\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":48,\n" +
                "               \"usersCount\":19,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4c6c7b29463537041ca606bc\",\n" +
                "            \"name\":\"Gravelly point rd.\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"lat\":40.40777,\n" +
                "               \"lng\":-73.99544,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.40777,\n" +
                "                     \"lng\":-73.99544\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":947,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Monmouth County\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Monmouth County, NJ\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":8,\n" +
                "               \"usersCount\":5,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"id\":\"4c4b39f9959220a13c5c460f\",\n" +
                "            \"name\":\"Stewarts\",\n" +
                "            \"contact\":{\n" +
                "\n" +
                "            },\n" +
                "            \"location\":{\n" +
                "               \"address\":\"Hwy 36 S\",\n" +
                "               \"lat\":40.405633,\n" +
                "               \"lng\":-74.00355,\n" +
                "               \"labeledLatLngs\":[\n" +
                "                  {\n" +
                "                     \"label\":\"display\",\n" +
                "                     \"lat\":40.405633,\n" +
                "                     \"lng\":-74.00355\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"distance\":695,\n" +
                "               \"cc\":\"US\",\n" +
                "               \"city\":\"Monmouth County\",\n" +
                "               \"state\":\"NJ\",\n" +
                "               \"country\":\"United States\",\n" +
                "               \"formattedAddress\":[\n" +
                "                  \"Hwy 36 S\",\n" +
                "                  \"Monmouth County, NJ\",\n" +
                "                  \"United States\"\n" +
                "               ]\n" +
                "            },\n" +
                "            \"categories\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"verified\":false,\n" +
                "            \"stats\":{\n" +
                "               \"checkinsCount\":5,\n" +
                "               \"usersCount\":5,\n" +
                "               \"tipCount\":0\n" +
                "            },\n" +
                "            \"beenHere\":{\n" +
                "               \"lastCheckinExpiredAt\":0\n" +
                "            },\n" +
                "            \"specials\":{\n" +
                "               \"count\":0,\n" +
                "               \"items\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"hereNow\":{\n" +
                "               \"count\":0,\n" +
                "               \"summary\":\"Nobody here\",\n" +
                "               \"groups\":[\n" +
                "\n" +
                "               ]\n" +
                "            },\n" +
                "            \"referralId\":\"v-1508507524\",\n" +
                "            \"venueChains\":[\n" +
                "\n" +
                "            ],\n" +
                "            \"hasPerk\":false\n" +
                "         }\n" +
                "      ],\n" +
                "      \"confident\":false\n" +
                "   }\n" +
                "}";

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
