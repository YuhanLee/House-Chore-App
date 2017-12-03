package com.uottawa.plscuddleme;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Yuhan on 11/19/2017.
 */

public class Resources extends Fragment implements View.OnClickListener{

    private static final String TAG = "Resources";
    ListView listViewResources;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    Button buttonAddNewResource;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Resources for Chores");
        buttonAddNewResource = (Button) getView().findViewById(R.id.buttonAddResource);
        String[] shoppingList = {"Pencil", "Eraser", "Rocket League", "Rocket League Controller", "Dog Food", "Dog"};
        ListView listViewResources = (ListView) getView().findViewById(R.id.list_shopping);


        ResourceCustomAdapter adapter = new ResourceCustomAdapter(getContext(), shoppingList);
        listViewResources.setAdapter(adapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_resources, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonAddResource:
                buttonSave();
                break;
        }

    }
}
