package com.example.dam.uebung2;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.List;
import 	android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        System.out.println("Start main cell task!");
        new MainCellAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
        System.out.println("Start neighbor cells task!");
        new NeighborCellsAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }
}
