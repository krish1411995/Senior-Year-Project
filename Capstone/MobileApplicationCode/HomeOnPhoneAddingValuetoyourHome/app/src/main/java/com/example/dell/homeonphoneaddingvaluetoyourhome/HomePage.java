package com.example.dell.homeonphoneaddingvaluetoyourhome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton ib1,ib2,ib3,ib4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent= new Intent(HomePage.this,BackgroundService.class);
        startService(intent);
        Intent intent1 = new Intent(HomePage.this,BatteryService.class);
        startService(intent1);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ib1=(ImageButton)findViewById(R.id.imageButton);
        ib2=(ImageButton)findViewById(R.id.imageButton2);
        ib3=(ImageButton)findViewById(R.id.imageButton3);
        ib4=(ImageButton)findViewById(R.id.imageButton4);

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this,HomeSecurity.class);
                startActivity(i);
            }
        });
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this,LightWatch.class);
                startActivity(i);
            }
        });
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this,AmbianceAssistant.class);
                startActivity(i);
            }
        });
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this,MailBoxNotification.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mod1) {
            // Handle the camera action
            Intent i = new Intent(this,HomeSecurity.class);
            startActivity(i);
        } else if (id == R.id.nav_onoff) {
            Intent i = new Intent(this,On_Off.class);
            startActivity(i);
        } else if (id == R.id.nav_mod2) {
            Intent i = new Intent(this,LightWatch.class);
            startActivity(i);
        } else if (id == R.id.nav_mod3) {
            Intent i = new Intent(this,AmbianceAssistant.class);
            startActivity(i);
        } else if (id == R.id.nav_mod4) {
            Intent i = new Intent(this,MailBoxNotification.class);
            startActivity(i);
        }
        else if (id == R.id.nav_clear) {
            Intent i = new Intent(this,ClearTopic.class);
            startActivity(i);
        }
        else if (id == R.id.nav_charge) {
            Intent i = new Intent(this,SmartCharging.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
