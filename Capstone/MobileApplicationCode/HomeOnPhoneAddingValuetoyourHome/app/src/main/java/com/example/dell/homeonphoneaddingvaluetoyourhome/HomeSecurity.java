package com.example.dell.homeonphoneaddingvaluetoyourhome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Calendar;

public class HomeSecurity extends AppCompatActivity {

    static String MQTTHOST="tcp://iot.eclipse.org:1883";
    static String USERNAME="username";
    static String PASSWORD="group14";
    String topic="/raspi/security";
    MqttAndroidClient client;

    SharedPreferences sharedpreferences1;
    String dropbox;
    TextView notify,dateify,link;


    public static final String mypreference1 = "mypref1";
    public static final String Name1_1 = "name1_1";
    public static final String Name1_2 = "name1_2";
    Vibrator vibrator;
    Ringtone myRing;
    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_security);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        myRing= RingtoneManager.getRingtone(getApplicationContext(),uri);
        dateify=(TextView)findViewById(R.id.date);
        notify=(TextView)findViewById(R.id.noti);
        link=(TextView)findViewById(R.id.textView12);

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
                    Toast.makeText(HomeSecurity.this, "Connected!!!", Toast.LENGTH_SHORT).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(HomeSecurity.this, "NotConnected!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (MqttException e) {
            e.printStackTrace();
        }

        dropbox="https://www.dropbox.com/home/Alert...!!!";
        link.setMovementMethod(LinkMovementMethod.getInstance());
        link.setText(dropbox);
        sharedpreferences1 = getSharedPreferences(mypreference1,
                Context.MODE_PRIVATE);
        if (sharedpreferences1.contains(Name1_1)) {
            notify.setText(sharedpreferences1.getString(Name1_1, ""));
        }
        if (sharedpreferences1.contains(Name1_2)) {
            dateify.setText(sharedpreferences1.getString(Name1_2, ""));
        }
    }

    private void setSubscription(){
        try{
            client.subscribe(topic,0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if (sharedpreferences1.getString(Name1_1, "").matches("Security Notifications: ON")) {
                    String mes=new String((message.getPayload()));
                    if(!mes.isEmpty()) {
                        String mydate;
                        dateify = (TextView) findViewById(R.id.date);
                        mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        dateify.setText(mydate);
                        vibrator.vibrate(500);
                        myRing.play();
                        SharedPreferences.Editor editor = sharedpreferences1.edit();
                        editor.putString(Name1_2, mydate);
                        editor.commit();
                        NotificationCompat.Builder builder =
                                (NotificationCompat.Builder) new NotificationCompat.Builder(HomeSecurity.this)
                                        .setSmallIcon(R.drawable.ic_mod1)
                                        .setContentTitle("Module-1")
                                        .setContentText("Your Home has been Invaded!!!Click here to see photos");

                        Intent notificationIntent = new Intent(HomeSecurity.this, HomeSecurity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(HomeSecurity.this, 0, notificationIntent, 0);
                        builder.setContentIntent(contentIntent);
                        // Add as notification
                        builder.setAutoCancel(true);
                        Notification notification = builder.build();
                        notification.flags |= Notification.FLAG_NO_CLEAR;
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(1, notification);
                    }

                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    public void yes_1(View view) {
        notify.setText("Security Notifications: ON");
        String n = "Security Notifications: ON";
        SharedPreferences.Editor editor = sharedpreferences1.edit();
        editor.putString(Name1_1, n);
        editor.commit();
    }

    public void no_1(View view) {
        notify.setText("Security Notifications: OFF");
        String n = "Security Notifications: OFF";
        SharedPreferences.Editor editor = sharedpreferences1.edit();
        editor.putString(Name1_1, n);
        editor.commit();
    }
}
