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

        TextView textView = (TextView) v.findViewById(R.id.neighboringInfoText);
        ImageView image = (ImageView) v.findViewById(R.id.signalStrengthNeighborImage);
        textView.setText(getItem(position).first);

        // Change icon based on signalLevel
        Integer signalLevel = getItem(position).second;

        switch (signalLevel) {
            // poor quality
            case 0:
                image.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_0_bar_black_24dp));
                break;
            case 1:
                image.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_1_bar_black_24dp));
                break;
            case 2:
                image.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_2_bar_black_24dp));
                break;
            case 3:
                image.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_3_bar_black_24dp));
                break;
            // excellent quality
            case 4:
                image.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_signal_cellular_4_bar_black_24dp));
                break;
        }

        return v;
    }
}
