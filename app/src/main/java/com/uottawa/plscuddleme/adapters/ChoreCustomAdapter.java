package com.uottawa.plscuddleme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uottawa.plscuddleme.R;

/**
 * Created by Psymon on 2017-11-21.
 */


public class ChoreCustomAdapter extends ArrayAdapter {
    private final Context context;
    private final String[][] myChores;
    private String choreName;

    public ChoreCustomAdapter(Context context, String[][] choreList) {
        super(context, R.layout.chore_item_layout, choreList);
        this.context = context;
        this.myChores = choreList;
    }

    public void setChoreName(String choreName) {
        this.choreName = choreName;
    }
    // set values to contents of chore_item_layout and return
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.chore_item_layout, parent, false);
        TextView choreNameTextField = (TextView) rowView.findViewById(R.id.itemName);
        TextView choreDescriptionTextField = (TextView) rowView.findViewById(R.id.itemDescription);
        ImageView choreImage = (ImageView) rowView.findViewById(R.id.icon);

        if (myChores[position] != null && myChores[position].length > 0) {
            // DisplayChoreActivity name located in index 0 of inner array
            choreNameTextField.setText(myChores[position][0]);
            setChoreName(myChores[position][0]);
            // DisplayChoreActivity note located at index 1 of inner array
            choreDescriptionTextField.setText("Note: " + myChores[position][1]);
        }

        return rowView;
    }
}
