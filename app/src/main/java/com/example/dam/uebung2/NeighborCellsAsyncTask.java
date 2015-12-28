package com.example.dam.uebung2;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dam.uebung2.R;

import java.util.ArrayList;
import java.util.List;

public class NeighborCellsAsyncTask extends AsyncTask<String, Void, String> {

    TelephonyManager telephonyManager;
    List<NeighboringCellInfo> neighboringCellInfos;
    Activity activity;

    ArrayList<String> listItems;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;


    public NeighborCellsAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        // infinite LOOP
        while (true) {
            try {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateNeighbors();
                    }
                });

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
       // return "Executed";
    }

    @Override
    protected void onPreExecute() {
        telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        // set list adapter
        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter(activity,
                android.R.layout.simple_list_item_1, listItems);
        ListView lView = (ListView) activity.findViewById(R.id.neighboringCellsListView);
        lView.setAdapter(adapter);


    }

    private void updateNeighbors() {
        listItems.clear();

        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

        String networkOperator = telephonyManager.getNetworkOperator();
        String mcc = networkOperator.substring(0, 3);
        String mnc = networkOperator.substring(3);

        int cid = cellLocation.getCid();
        int lac = cellLocation.getLac();

        neighboringCellInfos = telephonyManager.getNeighboringCellInfo();
        System.out.println("Neighbors Size: " + neighboringCellInfos.size());
        for (NeighboringCellInfo neighboringCellInfo : neighboringCellInfos) {
            String temp = "ID is : " + neighboringCellInfo.getCid() + "\n" +
                    "Cell Type : " + neighboringCellInfo.getNetworkType() + "\n" + // 2 for umts 3 for something else
                    "Mobile Country Code :" + telephonyManager.getNetworkCountryIso() + "\n" +
                    "LAC : " + neighboringCellInfo.getLac() + "\n" +
                    "MCC: " + mcc + "\n" +
                    "MNC " + mnc;
            ;
            listItems.add(temp);
        }

        adapter.notifyDataSetChanged();
    }

}