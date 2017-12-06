package com.uottawa.plscuddleme;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 * This method is responsible for listing all the chores and its notes
 * setting onclick item for update chore
 *
 */

public class Chore extends Fragment {
    ListView listViewHousechores;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    ChoreCustomAdapter adapter;
    String userId;
    String currentUserName;

    @Override
    // When returned to this activity from another activity's finish(), refresh list of chores
    public void onResume() {
        super.onResume();
        displayAllChores();
    }


    /**
     * Method creates the view and displays all the chores in the db
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("All Chores");
        Switch onOffSwitch = (Switch) getView().findViewById(R.id.switch1);
        listViewHousechores = (ListView) getView().findViewById(R.id.housechore_list);

        // Display all chores on page load
        displayAllChores();

        //get User context
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), SignInActivity.class));
        } else {
            firebaseUser = firebaseAuth.getCurrentUser();
            userId = firebaseUser.getUid();
        }

        //This gets the current user name
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
                    if (member.getID().equals(userId)) {
                        currentUserName = member.getfamilyMemberName();
                    }
                    users.add(userName);

                }
                //Populates the spinner for filtering in terms of user with Family Members
                Spinner userSpinner = (Spinner) getView().findViewById(R.id.user_filter);
                ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, users);
                usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userSpinner.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Implements on long click to display a dialog that allows user to change status of chore
        listViewHousechores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final int selectedRow = i;
                DatabaseReference databaseChores;
                databaseChores = FirebaseDatabase.getInstance().getReference().child("housechores");
                databaseChores.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int counter = 0;
                        // find corresponding database entry based of row selected
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Housechore housechore = snapshot.getValue(Housechore.class);
                            if (counter == selectedRow) {
                                // database entry found, call function that displays dialog
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

        // Add on click listener to spinner
        Spinner userSpinner = (Spinner) getView().findViewById(R.id.user_filter);
        userSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get selected value in spinner of users
                final String selectedSpinner = parentView.getItemAtPosition(position).toString();
                DatabaseReference databaseChores;
                databaseChores = FirebaseDatabase.getInstance().getReference().child("housechores");
                databaseChores.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        int arraySize = 0;
                        // Get amount of database entries of housechores that is assigned to the selected user and create an array of its size
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Housechore housechore = snapshot.getValue(Housechore.class);
                            if (housechore.getAssignedTo().equals(selectedSpinner)) {
                                arraySize = arraySize + 1;
                            }
                        }
                        String[][] choreList = new String[arraySize][];
                        // Create an array of arrays that includes the housechore name and the housechore note
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
                        // If the list is not null or empty, create custom adapter
                        if (choreList != null && choreList.length > 0) {
                            ListView listView = (ListView) getView().findViewById(R.id.housechore_list);
                            ChoreCustomAdapter adapter = new ChoreCustomAdapter(getContext(), choreList);
                            listView.setAdapter(adapter);
                        } else if (selectedSpinner.equals("Display All")) {
                            // Display all chores if selected value is display all
                            displayAllChores();
                        } else {
                            // otherwise, toast that the user selected has no chores
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

        // Add on click listener to row items that will redirect to a chore profile page
        listViewHousechores.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedRow = i;
                DatabaseReference databaseChores;
                databaseChores = FirebaseDatabase.getInstance().getReference().child("housechores");
                databaseChores.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int counter = 0;
                        // find chore in database corresponding to row selected
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Housechore housechore = (Housechore) snapshot.getValue(Housechore.class);
                            if (counter == selectedRow) {
                                // chore found in database, call function that opens the chore profile page
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

        //If the user select the switch (Show assigned to me) then the userSpinner will call setSelection
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Spinner userSpinner = (Spinner) getView().findViewById(R.id.user_filter);
                if (isChecked) {
                    userSpinner.setSelection(getIndex(userSpinner, currentUserName));
                } else {
                    userSpinner.setSelection(0);
                }
            }
        });

    }


    /**
     * Function passes the selected row to Update chore.xml
     * @param selectedRow the clicked row
     * @param id id of the chore
     */
    private void openChore(int selectedRow, String id) {
        Intent intent = new Intent(getContext(), UpdateChoreActivity.class);
        String ROW_NUM = "ROW_CLICKED";
        String CHORE_ID = "CHORE_ID";
        Bundle extras = new Bundle();

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

    /**
     * Displays all the chrores in firebase. Loops through all the housechore entry.
     * This function behaves very similarly as the block of code in line 164
     */
    public void displayAllChores() {
        DatabaseReference databaseChores;
        databaseChores = FirebaseDatabase.getInstance().getReference().child("housechores");
        databaseChores.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    adapter = new ChoreCustomAdapter(getContext(), choreList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * This function shows a confirmComplete dialog at a long item click to ask if the user
     * wants to change the status of the chore
     * @param id id of the chore
     * @param reward reward points of the chore
     * @param assignedTo Assignee of the chore
     */
    private void showConfirmCompleteDialog(final String id, final int reward, final String assignedTo) {
        // Build and show dialog
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

        // Close dialog on cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        // When confirm is clicked
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set value in database to completed
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id).child("completedStatus");
                dR.setValue("Completed");
                DatabaseReference databaseChores;
                databaseChores = FirebaseDatabase.getInstance().getReference().child("familyMembers");
                databaseChores.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Find family member the chore was assigned to
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Member member = snapshot.getValue(Member.class);
                            if (member.getfamilyMemberName().equals(assignedTo)) {
                                // Family member found, update member rewards points in database
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
                // On click postpone, update database value to postponed.
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id).child("completedStatus");
                dR.setValue("Postponed");
                Toast.makeText(getActivity(), "Marked as Postponed", Toast.LENGTH_LONG).show();
                b.dismiss();
            }
        });
    }

    /*
     * Get index of a value in a spinner
     */
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
