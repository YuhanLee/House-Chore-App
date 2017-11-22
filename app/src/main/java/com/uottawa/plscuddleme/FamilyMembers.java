package com.uottawa.plscuddleme;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Yuhan on 11/19/2017.
 */

public class FamilyMembers extends Fragment {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Family Members");

        String[] famList = {"Ponyo", "Piggy", "Poppy", "Simon", "Janaki"};
        ListView famListView = (ListView) getView().findViewById(R.id.fam_listView);
        FamilyMemberAdapter famAdapter = new FamilyMemberAdapter(getContext(),famList);
        famListView.setAdapter(famAdapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_people, container, false);
    }
}
