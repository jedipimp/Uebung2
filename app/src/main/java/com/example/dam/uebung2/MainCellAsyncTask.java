package com.example.dam.uebung2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.AndroidCharacter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dam.uebung2.MainActivity;
import com.example.dam.uebung2.R;

import org.w3c.dom.Text;

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

    @TargetApi(value = 23)
    private class MyPhoneStateListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each tiome there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            TextView signalText = (TextView) activity.findViewById(R.id.mainCellSignalStrenghtText);
            int signalStrengthASU = signalStrength.getGsmSignalStrength();
            int signalStrengthdBm = CellInfoUtils.ASUToDBM(signalStrengthASU);

            signalText.setText(signalStrengthASU + " ASU\n" +
                    signalStrengthdBm + " dBm");

            ImageView image = (ImageView) activity.findViewById(R.id.mainCellSignalStrengthImage);
            int level = signalStrength.getLevel();

            switch (level) {
                // poor quality
                case 0:
                    image.setImageDrawable(activity.getResources().getDrawable(R.mipmap.ic_signal_cellular_0_bar_black_24dp));
                    break;
                case 1:
                    image.setImageDrawable(activity.getResources().getDrawable(R.mipmap.ic_signal_cellular_1_bar_black_24dp));
                    break;
                case 2:
                    image.setImageDrawable(activity.getResources().getDrawable(R.mipmap.ic_signal_cellular_2_bar_black_24dp));
                    break;
                case 3:
                    image.setImageDrawable(activity.getResources().getDrawable(R.mipmap.ic_signal_cellular_3_bar_black_24dp));
                    break;
                // excellent quality
                case 4:
                    image.setImageDrawable(activity.getResources().getDrawable(R.mipmap.ic_signal_cellular_4_bar_black_24dp));
                    break;
            }
        }

    }

    public void refreshMainCell() {
        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        // cell id
        int cid = cellLocation.getCid();
        // location area code
        int lac = cellLocation.getLac();

        String networkOperator = telephonyManager.getNetworkOperator();
        // mobile country code
        String mccNo = networkOperator.substring(0, 3);
        String mccName = telephonyManager.getNetworkCountryIso().toUpperCase();
        // mobile network code
        String mnc = networkOperator.substring(3);

        ConnectivityManager conMan = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        // cell type (EDGE, UMTS, LTE, ...
        int cellTypeNo = telephonyManager.getNetworkType();
        String cellTypeName = conMan.getActiveNetworkInfo().getSubtypeName();

        TextView mainCellText = (TextView) activity.findViewById(R.id.mainCellIdText);

        String displayText = "Cell ID: " + cid + "\n" +
                "Cell Type: " + cellTypeName + " (" + cellTypeNo + ")\n" +
                "Mobile Country Code: " + mccName + " (" + mccNo + ")\n" +
                "LAC: " + lac + "\n" +
                "MNC: " + mnc;

        mainCellText.setText(displayText);
    }

}