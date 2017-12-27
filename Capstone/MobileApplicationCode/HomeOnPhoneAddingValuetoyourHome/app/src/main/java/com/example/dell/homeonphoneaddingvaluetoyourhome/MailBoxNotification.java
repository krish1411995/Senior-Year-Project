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
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

public class MailBoxNotification extends AppCompatActivity {

    static String MQTTHOST="tcp://iot.eclipse.org:1883";
    static String USERNAME="username";
    static String PASSWORD="group14";
    String topicstrg="/sensors/temp";
    MqttAndroidClient client;
    Button yes,no;
    SharedPreferences sharedpreferences4;
    TextView notify,mailify,dateify;
    public static final String mypreference4 = "mypref4";
    public static final String Name4_1 = "name4_1";
    public static final String Name4_2 = "name4_2";
    Vibrator vibrator;
    Ringtone myRing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        myRing= RingtoneManager.getRingtone(getApplicationContext(),uri);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        dateify=(TextView)findViewById(R.id.date);
        notify=(TextView)findViewById(R.id.noti);
        mailify=(TextView)findViewById(R.id.lastmail);

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
                    Toast.makeText(MailBoxNotification.this, "Connected!!!", Toast.LENGTH_SHORT).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MailBoxNotification.this, "NotConnected!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (MqttException e) {
            e.printStackTrace();
        }

        sharedpreferences4 = getSharedPreferences(mypreference4, Context.MODE_PRIVATE);
        if (sharedpreferences4.contains(Name4_1)) {
            notify.setText(sharedpreferences4.getString(Name4_1, ""));
        }
        if (sharedpreferences4.contains(Name4_2)) {
            dateify.setText(sharedpreferences4.getString(Name4_2, ""));
        }
    }

    private void setSubscription(){
        try{
            client.subscribe(topicstrg,0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if (sharedpreferences4.getString(Name4_1, "").matches("Mail-Box Notifications: ON")) {
                    String mes=new String((message.getPayload()));
                    if(!mes.isEmpty()) {
                        String mydate;
                        dateify = (TextView) findViewById(R.id.date);
                        mydate = new String(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
                        dateify.setText(mydate);
                        vibrator.vibrate(500);
                        myRing.play();
                        SharedPreferences.Editor editor = sharedpreferences4.edit();
                        editor.putString(Name4_2, mydate);
                        editor.commit();
                        NotificationCompat.Builder builder =
                                (NotificationCompat.Builder) new NotificationCompat.Builder(MailBoxNotification.this)
                                        .setSmallIcon(R.drawable.ic_mod4)
                                        .setContentTitle("Module-4")
                                        .setContentText("You have received a mail!!!Click here to see the timing...");

                        Intent notificationIntent = new Intent(MailBoxNotification.this, MailBoxNotification.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(MailBoxNotification.this, 0, notificationIntent, 0);
                        builder.setContentIntent(contentIntent);
                        // Add as notification
                        builder.setAutoCancel(true);
                        Notification notification = builder.build();
                        notification.flags |= Notification.FLAG_NO_CLEAR;
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(4, notification);
                    }
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void yes_1(View view) {

        notify = (TextView) findViewById(R.id.noti);
        notify.setText("Mail-Box Notifications: ON");
        String n = "Mail-Box Notifications: ON";
        SharedPreferences.Editor editor = sharedpreferences4.edit();
        editor.putString(Name4_1, n);
        editor.commit();
    }

    public void no_1(View view) {
        notify = (TextView) findViewById(R.id.noti);
        notify.setText("Mail-Box Notifications: OFF");
        String n = "Mail-Box Notifications: OFF";
        SharedPreferences.Editor editor = sharedpreferences4.edit();
        editor.putString(Name4_1, n);
        editor.commit();
    }
}
