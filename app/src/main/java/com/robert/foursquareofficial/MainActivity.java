package com.robert.foursquareofficial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPreferences = this.getSharedPreferences("com.robert.foursquareofficial", Context.MODE_PRIVATE);

        if(sharedPreferences.contains("user")){
            Intent i = new Intent(MainActivity.this,locationActivity.class);

            Bundle b = new Bundle();
            b.putString("user",sharedPreferences.getString("user",""));

            i.putExtras(b);
            startActivity(i);
        }else{
            Intent i = new Intent(MainActivity.this,login.class);
            startActivity(i);
        }
    }
}
