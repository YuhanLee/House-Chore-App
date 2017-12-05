package com.uottawa.plscuddleme;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateChoreActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "UpdateChoreActivity";
    private int row;
    private String rowClick;
    private String choreID;

    //Firebase + User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String userRole;
    String userId;
    String userName;

    //Housechore
    String housechoreName;
    String assignedBy;
    String assignedTo;
    String priority;
    String category;
    String note;
    String statusCompleted;
    long dueDate;
    int reward;

    //View items
    TextView textViewChoreName;
    TextView textViewAssignee;
    TextView textViewCategory;
    TextView textViewDueDate;
    TextView textViewStatus;
    TextView textViewPriority;
    TextView textViewReward;
    TextView textViewNotes;

    EditText editChoredueDate;
    ListView listViewResources;
    Calendar myCalendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get User context
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            firebaseUser = firebaseAuth.getCurrentUser();
            userId = firebaseUser.getUid();
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            choreID = extras.getString("CHORE_ID");
            Log.v(TAG, "choreID = " + choreID);
        }

        //gets all instances of the chores based on the ID
        DatabaseReference databaseHousechores;
        databaseHousechores = FirebaseDatabase.getInstance().getReference().child("housechores");
        databaseHousechores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id;
                for (DataSnapshot chores : dataSnapshot.getChildren()) {
                    Housechore chore = chores.getValue(Housechore.class);
                    id = chores.getKey();
                    if (id.equals(choreID)) {
                        getChore(chore);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseMembers;
        databaseMembers = FirebaseDatabase.getInstance().getReference().child("familyMembers");
        databaseMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id;
                for (DataSnapshot familyMembers : dataSnapshot.getChildren()) {
                    Member member = familyMembers.getValue(Member.class);
                    id = member.getID();
                    if (userId.equals(id)) {
                        getMember(member);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        displayAllResources();


    }


    private void getChore(Housechore chore) {

        //assignedBy = chore.getAssignedBy();
        housechoreName = chore.getHousechoreName();
        assignedTo = chore.getAssignedTo();
        dueDate = chore.getDueDate();
        priority = chore.getPriority();
        category = chore.getCategory();
        statusCompleted = chore.getCompletedStatus();
        reward = chore.getReward();
        note = chore.getNote();
        String debug = chore.toString();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }

    }

    private void getMember(Member member) {
        userName = member.getfamilyMemberName();
        userRole = member.getUserRole();
        setView();
    }

    private void setView() {
        if (userRole.equals("Adult")) {
            setContentView(R.layout.adult_update_housechore);
            Button buttonAssignTo = (Button) findViewById(R.id.buttonAssignTo);
            Button buttonEdit = (Button) findViewById(R.id.buttonEdit);
            Button buttonDelete = (Button) findViewById(R.id.buttonDelete);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAdultConfirmDeleteDialog();
                }
            });
            buttonAssignTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAdultConfirmAssignToDialog();
                }
            });
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAdultConfirmUpdateDialog();
                }
            });
        } else {
            setContentView(R.layout.child_update_housechore);

            if (!(assignedTo.equals("Unassigned"))) {
                Log.v(TAG, "assignedTo = " + assignedTo);
                LinearLayout buttonLinearLayout = (LinearLayout) findViewById(R.id.layoutAssignToMe);
                buttonLinearLayout.setVisibility(View.GONE);
            } else {
                Button buttonAssign = (Button) findViewById(R.id.buttonAssignToMe);
                buttonAssign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChildConfirmDialog(choreID, userName);
                    }
                });
            }
        }

        textViewChoreName = (TextView) findViewById(R.id.textViewChoreName);
        textViewAssignee = (TextView) findViewById(R.id.textViewAssignee);
        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        textViewDueDate = (TextView) findViewById(R.id.textViewDueDate);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewPriority = (TextView) findViewById(R.id.textViewPriority);
        textViewReward = (TextView) findViewById(R.id.textViewReward);
        textViewNotes = (TextView) findViewById(R.id.textViewNotes);

        textViewChoreName.setText(housechoreName);
        textViewAssignee.setText(assignedTo);
        textViewCategory.setText(category);
        textViewDueDate.setText(getDate(dueDate, "MM/dd/yyyy"));
        textViewStatus.setText(statusCompleted);
        textViewPriority.setText(priority);
        textViewReward.setText(String.valueOf(reward));
        textViewNotes.setText(note);
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void showChildConfirmDialog(final String id, final String assignTo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.child_confirm_chore, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Accept Chore");
        final AlertDialog b = dialogBuilder.create();
        b.show();


        TextView description = (TextView)dialogView.findViewById(R.id.descriptionConfirm);
        TextView descriptionNote = (TextView)dialogView.findViewById(R.id.notesConfirm);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.childCancel);
        final Button buttonConfirm = (Button) dialogView.findViewById(R.id.childAccept);

        description.setText(getString(R.string.accept_description, housechoreName));
        descriptionNote.setText(getString(R.string.note_description, note));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id).child("assignedTo");
                dR.setValue(assignTo);
                Toast.makeText(UpdateChoreActivity.this, "Successfully Assigned Chore to " + assignTo, Toast.LENGTH_LONG).show();
                LinearLayout buttonLinearLayout = (LinearLayout) findViewById(R.id.layoutAssignToMe);
                buttonLinearLayout.setVisibility(View.GONE);
                b.dismiss();
            }
        });
    }

    private void showAdultConfirmDeleteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.adult_delete_confirm, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Delete Chore");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.cancelButton);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.deleteButton);
        TextView description = (TextView)dialogView.findViewById(R.id.descriptionConfirm);

        description.setText(getString(R.string.confirm_delete, housechoreName));
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteHousechore(choreID);
                b.dismiss();
                finish();
            }
        });
    }

    private void showAdultConfirmAssignToDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.adult_assignto_confirm, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Assign Chore");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.cancelButton);
        final Button buttonAssign = (Button) dialogView.findViewById(R.id.assignButton);

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
                    users.add(userName);

                }

                Spinner userSpinner = (Spinner)dialogView.findViewById(R.id.assignto_spinner);
                ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(UpdateChoreActivity.this, android.R.layout.simple_spinner_item, users);
                usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userSpinner.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner userSpinner = (Spinner) dialogView.findViewById(R.id.assignto_spinner);
                assignHousechore(choreID, userSpinner.getSelectedItem().toString());
                b.dismiss();
                finish();
            }
        });
    }

    private void showAdultConfirmUpdateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_housechore_confirm, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Update Chore");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.cancelButton);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.updateButton);

        myCalendar = Calendar.getInstance();
        editChoredueDate = (EditText) dialogView.findViewById(R.id.edit_dueDate);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateChoreActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editHousechoreName = (EditText) dialogView.findViewById(R.id.edit_chore_name);
                EditText editChoredueDate = (EditText) dialogView.findViewById(R.id.edit_dueDate);
                EditText editNote = (EditText) dialogView.findViewById(R.id.edit_note);
                Spinner editChorePriority = (Spinner) dialogView.findViewById(R.id.edit_priority);
                Spinner editChoreCategory = (Spinner) dialogView.findViewById(R.id.edit_category);
                Spinner editChoreRewards = (Spinner) dialogView.findViewById(R.id.edit_rewards);
                final String dateString = editChoredueDate.getText().toString();
                final String stringHousechore = editHousechoreName.getText().toString();
                final String stringPriority = editChorePriority.getSelectedItem().toString();
                final String stringChoreCategory = editChoreCategory.getSelectedItem().toString();
                final String stringNote = editNote.getText().toString();
                final int intRewards = Integer.parseInt(editChoreRewards.getSelectedItem().toString());


                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final long timestampDate = convertedDate.getTime();
                updateHousechore(choreID, stringHousechore, timestampDate, stringPriority, stringChoreCategory, intRewards, stringNote);
                b.dismiss();
                finish();
            }
        });
    }

    private void updateHousechore(String id, String stringHousechore, Long timestampDate, String stringPriority, String stringChoreCategory, int intRewards, String stringNote) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id);
        Housechore housechore = new Housechore(id, stringHousechore, assignedTo, "N/A", timestampDate, stringPriority, stringChoreCategory, statusCompleted, intRewards, stringNote);
        dR.setValue(housechore);
        Toast.makeText(getApplicationContext(), "Housechore Updated", Toast.LENGTH_LONG).show();
    }

    private boolean assignHousechore(String id, String assignTo) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id).child("assignedTo");
        dR.setValue(assignTo);
        Toast.makeText(UpdateChoreActivity.this, "Successfully Assigned Chore to " + assignTo, Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteHousechore(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Housechore Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editChoredueDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void displayAllResources() {
        DatabaseReference databaseResources;
        databaseResources = FirebaseDatabase.getInstance().getReference().child("resources");
        databaseResources.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                int arraySize = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Resource resource = snapshot.getValue(Resource.class);
                    if (resource.getHousechore().equals(housechoreName)) {
                        arraySize = arraySize + 1;
                    }
                }
                String[] resourcesList = new String[arraySize];
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Resource resource = snapshot.getValue(Resource.class);
                    if (resource.getHousechore().equals(housechoreName)) {
                        resourcesList[i] = resource.getResourceName();
                        i = i + 1;
                    }

                }
                ListView listView = (ListView) findViewById(R.id.listViewResources);
                ResourceCustomAdapter adapter = new ResourceCustomAdapter(UpdateChoreActivity.this, resourcesList);
                listView.setAdapter(adapter);
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {

                        DatabaseReference databaseResources;
                        databaseResources = FirebaseDatabase.getInstance().getReference().child("resources");
                        databaseResources.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Resource resource = snapshot.getValue(Resource.class);
                                    if (resource.getHousechore().equals(housechoreName)) {
                                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("resources").child(resource.getID());
                                        dR.removeValue();
                                        Toast.makeText(UpdateChoreActivity.this, "Resource Deleted", Toast.LENGTH_LONG).show();
                                        displayAllResources();
                                    }
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

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
