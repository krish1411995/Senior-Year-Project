package com.example.dell.homeonphoneaddingvaluetoyourhome;

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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class LightWatch extends AppCompatActivity {

    static String MQTTHOST="tcp://iot.eclipse.org:1883";
    static String USERNAME="username";
    static String PASSWORD="group14";
    MqttAndroidClient client;
    SharedPreferences sharedpreferences2;
    TextView last_update,ldr_read,ldr,last,prog,adj,diff;
    EditText editText;
    Button btn;
    SeekBar seekBar;
    ToggleButton tb;
    public static final String mypreference2 = "mypref2";
    public static final String Name2_1 = "name2_1";
    public static final String Name2_2 = "name2_2";
    public static final String Name2_3 = "name2_3";
    public static final String Name2_4 = "name2_4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_watch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ldr_read = (TextView) findViewById(R.id.textView31);
        last_update = (TextView) findViewById(R.id.textView33);
        ldr= (TextView) findViewById(R.id.textView30);
        last= (TextView) findViewById(R.id.textView32);
        prog= (TextView) findViewById(R.id.textView35);
        adj= (TextView) findViewById(R.id.textView34);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        editText = (EditText)findViewById(R.id.editText3);
        diff=(TextView)findViewById(R.id.textView16);
        btn=(Button)findViewById(R.id.button13);
        tb=(ToggleButton)findViewById(R.id.toggleButton);
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());


        try {
            IMqttToken token = client.connect(options);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(LightWatch.this, "Connected!!!", Toast.LENGTH_LONG).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(LightWatch.this, "NotConnected!!!", Toast.LENGTH_LONG).show();
                }
            });
        }catch (MqttException e) {
            e.printStackTrace();
        }
        sharedpreferences2 = getSharedPreferences(mypreference2,
                Context.MODE_PRIVATE);
        if (sharedpreferences2.contains(Name2_1)) {
            last_update.setText(sharedpreferences2.getString(Name2_1, ""));
        }
        if (sharedpreferences2.contains(Name2_3)) {
            ldr_read.setText(sharedpreferences2.getString(Name2_3, ""));
        }
        if (sharedpreferences2.contains(Name2_2)) {
            if((sharedpreferences2.getString(Name2_2, "")=="yes")){
                tb.setChecked(true);
            }
            else{
                tb.setChecked(false);
            }
        }
        if (sharedpreferences2.contains(Name2_4)) {
            editText.setText(sharedpreferences2.getString(Name2_4, ""));
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            String n1=null;
            @Override
            public void onProgressChanged(SeekBar seekBar1,int progress,boolean fromUser)
            {
                prog.setText(progress*10+"%");
                n1 = progress*10+"%";
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1)
            {
                SharedPreferences.Editor editor1 = sharedpreferences2.edit();
                editor1.putString(Name2_1, n1);
                editor1.commit();
                last_update.setText(n1);
                String message =n1;
                try {
                    client.publish("/nodemcu/intensity", message.getBytes(),0,false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

        });
        if(tb.isChecked()){
            seekBar.setVisibility(View.GONE);
            last.setVisibility(View.GONE);
            last_update.setVisibility(View.GONE);
            adj.setVisibility(View.GONE);
            prog.setVisibility(View.GONE);
            ldr.setVisibility(View.VISIBLE);
            ldr_read.setVisibility(View.VISIBLE);
            diff.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);
        }
        else{
            seekBar.setVisibility(View.VISIBLE);
            last.setVisibility(View.VISIBLE);
            last_update.setVisibility(View.VISIBLE);
            adj.setVisibility(View.VISIBLE);
            prog.setVisibility(View.VISIBLE);
            ldr.setVisibility(View.GONE);
            ldr_read.setVisibility(View.GONE);
            diff.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
        }
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tb.isChecked()){
                    SharedPreferences.Editor editor1 = sharedpreferences2.edit();
                    editor1.putString(Name2_2, "yes");
                    editor1.commit();
                    String message = editText.getText().toString();
                    seekBar.setVisibility(View.GONE);
                    last.setVisibility(View.GONE);
                    last_update.setVisibility(View.GONE);
                    adj.setVisibility(View.GONE);
                    prog.setVisibility(View.GONE);
                    ldr.setVisibility(View.VISIBLE);
                    ldr_read.setVisibility(View.VISIBLE);
                    diff.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.VISIBLE);
                    try {
                        client.publish("/nodemcu/intensity", message.getBytes(),0,false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                else{
                    SharedPreferences.Editor editor1 = sharedpreferences2.edit();
                    editor1.putString(Name2_2, "no");
                    editor1.commit();
                    seekBar.setVisibility(View.VISIBLE);
                    last.setVisibility(View.VISIBLE);
                    last_update.setVisibility(View.VISIBLE);
                    adj.setVisibility(View.VISIBLE);
                    prog.setVisibility(View.VISIBLE);
                    ldr.setVisibility(View.GONE);
                    ldr_read.setVisibility(View.GONE);
                    diff.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);
                    try {
                        client.publish("/nodemcu/intensity",sharedpreferences2.getString(Name2_1, "").getBytes(),0,false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor1 = sharedpreferences2.edit();
                String temp=editText.getText().toString();
                editor1.putString(Name2_4, temp);
                editor1.commit();

            }
        });
    }
    private void setSubscription(){
        try{
            client.subscribe("/nodemcu/automatic1",0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if (tb.isChecked()) {
                    String mes=new String((message.getPayload()));
                    if(!mes.isEmpty()) {
                        float value = Float.parseFloat(mes);
                        value = 100 - value;
                        mes=Float.toString(value);
                        ldr_read.setText(mes + "%");
                        client.publish("/nodemcu/automatic1", new byte[0],0,true);
                        SharedPreferences.Editor editor = sharedpreferences2.edit();
                        editor.putString(Name2_3, mes);
                        editor.commit();
                    }
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

}
