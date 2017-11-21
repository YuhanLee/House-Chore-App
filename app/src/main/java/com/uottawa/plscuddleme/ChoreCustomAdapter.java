package com.uottawa.plscuddleme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Psymon on 2017-11-21.
 */



public class ChoreCustomAdapter extends ArrayAdapter {
    private final Context context;
    private final String[] myChores;
    public ChoreCustomAdapter(Context context, String[] choreList) {
        super(context, R.layout.chore_item_layout, choreList);
        this.context = context;
        this.myChores = choreList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.chore_item_layout, parent, false);
        TextView choreNameTextField = (TextView) rowView.findViewById(R.id.itemName);
        TextView choreDescriptionTextField = (TextView) rowView.findViewById(R.id.itemDescription);
        ImageView choreImage = (ImageView) rowView.findViewById(R.id.icon);
        choreNameTextField.setText(myChores[position]);
        choreDescriptionTextField.setText(myChores[position] + " is a chore!");
        return rowView;
    }
}
