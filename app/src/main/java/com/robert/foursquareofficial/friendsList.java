package com.robert.foursquareofficial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class friendsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");

        JSONArray friendslist;
        ArrayList<String> friends = new ArrayList<String>();

        try {
            friendslist = new JSONArray(jsondata);
            for (int l=0; l < friendslist.length(); l++) {
                friends.add(friendslist.getJSONObject(l).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.listview, friends); // simple textview for list item
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

}

