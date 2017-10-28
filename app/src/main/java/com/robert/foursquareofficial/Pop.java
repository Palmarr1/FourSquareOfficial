package com.robert.foursquareofficial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Robert on 10/23/2017.
 */

public class Pop extends Activity {

    LinearLayout ll;
    RadioGroup group;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop);

        ll = (LinearLayout)findViewById(R.id.popOut);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8),(int)(height * .8));

        Bundle b = getIntent().getExtras();
        group = new RadioGroup(this);
        group.setOrientation(RadioGroup.VERTICAL);

        int x = 0;
        for(String s : b.getStringArrayList("Locations")){
            if(x > 5){break;}
            RadioButton btn1 = new RadioButton(this);
            btn1.setId(x);
            btn1.setText(s);

            group.addView(btn1);
            x++;
        }

        ll.addView(group);

        Button btn = new Button(this);
        btn.setText("Set Location");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getApplicationContext(),"Please Select One Location",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    Log.i("SELECT",Integer.toString(group.getCheckedRadioButtonId()));
                    intent.putExtra("ItemSelected",Integer.toString(group.getCheckedRadioButtonId()));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        ll.addView(btn);

        Button btn2 = new Button(this);
        btn2.setText("Close Window");

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("ItemSelected","-1");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ll.addView(btn2);

        Button btn3 = new Button(this);
        btn3.setText("Delete Location");

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("ItemSelected","-2");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ll.addView(btn3);




    }
    public void stopActivity(View v){
        finish();
    }
}

