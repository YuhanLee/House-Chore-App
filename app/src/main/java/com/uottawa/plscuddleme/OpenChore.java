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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by Yuhan on 11/19/2017.
 */

public class OpenChore extends Fragment {
    private static final String TAG = "OpenChore";
    ListView listViewHousechores;
    ListView listView;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Chores");
        listViewHousechores = (ListView) getView().findViewById(R.id.housechore_list);

        displayAllChores();

        DatabaseReference databaseMembers;
        databaseMembers = FirebaseDatabase.getInstance().getReference().child("familyMembers");
        databaseMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> users = new ArrayList<String>();
                users.add("Display All");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Member member = snapshot.getValue(Member.class);
                    String userName = member.getfamilyMemberName();
                    users.add(userName);

                }

                Spinner userSpinner = (Spinner) getView().findViewById(R.id.user_filter);
                ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, users);
                usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userSpinner.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        listViewHousechores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final int selectedRow = i;
                DatabaseReference databaseProducts;
                databaseProducts = FirebaseDatabase.getInstance().getReference().child("housechores");
                databaseProducts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int counter = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Housechore housechore = snapshot.getValue(Housechore.class);
                            if (counter == selectedRow) {
                                showConfirmCompleteDialog(housechore.getID(), housechore.getReward(), housechore.getAssignedTo());
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

        Spinner userSpinner = (Spinner) getView().findViewById(R.id.user_filter);
        userSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final String selectedSpinner = parentView.getItemAtPosition(position).toString();
                DatabaseReference databaseProducts;
                databaseProducts = FirebaseDatabase.getInstance().getReference().child("housechores");
                databaseProducts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        int arraySize = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Housechore housechore = snapshot.getValue(Housechore.class);
                            if (housechore.getAssignedTo().equals(selectedSpinner)) {
                                arraySize = arraySize + 1;
                            }
                        }
                        String[][] choreList = new String[arraySize][];
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Housechore housechore = snapshot.getValue(Housechore.class);
                            String[] itemPair = new String[2];
                            if (housechore.getAssignedTo().equals(selectedSpinner)) {
                                itemPair[0] = housechore.getHousechoreName();
                                itemPair[1] = housechore.getNote();
                                choreList[i] = itemPair;
                                i = i + 1;
                            }
                        }
                        if (choreList != null && choreList.length > 0) {
                            ListView listView = (ListView) getView().findViewById(R.id.housechore_list);
                            ChoreCustomAdapter adapter = new ChoreCustomAdapter(getContext(), choreList);
                            listView.setAdapter(adapter);
                        } else if (selectedSpinner.equals("Display All")) {
                            displayAllChores();
                        } else {
                            Toast.makeText(getActivity(), "There are no chores assigned to " + selectedSpinner, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        listViewHousechores.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedRow = i;
                DatabaseReference databaseProducts;
                databaseProducts = FirebaseDatabase.getInstance().getReference().child("housechores");
                databaseProducts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int counter = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Housechore housechore = (Housechore) snapshot.getValue(Housechore.class);
                            if (counter == selectedRow) {
                                openChore(selectedRow, housechore.getID());
                                break;
                            }
                            counter = counter + 1;

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }


    private void openChore(int selectedRow, String id) {
        Intent intent = new Intent(getContext(), UpdateChoreActivity.class);
        String ROW_NUM = "ROW_CLICKED";
        String CHORE_ID = "CHORE_ID";
        Bundle extras = new Bundle();

        Log.v(TAG, "The ID of the openChore that was clicked on was = " + id);
        String position = String.valueOf(selectedRow);
        extras.putString(ROW_NUM, position);
        extras.putString(CHORE_ID, id);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_open_chores, container, false);
    }

    public void displayAllChores() {
        DatabaseReference databaseProducts;
        databaseProducts = FirebaseDatabase.getInstance().getReference().child("housechores");
        databaseProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                String[][] choreList = new String[(int) dataSnapshot.getChildrenCount()][];
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Housechore housechore = snapshot.getValue(Housechore.class);
                    String[] itemPair = new String[2];
                    itemPair[0] = housechore.getHousechoreName();
                    itemPair[1] = housechore.getNote();
                    choreList[i] = itemPair;
                    i = i + 1;

                }
                if (choreList != null && choreList.length > 0) {
                    ListView listView = (ListView) getView().findViewById(R.id.housechore_list);
                    ChoreCustomAdapter adapter = new ChoreCustomAdapter(getContext(), choreList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showConfirmCompleteDialog(final String id, final int reward, final String assignedTo) {
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
                DatabaseReference databaseProducts;
                databaseProducts = FirebaseDatabase.getInstance().getReference().child("familyMembers");
                databaseProducts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Member member = snapshot.getValue(Member.class);
                            if (member.getfamilyMemberName().equals(assignedTo)) {
                                int currentRewards = member.getRewards();
                                int newRewards = currentRewards + reward;
                                DatabaseReference dRewards = FirebaseDatabase.getInstance().getReference("familyMembers").child(snapshot.getKey()).child("rewards");
                                dRewards.setValue(newRewards);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                Toast.makeText(getActivity(), "Marked as completed. Rewarded " + reward + " points " + "to " + assignedTo, Toast.LENGTH_LONG).show();
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
