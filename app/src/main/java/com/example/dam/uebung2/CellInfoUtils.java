package com.example.dam.uebung2;

/**
 * Created by Mat on 29.12.2015.
 */
public class CellInfoUtils {
    public static int ASUToDBM(int asu) {
        return 2*asu - 113;
    }

    public static int DBMToASU(int dbm) {
        return (dbm+113)/2;
    }

    public static int getSignalLevelFromRSSI(int rssiInDBM) {
        int signalLevel;
        if (rssiInDBM < -118)
            signalLevel = 0;
        else if (rssiInDBM < -103)
            signalLevel = 1;
        else if (rssiInDBM < -97)
            signalLevel = 2;
        else if (rssiInDBM < -85)
            signalLevel = 3;
            // good signal quality
        else
            signalLevel = 4;

        return signalLevel;
    }
}
