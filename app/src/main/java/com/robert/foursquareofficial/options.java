package com.robert.foursquareofficial;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class options extends AppCompatActivity {

    public AllLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .5),(int)(height * .5));
    }

    public void shareFacebook(View v){

    }

    public void shareMessage(View v){
        Intent message = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
        message.putExtra("sms_body", "I was at NJIT on 10/15/2017" );
        startActivity(message);
    }

    public void addComment(View v){
        Intent i = new Intent(options.this,comment_rating.class);
        Bundle b = getIntent().getExtras();
        i.putExtras(b);

        startActivity(i);
        finish();

    }
}
