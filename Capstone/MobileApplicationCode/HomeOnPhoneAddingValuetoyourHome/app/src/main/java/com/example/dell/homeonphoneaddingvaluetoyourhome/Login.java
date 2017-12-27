package com.example.dell.homeonphoneaddingvaluetoyourhome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText et1,et2;
    Button btn1,btn2;
    SharedPreferences sharedpreferences0;
    public static final String mypreference0 = "mypref0";
    public static final String Name0_1 = "name0_1";
    public static final String Name0_2 = "name0_2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        btn1 = (Button) findViewById(R.id.button7);
        btn2 = (Button) findViewById(R.id.button4);
        final Intent intent=getIntent();
        sharedpreferences0 = getSharedPreferences(mypreference0, Context.MODE_PRIVATE);
        if (sharedpreferences0.contains(Name0_1)) {
            et1.setText(sharedpreferences0.getString(Name0_1, ""));
        }
        if (sharedpreferences0.contains(Name0_2)) {
            et2.setText(sharedpreferences0.getString(Name0_2, ""));
        }

        if(et1.getText().toString().matches("username") && et2.getText().toString().matches("group14")) {
            Intent i = new Intent(Login.this, HomePage.class);
            startActivity(i);
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et1.getText().toString().matches("username") && et2.getText().toString().matches("group14")) {
                    String n1 = et1.getText().toString();
                    String n2 = et2.getText().toString();
                    SharedPreferences.Editor editor = sharedpreferences0.edit();
                    editor.putString(Name0_1, n1);
                    editor.putString(Name0_2, n2);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Login.this, HomePage.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Incorrect Username or Password",Toast.LENGTH_LONG).show();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setText("");
                et2.setText("");
                String n1 = et1.getText().toString();
                String n2 = et2.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences0.edit();
                editor.putString(Name0_1, n1);
                editor.putString(Name0_2, n2);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
            }
        });
    }

}
