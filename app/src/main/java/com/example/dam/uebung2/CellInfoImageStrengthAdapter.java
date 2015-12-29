package com.example.dam.uebung2;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mat on 29.12.2015.
 */
public class CellInfoImageStrengthAdapter extends ArrayAdapter<Pair<String,Integer>> {

    public CellInfoImageStrengthAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    public CellInfoImageStrengthAdapter(Context context, int resource, List<Pair<String,Integer>> values) {
        super(context, resource, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_neighbors_row, null);
        }

        TextView neighborCellInfoText = (TextView) v.findViewById(R.id.neighboringInfoText);
        ImageView signalStrenghtImage = (ImageView) v.findViewById(R.id.neighboringSignalImage);
        TextView signalStrenghtText = (TextView) v.findViewById(R.id.neighboringSignalStrengthText);

        Integer signalStrengthdBm = getItem(position).second;

        neighborCellInfoText.setText(getItem(position).first);
        signalStrenghtText.setText(CellInfoUtils.DBMToASU(signalStrengthdBm) + " ASU\n" +
                signalStrengthdBm + " dBm");

        // Change icon based on signalLevel
        Integer signalLevel = CellInfoUtils.getSignalLevelFromRSSI(signalStrengthdBm);

        switch (signalLevel) {
            // poor quality
            case 0:
                signalStrenghtImage.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_0_bar_black_24dp));
                break;
            case 1:
                signalStrenghtImage.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_1_bar_black_24dp));
                break;
            case 2:
                signalStrenghtImage.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_2_bar_black_24dp));
                break;
            case 3:
                signalStrenghtImage.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_3_bar_black_24dp));
                break;
            // excellent quality
            case 4:
                signalStrenghtImage.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_4_bar_black_24dp));
                break;
        }

        return v;
    }
}
