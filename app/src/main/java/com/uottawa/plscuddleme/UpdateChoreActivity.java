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
    private String choreID;

    //Firebase + User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String userRole;
    String userId;
    String userName;

    //Housechore
    String housechoreName;
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

        // Gets all instances of the chores based on the ID
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
                        // call function that will assign instance variables to the current page's chore information
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
                        // call function that will assign instance variables to the current logged in user information
                        getMember(member);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Call function that appends and displays resources list in the chore profile page
        displayAllResources();


    }

    /**
     * Assign local values to current chore page's information
     */
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

    /**
     * Assign local values to current logged in user's information
     */
    private void getMember(Member member) {
        userName = member.getfamilyMemberName();
        userRole = member.getUserRole();
        setView();
    }

    /**
     * Load the child or adult xml file depending on which type of user is logged in
     */
    private void setView() {
        if (userRole.equals("Adult")) {
            setContentView(R.layout.adult_update_housechore);
            Button buttonAssignTo = (Button) findViewById(R.id.buttonAssignTo);
            Button buttonEdit = (Button) findViewById(R.id.buttonEdit);
            Button buttonDelete = (Button) findViewById(R.id.buttonDelete);
            buttonDelete.setOnClickListener(this);
            buttonAssignTo.setOnClickListener(this);
            buttonEdit.setOnClickListener(this);
        } else {
            setContentView(R.layout.child_update_housechore);
            if (!(assignedTo.equals("Unassigned"))) {
                LinearLayout buttonLinearLayout = (LinearLayout) findViewById(R.id.layoutAssignToMe);
                buttonLinearLayout.setVisibility(View.GONE);
            } else {
                Button buttonAssignToMe = (Button) findViewById(R.id.buttonAssignToMe);
                buttonAssignToMe.setOnClickListener(this);
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

    /**
     * This function converts from date in milliseconds to a desired format
     * @param dateFormat the desired date format you want to convert to
     * @param milliSeconds the value of milliseconds that will be converted
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    /**
     * This function displays a dialog that allows users to confirm allocating a chore to him/herself
     */
    private void showChildConfirmDialog(final String id, final String assignTo) {
        // Build Dialog and show
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.child_confirm_chore, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Accept Chore");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // Set variables
        TextView description = (TextView) dialogView.findViewById(R.id.descriptionConfirm);
        TextView descriptionNote = (TextView) dialogView.findViewById(R.id.notesConfirm);
        final Button buttonCancelChild = (Button) dialogView.findViewById(R.id.childCancel);
        final Button buttonConfirmChild = (Button) dialogView.findViewById(R.id.childAccept);

        // Set text corresponding to chore name and note
        description.setText(getString(R.string.accept_description, housechoreName));
        descriptionNote.setText(getString(R.string.note_description, note));

        // Close dialog if click cancel
        buttonCancelChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonConfirmChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update Value
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("housechores").child(id).child("assignedTo");
                dR.setValue(assignTo);
                Toast.makeText(UpdateChoreActivity.this, "Successfully Assigned Chore to " + assignTo, Toast.LENGTH_LONG).show();
                // Remove assignToMe button
                LinearLayout buttonLinearLayout = (LinearLayout) findViewById(R.id.layoutAssignToMe);
                buttonLinearLayout.setVisibility(View.GONE);
                // Close Dialog
                b.dismiss();
                // Finish activity, return to previous activity
                finish();
            }
        });
    }

    /**
     * This function displays a dialog that allows an adult to confirm and delete a chore
     */
    private void showAdultConfirmDeleteDialog() {
        // Build Dialog and show
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.adult_delete_confirm, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Delete Chore");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // Set variables
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.cancelButton);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.deleteButton);
        TextView description = (TextView) dialogView.findViewById(R.id.descriptionConfirm);

        // Set text corresponding to housechore name that will be deleted
        description.setText(getString(R.string.confirm_delete, housechoreName));

        // Close dialog if click cancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        // Call function that deletes housechore
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteHousechore(choreID);
                // Close dialog
                b.dismiss();
                // Finish activity and return to previous activity
                finish();
            }
        });
    }

    /**
     * This function displays a dialog that allows Adults to assign chore to a person
     */
    private void showAdultConfirmAssignToDialog() {
        // Build and show dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.adult_assignto_confirm, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Assign Chore");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button buttonCancelAdult = (Button) dialogView.findViewById(R.id.cancelButton);
        final Button buttonAssignAdult = (Button) dialogView.findViewById(R.id.assignButton);

        DatabaseReference databaseMembers;
        databaseMembers = FirebaseDatabase.getInstance().getReference().child("familyMembers");
        databaseMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Create an empty array and add unassigned value
                final List<String> users = new ArrayList<String>();
                users.add("Unassigned");
                // Loop through database and add all users to array
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Member member = snapshot.getValue(Member.class);
                    String userName = member.getfamilyMemberName();
                    users.add(userName);

                }

                // Add all array elements to the spinner
                Spinner userSpinner = (Spinner) dialogView.findViewById(R.id.assignto_spinner);
                ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(UpdateChoreActivity.this, android.R.layout.simple_spinner_item, users);
                usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userSpinner.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Close dialog on click cancel
        buttonCancelAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        // Call function that assigns housechore, and exit activity smoothly
        buttonAssignAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner userSpinner = (Spinner) dialogView.findViewById(R.id.assignto_spinner);
                assignHousechore(choreID, userSpinner.getSelectedItem().toString());
                b.dismiss();
                finish();
            }
        });
    }

    /**
     * This function displays a dialog that allows Adult to confirm changes to a chore
     */
    private void showAdultConfirmUpdateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_housechore_confirm, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Update Chore");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button buttonCancelChore = (Button) dialogView.findViewById(R.id.cancelButton);
        final Button buttonUpdateChore = (Button) dialogView.findViewById(R.id.updateButton);

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

        buttonCancelChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });



        buttonUpdateChore.setOnClickListener(new View.OnClickListener() {
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

                if (dateString.isEmpty() || stringHousechore.isEmpty() || stringPriority.isEmpty() ||
                        stringChoreCategory.isEmpty() || intRewards == 0) {
                    Toast.makeText(getApplicationContext(),"The chore name and due date are mandatory fields", Toast.LENGTH_LONG).show();
                    return;
                }


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
        String myFormat = "MM/dd/yyyy";
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
                // Loop through database and check how many resources are assigned to current chore page
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Resource resource = snapshot.getValue(Resource.class);
                    if (resource.getHousechore().equals(housechoreName)) {
                        arraySize = arraySize + 1;
                    }
                }
                // Create array based on amount of database elements that has same name of current chore page
                String[] resourcesList = new String[arraySize];
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Resource resource = snapshot.getValue(Resource.class);
                    if (resource.getHousechore().equals(housechoreName)) {
                        resourcesList[i] = resource.getResourceName();
                        i = i + 1;
                    }

                }
                // Display list of resources that are allocated to current chore page name
                ListView listView = (ListView) findViewById(R.id.listViewResources);
                ResourceCustomAdapter adapter = new ResourceCustomAdapter(UpdateChoreActivity.this, resourcesList);
                listView.setAdapter(adapter);
                // Add on long click listener to rows of resources, which delete the resource on long click
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
                                    // Check database for which resource was selected
                                    if (resource.getHousechore().equals(housechoreName)) {
                                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("resources").child(resource.getID());
                                        dR.removeValue();
                                        Toast.makeText(UpdateChoreActivity.this, "Resource Deleted", Toast.LENGTH_LONG).show();
                                        // Update listview
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonDelete:
                showAdultConfirmDeleteDialog();
                break;

            case R.id.buttonAssignTo:
                showAdultConfirmAssignToDialog();
                break;


            case R.id.buttonEdit:
                showAdultConfirmUpdateDialog();
                break;

            case R.id.buttonAssignToMe:
                showChildConfirmDialog(choreID, userName);
        }

    }
}
