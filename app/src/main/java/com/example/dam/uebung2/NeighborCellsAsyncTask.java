package com.example.dam.uebung2;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.Pair;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NeighborCellsAsyncTask extends AsyncTask<String, Void, String> {

    TelephonyManager telephonyManager;
    Activity activity;

    ArrayList<Pair<String,CellSignalStrength>> listItems;

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
        listItems = new ArrayList<Pair<String,CellSignalStrength>>();
        adapter = new CellInfoImageStrengthAdapter(activity, R.layout.list_neighbors_row, listItems);
        ListView lView = (ListView) activity.findViewById(R.id.neighboringCellsListView);
        lView.setAdapter(adapter);


    }

    public void refreshNeighborCells() {
        listItems.clear();

        // get all neighnoring cells
        List<CellInfo> allCellInfos = telephonyManager.getAllCellInfo();

        if (allCellInfos.size() > 0) {
            for (CellInfo ci : allCellInfos) {
                // skip active cell, use only neighbor cells
                if (ci.isRegistered())
                    continue;

                CellSignalStrength signalStrength = null;
                String cellType = "N/A";
                int cid = -1, lac = -1,psc = -1,mcc = -1,mnc = -1,pci = -1,tac = -1;

                // UMTS (= WCDMA)
                if (ci instanceof CellInfoWcdma) {
                    CellInfoWcdma ciWcdma = (CellInfoWcdma) ci;
                    CellIdentityWcdma cellIdentityWcdma = ciWcdma.getCellIdentity();

                    cid = cellIdentityWcdma.getCid();
                    mnc = cellIdentityWcdma.getMnc();
                    mcc = cellIdentityWcdma.getMcc();
                    lac = cellIdentityWcdma.getLac();
                    psc = cellIdentityWcdma.getPsc();
                    cellType= "UMTS";
                    signalStrength = ciWcdma.getCellSignalStrength();

                // LTE
                } else if (ci instanceof CellInfoLte) {
                    CellInfoLte ciLte = (CellInfoLte) ci;
                    CellIdentityLte cellIdentityLte = ciLte.getCellIdentity();

                    cid = cellIdentityLte.getCi();
                    pci = cellIdentityLte.getPci(); // Physical Cell Id
                    mnc = cellIdentityLte.getMnc();
                    mcc = cellIdentityLte.getMcc();
                    tac = cellIdentityLte.getTac(); // Tracking Area Code
                    cellType = "LTE"; // LTE
                    signalStrength = ciLte.getCellSignalStrength();

                // GSM
                } else if (ci instanceof CellInfoGsm) {
                    CellInfoGsm ciGsm = (CellInfoGsm) ci;

                    CellIdentityGsm cellIdentityGsm = ciGsm.getCellIdentity();

                    cid = cellIdentityGsm.getCid();
                    mnc = cellIdentityGsm.getMnc();
                    mcc = cellIdentityGsm.getMcc();
                    lac = cellIdentityGsm.getLac();
                    psc = -1; // undefined for GSM
                    cellType = "GSM"; // GSM
                    signalStrength = ciGsm.getCellSignalStrength();
                } else if (ci instanceof CellInfoCdma) {
                    // not relevant
                }

                String neighboringCellText = "Cell ID: " + (cid == Integer.MAX_VALUE ? "N/A" : cid) + "\n" +
                        "Cell Type: " + cellType + "\n" +
                        "Mobile Country Code: " + (mcc == Integer.MAX_VALUE ? "N/A" : mcc) + "\n" +
                        "Mobile Network Operator: " + (mnc == Integer.MAX_VALUE ? "N/A" : mnc) + "\n";

                if (cellType.equals("LTE"))
                    neighboringCellText += "Physical Cell ID: " + (pci == Integer.MAX_VALUE ? "N/A" : pci) + "\n"+
                            "Tracking Area Code: " + (tac == Integer.MAX_VALUE ? "N/A" : tac);
                else
                    neighboringCellText += "Location Area Code: " + (lac == Integer.MAX_VALUE ? "N/A" : lac) + "\n" +
                        "Primary Scrambling Code: " + (psc == Integer.MAX_VALUE ? "N/A" : psc);

                Pair<String, CellSignalStrength> pair = new Pair<String, CellSignalStrength>(neighboringCellText, signalStrength);
                listItems.add(pair);
            }
        }

        adapter.notifyDataSetChanged();
    }

}