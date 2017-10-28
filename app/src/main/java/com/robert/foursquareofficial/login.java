package com.robert.foursquareofficial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class login extends AppCompatActivity {

    public EditText login;
    public EditText pass;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);


        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.facebook);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("USER",loginResult.getAccessToken().getUserId());


                DownloadUser user = new DownloadUser();
                JSONObject obj;

                try{
                    String result = user.execute(loginResult.getAccessToken().getUserId()).get();
                    obj = new JSONObject(result);

                    if(obj.length() == 0){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("users");

                        myRef.child(loginResult.getAccessToken().getUserId()).child("id").setValue(loginResult.getAccessToken().getUserId());
                        database.goOffline();

                        Intent i = new Intent(login.this,locationActivity.class);

                        Bundle b = new Bundle();
                        b.putString("user",loginResult.getAccessToken().getUserId());

                        i.putExtras(b);
                        startActivity(i);

                    }else{
                        SharedPreferences sharedPreferences = getSharedPreferences("com.robert.foursquareofficial", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("user",loginResult.getAccessToken().getUserId());

                        Intent i = new Intent(login.this,locationActivity.class);

                        Bundle b = new Bundle();
                        b.putString("user",loginResult.getAccessToken().getUserId());

                        i.putExtras(b);
                        startActivity(i);
                    }
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void onCancel() {
                Log.i("USER","Login Attempt Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("I",error.getMessage());
            }
        });

    }

    public void Signup(View v){
        Intent toy = new Intent(login.this,signup.class);
        startActivity(toy);
    }

    public void Login(View v){

        Log.i("I","Test");

        if(login.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Enter UserName",Toast.LENGTH_SHORT).show();
            return;
        }else if(pass.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        DownloadUser user = new DownloadUser();
        JSONObject obj;

        try{
            String result = user.execute(login.getText().toString()).get();
            obj = new JSONObject(result);

            if(obj.length() == 0){
                Toast.makeText(getApplicationContext(),"User Not Found",Toast.LENGTH_SHORT).show();
                return;
            }else{
                JSONObject obj2 = new JSONObject(obj.getString(obj.names().get(0).toString()));

                Log.i("I",obj2.toString());

                if(!obj2.getString("password").equals(pass.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Incorrect Password",Toast.LENGTH_SHORT).show();
                    pass.setText("");
                    return;
                }
                else{

                    SharedPreferences sharedPreferences = getSharedPreferences("com.robert.foursquareofficial", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("user",login.getText().toString());

                    Intent i = new Intent(login.this,locationActivity.class);

                    Bundle b = new Bundle();
                    b.putString("user",login.getText().toString());

                    i.putExtras(b);
                    startActivity(i);

                }
            }
        }catch (Exception e){e.printStackTrace();}

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
            Log.i("Results",result);
            return result;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
