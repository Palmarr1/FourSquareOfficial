package com.robert.foursquareofficial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class comment_rating extends AppCompatActivity {

    public int oldRating;
    public String oldComment;
    public String locationid;

    public EditText comments;
    public RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_rating);

        Bundle b = getIntent().getExtras();

        oldRating = b.getInt("rating");
        oldComment = b.getString("comment");
        locationid = b.getString("ID");


        Log.i("ID",locationid);
        Log.i("Rating",Integer.toString(oldRating));
        Log.i("Comment",oldComment);

        comments = (EditText) findViewById(R.id.editText);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        comments.setText(oldComment);
        if(oldRating != -1){
            ratingBar.setRating(oldRating);
        }
    }

    public void update(View v){
        if((int)ratingBar.getRating() != oldRating || !oldComment.equals(comments.getText().toString())){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("locations").child(locationid);

            myRef.child("rating").setValue(ratingBar.getRating());
            myRef.child("comment").setValue(comments.getText().toString());

            finish();

        }else{
            Toast.makeText(getApplicationContext(), "Nothing has been updated", Toast.LENGTH_SHORT).show();
        }


    }

    public void cancel(View v){
        finish();
    }
}
