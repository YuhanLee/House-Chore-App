package com.uottawa.plscuddleme;

/**
 * Created by Yuhan on 11/19/2017.
 */

import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.firebase.database.*;

import android.util.Log;


public class AddHouseChore extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddHouseChore";
    private DatabaseReference databaseHousechores;
    EditText editHousechoreName;
    Spinner editChoreAssignedTo;
    EditText editChoredueDate;
    Spinner editChorePriority;
    Spinner editChoreCategory;
    Spinner editChoreRewards;
    EditText editNote;
    ImageView imageChore;

    Button buttonAddChore;

    List<Housechore> housechores;


    //TODO: assignedBy and statusCompleted values have to be set as user and false at the beginning

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_housechore);

        databaseHousechores = FirebaseDatabase.getInstance().getReference("housechores");
        editHousechoreName = (EditText) findViewById(R.id.enter_chore_name);
        editChoreAssignedTo = (Spinner) findViewById(R.id.enter_assignee);
        editChoredueDate = (EditText) findViewById(R.id.enter_dueDate);
        editChorePriority = (Spinner) findViewById(R.id.enter_priority);
        editChoreCategory = (Spinner) findViewById(R.id.enter_category);
        editChoreRewards = (Spinner) findViewById(R.id.enter_rewards);
        editNote = (EditText) findViewById(R.id.enter_note);
        imageChore = (ImageView) findViewById(R.id.imageViewChore);

        buttonAddChore = (Button) findViewById(R.id.addButton);

        housechores = new ArrayList<>();

        buttonAddChore.setOnClickListener(this);

        DatabaseReference databaseMembers;
        databaseMembers = FirebaseDatabase.getInstance().getReference().child("familyMembers");
        databaseMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> users = new ArrayList<String>();
                users.add("Unassigned");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Member member = snapshot.getValue(Member.class);
                    String userName = member.getfamilyMemberName();
                    Log.i(TAG, userName);
                    users.add(userName);

                }
                Spinner userSpinner = (Spinner)findViewById(R.id.enter_assignee);
                ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(AddHouseChore.this, android.R.layout.simple_spinner_item, users);
                usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userSpinner.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addHousechore() {
        Log.i(TAG, "addHousechore Called");
        DatabaseReference databaseProducts;
        databaseProducts = FirebaseDatabase.getInstance().getReference("housechores");
        String name = editHousechoreName.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            String id = databaseProducts.push().getKey();
            Log.i(TAG, id);
            String stringHousechore = editHousechoreName.getText().toString();
            String stringAssignedTo = editChoreAssignedTo.getSelectedItem().toString();
            String dateString = editChoredueDate.getText().toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String stringPriority = editChorePriority.getSelectedItem().toString();
            String stringChoreCategory = editChoreCategory.getSelectedItem().toString();
            String stringNote = editNote.getText().toString();
            int intRewards = Integer.parseInt(editChoreRewards.getSelectedItem().toString());
            Log.i(TAG, getDate(convertedDate.getTime(), "dd/MM/yyyy"));
            Housechore housechore = new Housechore(id, stringHousechore, stringAssignedTo, "N/A", convertedDate.getTime(), stringPriority, stringChoreCategory, "Incomplete", intRewards, stringNote);
            Log.i(TAG, housechore.getHousechoreName());
            Log.i(TAG, housechore.getNote());
            Log.i(TAG, housechore.getCategory());

            databaseProducts.child(id).setValue(housechore);
            editHousechoreName.setText("");
            editChoreAssignedTo.setSelection(0);
            editChoredueDate.setText("");
            editNote.setText("");
            editChoreCategory.setSelection(0);
            editChorePriority.setSelection(0);
            editChoreRewards.setSelection(0);
            Toast.makeText(this, "Housechore Added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enter a Housechore name", Toast.LENGTH_LONG).show();
        }
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                addHousechore();
                break;
        }
    }

}

