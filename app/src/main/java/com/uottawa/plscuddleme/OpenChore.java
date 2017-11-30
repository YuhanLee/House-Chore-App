package com.uottawa.plscuddleme;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuhan on 11/19/2017.
 */

public class OpenChore extends Fragment {
    private static final String TAG = "OpenChore";
    ListView listViewHousechores;
    private long numberOfChores;
    RelativeLayout tv;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Chores");
        tv = (RelativeLayout)getView().findViewById(R.id.filter_container);
        listViewHousechores = (ListView)getView().findViewById(R.id.housechore_list);
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

        DatabaseReference databaseProducts;
        databaseProducts = FirebaseDatabase.getInstance().getReference().child("housechores");
        databaseProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                String[][] choreList = new String[(int)dataSnapshot.getChildrenCount()][];
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Housechore housechore = snapshot.getValue(Housechore.class);
                    String[] itemPair = new String[2];
                    itemPair[0] = housechore.getHousechoreName();
                    itemPair[1] = housechore.getNote();
                    choreList[i] = itemPair;
                    i = i + 1;

                }
                ListView listView = (ListView)getView().findViewById(R.id.housechore_list);
                ChoreCustomAdapter adapter = new ChoreCustomAdapter(getContext(), choreList);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        listViewHousechores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedRow = i;
                DatabaseReference databaseProducts;
                databaseProducts = FirebaseDatabase.getInstance().getReference().child("housechores");
                databaseProducts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int counter = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Housechore housechore = snapshot.getValue(Housechore.class);
                            Log.i(TAG, String.valueOf(selectedRow));
                            Log.i(TAG, String.valueOf(counter));
                            if (counter == selectedRow) {
                                showConfirmCompleteDialog(housechore.getID());
                            }
                            counter = counter + 1;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                return true;
            }
        });

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

    private void showConfirmCompleteDialog(final String id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mark_completed_confirm, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Mark as Completed?");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.cancelButton);
        final Button buttonConfirm = (Button) dialogView.findViewById(R.id.confirmButton);
        final Button buttonPostpone = (Button) dialogView.findViewById(R.id.postponeButton);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id).child("completedStatus");
                dR.setValue("Completed");
                Toast.makeText(getActivity(), "Marked as Complete", Toast.LENGTH_LONG).show();
                b.dismiss();
            }
        });

        buttonPostpone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id).child("completedStatus");
                dR.setValue("Postponed");
                Toast.makeText(getActivity(), "Marked as Postponed", Toast.LENGTH_LONG).show();
                b.dismiss();
            }
        });
    }
}
