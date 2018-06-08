package com.uottawa.plscuddleme.activities;

/**
 * Created by Yuhan on 11/19/2017.
 */

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.firebase.database.*;
import com.uottawa.plscuddleme.models.Housechore;
import com.uottawa.plscuddleme.models.Member;
import com.uottawa.plscuddleme.R;


public class AddHouseChoreActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseHousechores;
    EditText editHousechoreName;
    Spinner editChoreAssignedTo;
    EditText editChoredueDate;
    Spinner editChorePriority;
    Spinner editChoreCategory;
    Spinner editChoreRewards;
    EditText editNote;
    ImageView imageChore;
    Calendar myCalendar;
    Button buttonAddChore;
    List<Housechore> housechores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uottawa.plscuddleme.R.layout.add_housechore);

        databaseHousechores = FirebaseDatabase.getInstance().getReference("housechores");
        editHousechoreName = (EditText) findViewById(com.uottawa.plscuddleme.R.id.enter_chore_name);
        editChoreAssignedTo = (Spinner) findViewById(com.uottawa.plscuddleme.R.id.spinnerAssignee);
        editChoredueDate = (EditText) findViewById(com.uottawa.plscuddleme.R.id.enter_dueDate);
        editChorePriority = (Spinner) findViewById(com.uottawa.plscuddleme.R.id.enter_priority);
        editChoreCategory = (Spinner) findViewById(com.uottawa.plscuddleme.R.id.enter_category);
        editChoreRewards = (Spinner) findViewById(com.uottawa.plscuddleme.R.id.enter_rewards);
        editNote = (EditText) findViewById(com.uottawa.plscuddleme.R.id.enter_note);
        imageChore = (ImageView) findViewById(com.uottawa.plscuddleme.R.id.imageViewChore);

        buttonAddChore = (Button) findViewById(com.uottawa.plscuddleme.R.id.addButton);
        housechores = new ArrayList<>();
        buttonAddChore.setOnClickListener(this);


        DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference().child("familyMembers");
        databaseMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> users = new ArrayList<String>();
                users.add("Unassigned");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Member member = snapshot.getValue(Member.class);
                    String userName = member.getfamilyMemberName();
                    users.add(userName);

                }

                Spinner userSpinner = (Spinner) findViewById(com.uottawa.plscuddleme.R.id.spinnerAssignee);
                ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(AddHouseChoreActivity.this, android.R.layout.simple_spinner_item, users);
                usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userSpinner.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        editChoredueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddHouseChoreActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

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
        DatabaseReference databaseChore;
        databaseChore = FirebaseDatabase.getInstance().getReference("housechores");
        String name = editHousechoreName.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "DisplayChoreActivity Name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(editChoredueDate.getText().toString())) {
            Toast.makeText(this, "DisplayChoreActivity has to have a due date", Toast.LENGTH_LONG).show();
            return;
        }

        if (!TextUtils.isEmpty(name)) {
            String id = databaseChore.push().getKey();
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

            Housechore housechore = new Housechore(id, stringHousechore, stringAssignedTo, "N/A", convertedDate.getTime(), stringPriority, stringChoreCategory, "Incomplete", intRewards, stringNote, false);

            databaseChore.child(id).setValue(housechore);

            editHousechoreName.setText("");
            editChoreAssignedTo.setSelection(0);
            editChoredueDate.setText("");
            editNote.setText("");
            editChoreCategory.setSelection(0);
            editChorePriority.setSelection(0);
            editChoreRewards.setSelection(0);
            Toast.makeText(this, "Housechore Added", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                addHousechore();
                break;
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editChoredueDate.setText(sdf.format(myCalendar.getTime()));
    }

}

