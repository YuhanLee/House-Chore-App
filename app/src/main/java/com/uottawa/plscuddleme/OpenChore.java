package com.uottawa.plscuddleme;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuhan on 11/19/2017.
 */

public class OpenChore extends Fragment {
    RelativeLayout tv;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Chores");

        tv = (RelativeLayout)getView().findViewById(R.id.filter_container);
        setLayoutInvisible();
        Button filterButton = (Button)getView().findViewById(R.id.filterChore);
        filterButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                setLayoutVisible();
            }
        });

        Button closeFilter = (Button)getView().findViewById(R.id.hideFilter);
        closeFilter.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                setLayoutInvisible();
            }
        });

        // TODO Populate list with database items @Psymon
        String[] choreList = {"Walk Dog", "Do the Dishes", "Clean Room", "Make Bed", "Take Trash Out"};
        ListView listView = (ListView)getView().findViewById(R.id.housechore_list);
        ChoreCustomAdapter adapter = new ChoreCustomAdapter(getContext(), choreList);
        listView.setAdapter(adapter);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_open_chores, container, false);
    }

    public void setLayoutInvisible() {
        if (tv.getVisibility() == View.VISIBLE) {
            tv.setVisibility(View.GONE);
        }
    }
    public void setLayoutVisible() {
        if (tv.getVisibility() == View.GONE) {
            tv.setVisibility(View.VISIBLE);
        }
    }
}
