package com.example.dam.uebung2;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Button;
import android.widget.TextView;

import com.example.dam.uebung2.MainActivity;
import com.example.dam.uebung2.R;

public class MainCellAsyncTask extends AsyncTask<String, Void, String> {
    TelephonyManager telephonyManager;
    MyPhoneStateListener MyListener;
    Activity activity;

    public MainCellAsyncTask(Activity activity) {
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
                        refreshMainCell();
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
        MyListener = new MyPhoneStateListener();
        telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each tiome there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            TextView temp = (TextView) activity.findViewById(R.id.signalStrenghtTextView);
            temp.setText(String.valueOf(signalStrength.getGsmSignalStrength()));
        }

    }



    private void refreshMainCell() {
        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

        String networkOperator = telephonyManager.getNetworkOperator();
        String mcc = networkOperator.substring(0, 3);
        String mnc = networkOperator.substring(3);

        int cid = cellLocation.getCid();
        int lac = cellLocation.getLac();


        Button myButton = (Button) activity.findViewById(R.id.mainCellButton);

        String displayText = "Main Cell ID is  " + cid + "\n" +
                "Cell Type : " + telephonyManager.getNetworkType() + "\n" + // 2 for umts 3 for something else
                "Mobile Country Code :" + telephonyManager.getNetworkCountryIso() + "\n" +
                "LAC : " + lac + "\n" +
                "MCC: " + mcc + "\n" +
                "MNC " + mnc;

        System.out.println("updated main cell!");
        myButton.setText(displayText);
    }

}