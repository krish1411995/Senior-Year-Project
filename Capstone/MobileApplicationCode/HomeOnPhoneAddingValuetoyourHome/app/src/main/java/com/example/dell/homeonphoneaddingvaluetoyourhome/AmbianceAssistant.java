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

public class AmbianceAssistant extends AppCompatActivity {

    SharedPreferences sharedpreferences3;
    TextView tv;
    EditText et1, et2;
    Button btn1, btn2, btn3;
    public static final String mypreference3 = "mypref3";
    public static final String Name3_1 = "name3_1";
    public static final String Name3_2 = "name3_2";
    public static final String Name3_3 = "name3_3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambiance_assistant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv=(TextView)findViewById(R.id.textView2);
        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        btn1=(Button)findViewById(R.id.button3);
        btn2=(Button)findViewById(R.id.button5);
        btn3=(Button)findViewById(R.id.button8);
        sharedpreferences3 = getSharedPreferences(mypreference3,
                Context.MODE_PRIVATE);
        if (sharedpreferences3.contains(Name3_1)) {
            tv.setText(sharedpreferences3.getString(Name3_1, ""));
        }
        if (sharedpreferences3.contains(Name3_2)) {
            et1.setText(sharedpreferences3.getString(Name3_2, ""));
        }
        if (sharedpreferences3.contains(Name3_3)) {
            et2.setText(sharedpreferences3.getString(Name3_3, ""));
        }
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(3);
    }

    public void On(View view){
        tv.setText("Auto Wi-Fi Ambiance: ON");
        String n = "Auto Wi-Fi Ambiance: ON";
        SharedPreferences.Editor editor = sharedpreferences3.edit();
        editor.putString(Name3_1, n);
        editor.commit();
    }
    public void Off(View view){
        tv.setText("Auto Wi-Fi Ambiance: OFF");
        String n = "Auto Wi-Fi Ambiance: OFF";
        SharedPreferences.Editor editor = sharedpreferences3.edit();
        editor.putString(Name3_1, n);
        editor.commit();
    }
    public void Confirm(View view){
        String n1=et1.getText().toString();
        String n2=et2.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences3.edit();
        editor.putString(Name3_2, n1);
        editor.putString(Name3_3, n2);
        editor.commit();
    }
}
