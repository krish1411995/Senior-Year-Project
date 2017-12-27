package com.example.dell.homeonphoneaddingvaluetoyourhome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ClearTopic extends AppCompatActivity {

    Button btnc1,btnc2,btnc3,btnc4,btnc5;
    static String MQTTHOST="tcp://iot.eclipse.org:1883";
    static String USERNAME="username";
    static String PASSWORD="group14";
    MqttAndroidClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnc1=(Button)findViewById(R.id.button6);
        btnc2=(Button)findViewById(R.id.button9);
        btnc3=(Button)findViewById(R.id.button10);
        btnc4=(Button)findViewById(R.id.button11);
        btnc5=(Button)findViewById(R.id.button12);
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
                    Toast.makeText(ClearTopic.this, "Connected!!!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(ClearTopic.this, "NotConnected!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (MqttException e) {
            e.printStackTrace();
        }
        btnc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.publish("/raspi/security", new byte[0],0,true);
                    Toast.makeText(getApplicationContext(),"/raspi/security: Cleared",Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        btnc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.publish("/nodemcu/led", new byte[0],0,true);
                    Toast.makeText(getApplicationContext(),"/nodemcu/led: Cleared",Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        btnc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.publish("/nodemcu/intensity", new byte[0],0,true);
                    Toast.makeText(getApplicationContext(),"/nodemcu/intensity: Cleared",Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        btnc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.publish("/nodemcu/automatic1", new byte[0],0,true);
                    Toast.makeText(getApplicationContext(),"/nodemcu/automatic1: Cleared",Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        btnc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.publish("/sensors/temp", new byte[0],0,true);
                    Toast.makeText(getApplicationContext(),"/sensors/temp: Cleared",Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
