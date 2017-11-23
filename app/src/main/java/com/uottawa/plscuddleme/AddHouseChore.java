package com.uottawa.plscuddleme;

/**
 * Created by Yuhan on 11/19/2017.
 */

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.*;


public class AddHouseChore extends AppCompatActivity {
    private DatabaseReference databaseHousechores;
    EditText editHousechoreName;
    EditText editChoreAssignedTo;
    EditText editChoredueDate;
    Spinner editChorePriority;
    Spinner editChoreCategory;
    Spinner editChoreRewards;

    Button buttonAddChore;
    ListView listViewHousechores;

    List<Housechore> housechores;


    //TODO: assignedBy and statusCompleted values have to be set as user and false at the beginning

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_housechore);

        databaseHousechores = FirebaseDatabase.getInstance().getReference("housechores");
        editHousechoreName = (EditText) findViewById(R.id.enter_chore_name);
        editChoreAssignedTo = (EditText) findViewById(R.id.enter_assignee);
        editChoredueDate = (EditText) findViewById(R.id.enter_dueDate);
        editChorePriority = (Spinner) findViewById(R.id.enter_priority);
        editChoreCategory = (Spinner) findViewById(R.id.enter_category);
        editChoreRewards = (Spinner) findViewById(R.id.enter_rewards);

        listViewHousechores = (ListView) findViewById(R.id.listViewHousechore);
        buttonAddChore = (Button) findViewById(R.id.addButton);

        housechores = new ArrayList<>();

        //adding an OnClickListener to button
        buttonAddChore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addHousechore();
            }
        });

        listViewHousechores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Housechore housechore = housechores.get(i);
                showUpdateDeleteDialog(housechore.getID(), housechore.getHousechoreName());
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseHousechores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                housechores.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Housechore housechore = postSnapshot.getValue(Housechore.class);
                    housechores.add(housechore);
                }
                HousechoreList housechoreAdapter = new HousechoreList(AddHouseChore.this, housechores);
                listViewHousechores.setAdapter(housechoreAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showUpdateDeleteDialog(final String housechoreId, String housechoreName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_housechore, null);
        dialogBuilder.setView(dialogView);
        //TODO ensure the following ids exist in the update_housechore.xml
        final EditText editChoreName = (EditText) dialogView.findViewById(R.id.enter_chore_name);
        final EditText editChoreAssignedTo = (EditText) dialogView.findViewById(R.id.enter_assignee);
        final EditText editChoreDueDate = (EditText) dialogView.findViewById(R.id.enter_dueDate);
        final EditText editChorePriority = (EditText) dialogView.findViewById(R.id.enter_priority);
        final EditText editChoreCategory = (EditText) dialogView.findViewById(R.id.enter_category);
        final EditText editChoreRewards = (EditText) dialogView.findViewById(R.id.enter_rewards);

        dialogBuilder.setTitle(housechoreName);
        final AlertDialog b = dialogBuilder.create();
        b.show();
        //this shows the toaster

    //        buttonUpdate.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                String name = editChoreName.getText().toString().trim();
    //                String assignee = editChoreAssignedTo.getText().toString().trim();
    //                String dueDate =editChoreDueDate.getText().toString().trim();
    //                //TODO fix the date object
    //                String priority = editChorePriority.getText().toString().trim();
    //                String category = editChoreCategory.getText().toString().trim();
    //                String reward = editChoreRewards.getText().toString();
    //                //TODO fix the reward to an int type object
    //
    //                //TODO may have to change the Date object ?
    //                //TODO look at android date objects and how the UI can be made nicely
    //
    //                if (!TextUtils.isEmpty(name)) {
    //                    updateHousechore(housechoreId, name, assignee, dueDate, priority,category, reward);
    //                }
    //
    //            }
    //        });

    }


    private void updateHousechore(String id, String housechoreName, String assignee, String dueDate, String priority, String category, String rewards) {

        //TODO fix the String type reward and string type DueDate into int and Date objects

    }
    private void addHousechore() {

    }

}
