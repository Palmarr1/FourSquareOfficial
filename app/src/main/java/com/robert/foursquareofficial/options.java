package com.robert.foursquareofficial;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.share.Share;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

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

        getWindow().setLayout((int)(width * .7),(int)(height * .7));
    }

    public void shareFacebook(View v){

        Log.i("I","HERE");
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        Log.i("ACCESS",accessToken.toString());

        if(accessToken == null){
            Toast.makeText(getApplicationContext(), "User Account is not Linked to Facebook", Toast.LENGTH_SHORT).show();
        }else{
            Bundle b = getIntent().getExtras();
            String shareLink = "https://foursquare.com/v/" + b.getString("LocationID");
            Log.i("SHARE LINK",shareLink);

            ShareDialog shareDialog = new ShareDialog(this);
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(shareLink))
                    .build();

            shareDialog.show(content);
        }
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

    public void exit(View v){
        finish();
    }
}
