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

    List<NeighboringCellInfo> neighboringCellInfos;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new MainCellAsyncTask().execute("");
        //new NeighboringCellInfoAsyncTask().execute("");
        //List<NeighboringCellInfo> neighboringCellInfos = telephonyManager.getNeighboringCellInfo();

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
    public void onClick(View view)
    {

    }


    private class MainCellAsyncTask extends AsyncTask<String, Void, String> {
        TelephonyManager telephonyManager;
        MyPhoneStateListener    MyListener;
        @Override
        protected String doInBackground(String... params) {
            // infinite LOOP
            for (int i = 0; i < 0; i++) {
                try {
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();

            String networkOperator = telephonyManager.getNetworkOperator();
            String mcc = networkOperator.substring(0, 3);
            String mnc = networkOperator.substring(3);

            int cid = cellLocation.getCid();
            int lac = cellLocation.getLac();


            Button myButton = (Button)findViewById(R.id.mainCellButton);

            String displayText = "Main Cell ID is  "+ cid + "\n"+
            "Cell Type : "+ telephonyManager.getNetworkType()+"\n"+ // 2 for umts 3 for something else
                    "Mobile Country Code :" + telephonyManager.getNetworkCountryIso()+"\n"+
                    "LAC : " +lac+ "\n"+
                    "MCC: "+ mcc+ "\n"+
                    "MNC "+mnc;
            myButton.setText(displayText);


        }

        @Override
        protected void onPreExecute() {
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            MyListener   = new MyPhoneStateListener();
            telephonyManager       = ( TelephonyManager )getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
        private class MyPhoneStateListener extends PhoneStateListener
        {
            /* Get the Signal strength from the provider, each tiome there is an update */
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength)
            {
                super.onSignalStrengthsChanged(signalStrength);
                TextView temp = (TextView)findViewById(R.id.signalStrenghtTextView);
                temp.setText(String.valueOf(signalStrength.getGsmSignalStrength()));
            }

        };/* End of private Class */


        private class NeighborCellsAsyncTask extends AsyncTask<String, Void, String> {



            TelephonyManager telephonyManager;
            List<NeighboringCellInfo> neighboringCellInfos;


            ArrayList<String> listItems;

            //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
            ArrayAdapter<String> adapter;


            @Override
            protected String doInBackground(String... params) {
                // infinite LOOP
                for (int i = 0; i < 0; i++) {
                    try {
                        Thread.sleep(10000);

                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                }
                return "Executed";
            }

            @Override
            protected void onPostExecute(String result) {
                ArrayList<String> listItems=new ArrayList<String>();


                GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();

                String networkOperator = telephonyManager.getNetworkOperator();
                String mcc = networkOperator.substring(0, 3);
                String mnc = networkOperator.substring(3);

                int cid = cellLocation.getCid();
                int lac = cellLocation.getLac();


                for(NeighboringCellInfo neighboringCellInfo : neighboringCellInfos)
                {
                    String temp = "ID is : "+ neighboringCellInfo.getCid()+"\n"+
                            "Cell Type : "+ telephonyManager.getNetworkType()+"\n"+ // 2 for umts 3 for something else
                            "Mobile Country Code :" + telephonyManager.getNetworkCountryIso()+"\n"+
                            "LAC : " +lac+ "\n"+
                            "MCC: "+ mcc+ "\n"+
                            "MNC "+mnc; ;
                    listItems.add(temp);

                }

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_1, listItems);


                ListView lView = (ListView) findViewById(R.id.neighboringCellsListView);
                lView.setAdapter(adapter);


            }

            @Override
            protected void onPreExecute() {
                telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                neighboringCellInfos = telephonyManager.getNeighboringCellInfo();


            }

        }
    }
}
