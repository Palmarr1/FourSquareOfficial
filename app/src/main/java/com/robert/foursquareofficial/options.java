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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        try{
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
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
        }catch(Exception e){e.printStackTrace();}

    }

    public void shareMessage(View v){

        Bundle b = getIntent().getExtras();
        String shareLink = "https://foursquare.com/v/" + b.getString("LocationID");

        Intent message = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
        message.putExtra("sms_body", shareLink );
        startActivity(message);
    }

    public void addComment(View v){
        Intent i = new Intent(options.this,comment_rating.class);
        Bundle b = getIntent().getExtras();
        i.putExtras(b);

        startActivity(i);
    }

    public void exit(View v){
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    public void pictures(View v){
        Bundle b = getIntent().getExtras();
        String id = b.getString("LocationID");
        Log.i("I",id);
        Bundle give = new Bundle();
        give.putString("id",id);

        Intent i = new Intent(this,pictures.class);
        i.putExtras(give);
        startActivity(i);

    }

    public void Delete(View v){
        Bundle b = getIntent().getExtras();
        String ID = b.getString("ID");
        Log.i("I",ID);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("locations").child(ID);
        myRef.removeValue();

        finish();
    }
}
