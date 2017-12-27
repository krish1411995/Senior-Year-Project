package com.example.dell.homeonphoneaddingvaluetoyourhome;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SmartCharging extends AppCompatActivity {

    SharedPreferences sharedpreferences5;
    TextView tv5_1;
    EditText et5_1;
    Button btn5_1, btn5_2, btn5_3;
    public static final String mypreference5 = "mypref5";
    public static final String Name5_1 = "name5_1";
    public static final String Name5_2 = "name5_2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_charging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv5_1=(TextView)findViewById(R.id.noti);
        et5_1=(EditText)findViewById(R.id.editText5);
        btn5_1=(Button)findViewById(R.id.yes);
        btn5_2=(Button)findViewById(R.id.no);
        btn5_3=(Button)findViewById(R.id.button14);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(5);
        sharedpreferences5 = getSharedPreferences(mypreference5,
                Context.MODE_PRIVATE);
        if (sharedpreferences5.contains(Name5_1)) {
            tv5_1.setText(sharedpreferences5.getString(Name5_1, ""));
        }
        if (sharedpreferences5.contains(Name5_2)) {
            et5_1.setText(sharedpreferences5.getString(Name5_2, ""));
        }
        btn5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv5_1.setText("Smart Charging Feature: ON");
                String n = "Smart Charging Feature: ON";
                SharedPreferences.Editor editor = sharedpreferences5.edit();
                editor.putString(Name5_1, n);
                editor.commit();
            }
        });
        btn5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv5_1.setText("Smart Charging Feature: OFF");
                String n = "Smart Charging Feature: OFF";
                SharedPreferences.Editor editor = sharedpreferences5.edit();
                editor.putString(Name5_1, n);
                editor.commit();
            }
        });
        btn5_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n1=et5_1.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences5.edit();
                editor.putString(Name5_2, n1);
                editor.commit();
            }
        });
    }

}
