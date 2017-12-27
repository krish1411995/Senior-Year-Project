package com.example.dell.homeonphoneaddingvaluetoyourhome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

public class BackgroundService extends Service implements Runnable{
    Handler handler=new Handler();
    public String ssid = null;
    static String MQTTHOST="tcp://iot.eclipse.org:1883";
    static String USERNAME="username";
    static String PASSWORD="group14";
    MqttAndroidClient client;
    String check,light1,light2;
    SharedPreferences sharedpreferences3;
    public static final String mypreference3 = "mypref3";
    public static final String Name3_1 = "name3_1";
    public static final String Name3_2 = "name3_2";
    public static final String Name3_3 = "name3_3";
    boolean published=false;

    public BackgroundService() {
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
                    Toast.makeText(BackgroundService.this, "Connected!!!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(BackgroundService.this, "NotConnected!!!", Toast.LENGTH_LONG).show();
                }
            });
        }catch (MqttException e) {
            e.printStackTrace();
        }
        handler.postDelayed(this,10000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this.getBaseContext(),"Destroyed",Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void run() {

        sharedpreferences3 = getSharedPreferences(mypreference3, Context.MODE_PRIVATE);
        if (sharedpreferences3.contains(Name3_1)) {
            check=(sharedpreferences3.getString(Name3_1, ""));
        }
        if (sharedpreferences3.contains(Name3_2)) {
            light1=(sharedpreferences3.getString(Name3_2, ""));
        }
        if (sharedpreferences3.contains(Name3_3)) {
            light2=(sharedpreferences3.getString(Name3_3, ""));
        }
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            ssid = connectionInfo.getSSID();
            if(ssid.contains("DLINK") && check.matches("Auto Wi-Fi Ambiance: ON") && !published){
                published=true;
                try {
                    client.publish("/nodemcu/led", light1.getBytes(),0,false);
                    client.publish("/nodemcu/intensity", light2.getBytes(),0,false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                String n = "Auto Wi-Fi Ambiance: OFF";
                SharedPreferences.Editor editor = sharedpreferences3.edit();
                editor.putString(Name3_1, n);
                editor.commit();
                Toast.makeText(this.getBaseContext(),"published"+light2+"!!!",Toast.LENGTH_SHORT).show();
                Notification.Builder builder = new Notification.Builder(BackgroundService.this);
                //builder.setContentText("Click here to turn on the settings again!!!").setContentTitle("Ambiance Created");
                //Notification.Builder builder = new Notification.Builder(BackgroundService.this);
                builder.setContentText("Click here to turn on the settings again!!!").setContentTitle("Ambiance Created");
                builder.setSmallIcon(R.drawable.ic_mod3);
                Intent secondActivityIntent = new Intent(BackgroundService.this, AmbianceAssistant.class);
                // pending intent
                PendingIntent pendingIntent = PendingIntent.getActivity(BackgroundService.this, 0, secondActivityIntent, 0);
                builder.setContentIntent(pendingIntent);
                //builder.setAutoCancel(false);
                Notification notification = builder.build();
                notification.flags |= Notification.FLAG_NO_CLEAR;
                NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(3, notification);;
                onDestroy();
            }
            else{
                published=false;
            }
            handler.postDelayed(this,10000);
        }
        else{
            published=false;
            handler.postDelayed(this,15000);
        }

    }
}
