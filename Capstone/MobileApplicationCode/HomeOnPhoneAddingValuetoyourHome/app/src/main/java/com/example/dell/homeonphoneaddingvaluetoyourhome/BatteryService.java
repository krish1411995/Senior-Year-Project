package com.example.dell.homeonphoneaddingvaluetoyourhome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class BatteryService extends Service implements Runnable {
    Handler handler = new Handler();
    static String MQTTHOST="tcp://iot.eclipse.org:1883";
    static String USERNAME="username";
    static String PASSWORD="group14";
    MqttAndroidClient client;
    String percentage,check;
    int per;
    SharedPreferences sharedpreferences5;
    public static final String mypreference5 = "mypref5";
    public static final String Name5_1 = "name5_1";
    public static final String Name5_2 = "name5_2";
    boolean published=false;

    public BatteryService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
                    Toast.makeText(BatteryService.this, "Connected!!!", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(BatteryService.this, "NotConnected!!!", Toast.LENGTH_LONG).show();
                }
            });
        }catch (MqttException e) {
            e.printStackTrace();
        }
        handler.postDelayed(this, 10000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this.getBaseContext(), "Destroyed", Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(this);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void run() {

        sharedpreferences5 = getSharedPreferences(mypreference5, Context.MODE_PRIVATE);
        if (sharedpreferences5.contains(Name5_1)) {
            check = (sharedpreferences5.getString(Name5_1, ""));
        }
        if (sharedpreferences5.contains(Name5_2)) {
            percentage = (sharedpreferences5.getString(Name5_2, ""));
        }

        per=Integer.parseInt(percentage);

        if (check.matches("Smart Charging Feature: ON")) {
            getBatteryLevel();
        }
        handler.postDelayed(this, 10000);
    }
    public void getBatteryLevel() {
        final BroadcastReceiver AlarmReceiver = new BroadcastReceiver() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onReceive(final Context ctxt, final Intent intent) {
                ctxt.unregisterReceiver(this);
                final int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                if (level>=per) {
                    published=true;
                    String message="0";
                    try {
                        client.publish("/nodemcu/led", message.getBytes(),0,false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    String n = "Smart Charging Feature: OFF";
                    SharedPreferences.Editor editor = sharedpreferences5.edit();
                    editor.putString(Name5_1, n);
                    editor.commit();
                    Toast.makeText(getBaseContext(),Integer.toString(level),Toast.LENGTH_SHORT).show();
                    Notification.Builder builder = new Notification.Builder(BatteryService.this);
                    //builder.setContentText("Click here to turn on the settings again!!!").setContentTitle("Ambiance Created");
                    //Notification.Builder builder = new Notification.Builder(BackgroundService.this);
                    builder.setContentText("Click here to turn on the settings again!!!").setContentTitle("Battery Charged to desired Level...");
                    builder.setSmallIcon(R.drawable.ic_battery);
                    Intent secondActivityIntent = new Intent(BatteryService.this, SmartCharging.class);
                    // pending intent
                    PendingIntent pendingIntent = PendingIntent.getActivity(BatteryService.this, 0, secondActivityIntent, 0);
                    builder.setContentIntent(pendingIntent);
                    //builder.setAutoCancel(false);
                    Notification notification = builder.build();
                    notification.flags |= Notification.FLAG_NO_CLEAR;
                    NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(5, notification);;
                    //onDestroy();
                }
            }
        };
        this.registerReceiver(AlarmReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
}
