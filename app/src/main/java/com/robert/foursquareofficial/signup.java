package com.robert.foursquareofficial;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class signup extends AppCompatActivity {

    EditText nameText;
    EditText addressText;
    EditText emailText;
    EditText mobileText;
    EditText passwordText;
    EditText reEnterPasswordText;
    Button signupButton;
    TextView loginLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameText = (EditText)findViewById(R.id.input_name);
        addressText = (EditText)findViewById(R.id.input_address);
        emailText = (EditText)findViewById(R.id.input_email);
        mobileText = (EditText)findViewById(R.id.input_mobile);
        passwordText = (EditText)findViewById(R.id.input_password);
        reEnterPasswordText = (EditText)findViewById(R.id.input_reEnterPassword);


    }
    public void signup_2_database(View v){

        if(nameText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Username",Toast.LENGTH_SHORT).show();
            return;
        }else if(addressText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Address",Toast.LENGTH_SHORT).show();
            return;
        }else if(emailText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }else if(mobileText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Mobile",Toast.LENGTH_SHORT).show();
            return;
        }else if(passwordText.getText().toString().equals("") || reEnterPasswordText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }else if(!passwordText.getText().toString().equals(reEnterPasswordText.getText().toString())){
            Toast.makeText(getApplicationContext(),"Passwords Do Not Match",Toast.LENGTH_SHORT).show();
            return;
        }

        if(nameText.getText().toString().contains(" ")){
            Toast.makeText(getApplicationContext(),"UserName can only contain letters and numbers no Spaces",Toast.LENGTH_SHORT).show();
            return;
        }

        DownloadUser user = new DownloadUser();
        JSONObject obj;

        try {
            String result = user.execute(nameText.getText().toString()).get();
            obj = new JSONObject(result);
            if(obj.length() == 0){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");

                myRef.child(nameText.getText().toString()).child("id").setValue(nameText.getText().toString());
                myRef.child(nameText.getText().toString()).child("address").setValue(addressText.getText().toString());
                myRef.child(nameText.getText().toString()).child("email").setValue(emailText.getText().toString());
                myRef.child(nameText.getText().toString()).child("mobile").setValue(mobileText.getText().toString());
                myRef.child(nameText.getText().toString()).child("password").setValue(passwordText.getText().toString());

                Intent i = new Intent(signup.this,locationActivity.class);

                Bundle b = new Bundle();
                b.putString("user",nameText.getText().toString());

                i.putExtras(b);
                startActivity(i);
                finish();

            }else{
                Toast.makeText(getApplicationContext(),"User Already Exists Please Login",Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){e.printStackTrace();}

    }

    public class DownloadUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String finalLink = "https://foursquarenjit.firebaseio.com/users.json?orderBy=\"id\"&equalTo=\"" + strings[0] + "\"";

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;


            try{
                url = new URL(finalLink);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in  = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data != -1){
                    result += (char)data;
                    data = reader.read();
                }
            }catch (Exception e){
                return e.toString();
            }
            //Log.i("Results",result);
            return result;

        }
    }
    public void Login(View v){
        Intent i = new Intent(signup.this,login.class);
        startActivity(i);
    }

}
