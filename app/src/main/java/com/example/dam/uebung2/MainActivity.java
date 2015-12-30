package com.example.dam.uebung2;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    MainCellAsyncTask mainCellTask;
    NeighborCellsAsyncTask neighborCellsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Swipe Refresh
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainCellTask.refreshMainCell();
                neighborCellsTask.refreshNeighborCells();
                // disable refreshing animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // don't refresh when list is down
        final ListView listView = (ListView) findViewById(R.id.neighboringCellsListView);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                // only enable swipe refresh when list is at top
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        mainCellTask = new MainCellAsyncTask(this);
        mainCellTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

        neighborCellsTask = new NeighborCellsAsyncTask(this);
        neighborCellsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        List<String> items = new ArrayList<String>();
        items.add("1 sec"); items.add("5 sec"); items.add("10 sec"); items.add("30 sec"); items.add("1 min"); items.add("off");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,items);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinner.setAdapter(adapter); // set the adapter to provide layout of rows and content
        spinner.setPopupBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapter.getDropDownView(position,view,parent)).setBackgroundColor(Color.WHITE);
                String selectedItem = (String) parent.getAdapter().getItem(position);
                int seconds = -1;
                switch (selectedItem) {
                    case "1 sec":
                        seconds = 1;
                        break;
                    case "5 sec":
                        seconds = 5;
                        break;
                    case "10 sec":
                        seconds = 10;
                        break;
                    case "30 sec":
                        seconds = 30;
                        break;
                    case "1 min":
                        seconds = 60;
                        break;
                    case "off":
                        seconds = -1;
                        break;
                }

                CellInfoUtils.setRefreshRateInSec(seconds);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); // set the listener, to perform actions based on item selection

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            mainCellTask.refreshMainCell();
            neighborCellsTask.refreshNeighborCells();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }
}
