package com.uottawa.plscuddleme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Psymon on 2017-11-23.
 */

public class ResourceCustomAdapter extends ArrayAdapter {
    private static final String TAG = "ResourceCustomAdapter";
    private final Context context;
    private final String[] myResource;

    public ResourceCustomAdapter(Context context, String[] shoppingList) {
        super(context, R.layout.resource_item_layout, shoppingList);
        this.context = context;
        this.myResource = shoppingList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // set values to contents of resource_item_layout and return
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.resource_item_layout, parent, false);
        TextView textViewResourceName = (TextView) rowView.findViewById(R.id.textViewResourceName);
        TextView textViewChoreBelonging = (TextView) rowView.findViewById(R.id.textViewChoreBelonging);
        ImageView imageViewResource = (ImageView) rowView.findViewById(R.id.imageViewResource);
        textViewResourceName.setText(myResource[position]);
        return rowView;
    }
}
