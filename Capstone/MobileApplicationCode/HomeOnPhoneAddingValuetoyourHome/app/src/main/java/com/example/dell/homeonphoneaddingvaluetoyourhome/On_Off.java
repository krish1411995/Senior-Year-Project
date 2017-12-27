package com.example.dell.homeonphoneaddingvaluetoyourhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class On_Off extends AppCompatActivity {

    static String MQTTHOST="tcp://iot.eclipse.org:1883";
    static String USERNAME="username";
    static String PASSWORD="group14";
    String topicstrg="/nodemcu/led";
    MqttAndroidClient client;

    SharedPreferences sharedpreferenceso_f;
    TextView status;
    public static final String mypreferenceo_f = "myprefo_f";
    public static final String Nameo_f = "nameo_f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on__off);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    Toast.makeText(On_Off.this, "Connected!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(On_Off.this, "NotConnected!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (MqttException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////
        status = (TextView) findViewById(R.id.textView2);
        sharedpreferenceso_f = getSharedPreferences(mypreferenceo_f, Context.MODE_PRIVATE);
        if (sharedpreferenceso_f.contains(Nameo_f)) {
            status.setText(sharedpreferenceso_f.getString(Nameo_f, ""));
            if(sharedpreferenceso_f.getString(Nameo_f, "")=="On")
            {
                ImageView imgView = (ImageView) findViewById(R.id.imageView5);
                ImageView imgView1 = (ImageView) findViewById(R.id.imageView6);
                imgView.setVisibility(View.VISIBLE);
                imgView1.setVisibility(View.INVISIBLE);
            }
            else
            {
                ImageView imgView = (ImageView) findViewById(R.id.imageView5);
                ImageView imgView1 = (ImageView) findViewById(R.id.imageView6);
                imgView.setVisibility(View.INVISIBLE);
                imgView1.setVisibility(View.VISIBLE);
            }
        }
    }

    public void On(View view) {
        ImageView imgView = (ImageView) findViewById(R.id.imageView5);
        ImageView imgView1 = (ImageView) findViewById(R.id.imageView6);
        imgView.setVisibility(View.VISIBLE);
        imgView1.setVisibility(View.INVISIBLE);
        status.setText("On");
        String n = "On";
        SharedPreferences.Editor editor = sharedpreferenceso_f.edit();
        editor.putString(Nameo_f, n);
        editor.commit();
        String topic = topicstrg;
        String message = "1";
        try {
            client.publish(topic, message.getBytes(),0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void Off(View view) {
        ImageView imgView = (ImageView) findViewById(R.id.imageView5);
        ImageView imgView1 = (ImageView) findViewById(R.id.imageView6);
        imgView.setVisibility(View.INVISIBLE);
        imgView1.setVisibility(View.VISIBLE);
        status.setText("Off");
        String n = "Off";
        SharedPreferences.Editor editor = sharedpreferenceso_f.edit();
        editor.putString(Nameo_f, n);
        editor.commit();
        String topic = topicstrg;
        String message = "0";
        try {
            client.publish(topic, message.getBytes(),0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
