package com.example.dam.uebung2;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
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
                if (CellInfoUtils.getRefreshRateInSec() != -1) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshNeighborCells();
                        }
                    });

                    Thread.sleep(CellInfoUtils.getRefreshRateInSec() * 1000);
                }
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

    public void refreshNeighborCells() {
        listItems.clear();

        // get all neighnoring cells
        neighboringCellInfos = telephonyManager.getNeighboringCellInfo();

        if (neighboringCellInfos.size() > 0) {
            for (NeighboringCellInfo neighboringCellInfo : neighboringCellInfos) {
                // cell id (-1 if unknown)
                int cid = neighboringCellInfo.getCid();
                // cell type (2 for EDGE, 3 for UMTS, 13 for LTE, 0 for unknown)
                int cellTypeNo = neighboringCellInfo.getNetworkType();
                String cellTypeName = CellInfoUtils.getCellTypeName(cellTypeNo);
                // location area code (-1 if unknown)
                int lac = neighboringCellInfo.getLac();
                // Receive Signal Strength Indicator (range -100..0, closer to 0 means better signal quality)
                int rssi = neighboringCellInfo.getRssi();
                // primary scrambling code
                int psc = neighboringCellInfo.getPsc();

                String neighborCellInfo = "Cell ID: " + (cid == NeighboringCellInfo.UNKNOWN_CID ? "N/A" : cid) + "\n" +
                        "Cell Type: " + (cellTypeNo == NeighboringCellInfo.UNKNOWN_CID ? "N/A" : cellTypeName + " (" + cellTypeNo + ")") + "\n" +
                        "Location Area Code: " + (lac == NeighboringCellInfo.UNKNOWN_CID ? "N/A" : lac) + "\n" +
                        "Primary Scrambling Code: " + (psc == NeighboringCellInfo.UNKNOWN_CID ? "N/A" : psc);

                Integer signalLevel = CellInfoUtils.getSignalLevelFromRSSI(rssi);

                Pair<String, Integer> pair = new Pair<String,Integer>(neighborCellInfo, rssi);
                listItems.add(pair);
            }
        }

        adapter.notifyDataSetChanged();
    }

}