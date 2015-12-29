package com.example.dam.uebung2;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Pair;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NeighborCellsAsyncTask extends AsyncTask<String, Void, String> {

    TelephonyManager telephonyManager;
    List<NeighboringCellInfo> neighboringCellInfos;
    Activity activity;

    ArrayList<Pair<String,Integer>> listItems;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    CellInfoImageStrengthAdapter adapter;


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
        listItems = new ArrayList<Pair<String,Integer>>();
        adapter = new CellInfoImageStrengthAdapter(activity, R.layout.list_neighbors_row, listItems);
        ListView lView = (ListView) activity.findViewById(R.id.neighboringCellsListView);
        lView.setAdapter(adapter);


    }

    private void updateNeighbors() {
        listItems.clear();

        // get all neighnoring cells
        neighboringCellInfos = telephonyManager.getNeighboringCellInfo();

        if (neighboringCellInfos.size() > 0) {
            for (NeighboringCellInfo neighboringCellInfo : neighboringCellInfos) {
                // cell id (-1 if unknown)
                int cid = neighboringCellInfo.getCid();
                // cell type (2 for EDGE, 3 for UMTS, 13 for LTE, 0 for unknown)
                int cellTypeInt = neighboringCellInfo.getNetworkType();
                // location area code (-1 if unknown)
                int lac = neighboringCellInfo.getLac();
                // Receive Signal Strength Indicator (range -100..0, closer to 0 means better signal quality)
                int rssi = neighboringCellInfo.getRssi();

                String neighborCellInfo = "Cell ID: " + (cid == NeighboringCellInfo.UNKNOWN_CID ? "N/A" : cid) + "\n" +
                        "Cell Type: " + (cellTypeInt == NeighboringCellInfo.UNKNOWN_CID ? "N/A" : cellTypeInt) + "\n" +
                        "LAC: " + (lac == NeighboringCellInfo.UNKNOWN_CID ? "N/A" : lac) + "\n" +
                        "RSSI: " + (rssi == NeighboringCellInfo.UNKNOWN_RSSI ? "N/A" : rssi + " dBm");
                ;
                Integer signalLevel = 0;

                // poor signal quality
                if (rssi < -118)
                    signalLevel = 0;
                else if (rssi < -103)
                    signalLevel = 1;
                else if (rssi < -97)
                    signalLevel = 2;
                else if (rssi < -85)
                    signalLevel = 3;
                // good signal quality
                else
                    signalLevel = 4;

                Pair<String, Integer> pair = new Pair<String,Integer>(neighborCellInfo, signalLevel);
                listItems.add(pair);
            }
        }

        adapter.notifyDataSetChanged();
    }

}