package com.uottawa.plscuddleme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

        //TODO find user.Role for setting view

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            choreID = extras.getString("CHORE_ID");
            Log.v(TAG,"choreID = " +choreID);
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
                    Log.i(TAG, "The value of id before getchore is " + id);
                    Log.i(TAG, "The value of choreId before getchore is " + choreID);
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
                    Log.i(TAG, "The value of id before getmember is " + id);
                    Log.i(TAG, "The value of choreId before getmember is " + userId);
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


    }



    private void getChore(Housechore chore) {

        //TODO get assignedBy
        //assignedBy = chore.getAssignedBy();
        Log.i(TAG, "getchore is being called");
        housechoreName = chore.getHousechoreName();
        assignedTo = chore.getAssignedTo();
        dueDate = chore.getDueDate();
        priority = chore.getPriority();
        category = chore.getCategory();
        statusCompleted = chore.getCompletedStatus();
        reward = chore.getReward();
        note = chore.getNote();
        String debug = chore.toString();
        Log.v(TAG, "*****************getChore called, chore val  = " +debug);

    }

    @Override
    public void onClick(View view) {

    }

    private void getMember(Member member) {
        Log.i(TAG, "getmember is being called");
        userName = member.getfamilyMemberName();
        userRole = member.getUserRole();
        setView();
    }

    private void setView() {
        if (userRole.equals("Adult")) {
            setContentView(R.layout.adult_update_housechore);
        } else {
            setContentView(R.layout.child_update_housechore);

            if (!(assignedTo.equals(""))) {
                Log.v(TAG, "assignedTo = "+assignedTo);
                LinearLayout buttonLinearLayout = (LinearLayout) findViewById(R.id.layoutAssignToMe);
                buttonLinearLayout.setVisibility(View.GONE);
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
        textViewDueDate.setText(getDate(dueDate,"MM/dd/yyyy"));
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
}
