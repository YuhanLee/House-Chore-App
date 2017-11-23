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

public class ShoppingListCustomAdapter extends ArrayAdapter {
    private final Context context;
    private final String[] myList;
    public ShoppingListCustomAdapter(Context context, String[] shoppingList) {
        super(context, R.layout.shopping_item_layout, shoppingList);
        this.context = context;
        this.myList = shoppingList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.shopping_item_layout, parent, false);
        TextView itemNameTextField = (TextView) rowView.findViewById(R.id.checkBox);
        ImageView choreImage = (ImageView) rowView.findViewById(R.id.icon);
        itemNameTextField.setText(myList[position]);
        return rowView;
    }
}
