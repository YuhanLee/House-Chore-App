package com.uottawa.plscuddleme;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Yuhan on 11/19/2017.
 */

public class ShoppingList extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Shopping List Items");

        String[] shoppingList = {"Pencil", "Eraser", "Rocket League", "Rocket League Controller", "Dog Food", "Dog"};
        ListView listView = (ListView)getView().findViewById(R.id.list_shopping);
        ShoppingListCustomAdapter adapter = new ShoppingListCustomAdapter(getContext(), shoppingList);
        listView.setAdapter(adapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_shopping_list, container, false);
    }
}
