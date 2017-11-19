package com.uottawa.plscuddleme;

/**
 * Created by Yuhan on 2017-11-14.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class ItemList extends ArrayAdapter<ItemObj> {
    private Activity context;
    List<ItemObj> items;

    public ItemList(Activity context, List<ItemObj> items) {
        super(context, R.layout.activity_item_list, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_item_list, null, true);

//        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
//        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.textViewPrice);
//
//        Product product = products.get(position);
//        textViewName.setText(product.getProductName());
//        textViewPrice.setText(String.valueOf(product.getPrice()));
        return listViewItem;
    }
}

