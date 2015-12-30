package com.example.dam.uebung2;

import android.telephony.TelephonyManager;

public class CellInfoUtils {

    // refresh rate in seconds, -1 for turning off
    private static int refreshRateInSec = 5;

    public static int getRefreshRateInSec() {
        return refreshRateInSec;
    }

    public static void setRefreshRateInSec(int refreshRateInSec) {
        CellInfoUtils.refreshRateInSec = refreshRateInSec;
    }

    public static int ASUToRSSI(int asu, int cellType) {
        switch (cellType) {
            case TelephonyManager.NETWORK_TYPE_LTE:
                return asu - 120;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return 2 * asu - 113;
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return 2 * asu - 113;
        }
        return -1;
    }

    public static int DBMToASU(int dbm) {
        return (dbm + 113) / 2;
    }

    public static int getSignalLevelFromRSSI(int rssi) {
        int signalLevel;
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

        return signalLevel;
    }

    public static String getCellTypeName(int cellType) {
        String str;
        switch (cellType) {
            case TelephonyManager.NETWORK_TYPE_EDGE:
                str = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                str = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                str = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                str = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                str = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                str = "HSPA+";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                str = "HSDPA";
                break;
            default:
                str = "N/A";
                break;
        }

        return str;
    }

}