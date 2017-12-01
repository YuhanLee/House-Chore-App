package com.uottawa.plscuddleme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateChoreActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "UpdateChoreActivity";
    private String view;
    private String rowClick;
    private String choreID;
    private String userRole;
    private int row;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Boolean tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO find user.Role for setting view
        tmp = decideRole().equals("Adult");
        Log.i(TAG, "tmp = "+tmp);

        if (decideRole().equals("Adult")) {
            Log.i(TAG,"decideRole does equal to Adult");
            setContentView(R.layout.adult_update_housechore);
        } else {
            setContentView(R.layout.update_housechore);
        }
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras!= null) {
            rowClick = extras.getString("ROW_CLICKED");
            row = Integer.parseInt(rowClick);
            choreID = extras.getString("CHORE_ID");
            Log.i(TAG,"Chore_id = " +choreID);
            Log.i(TAG,"rowClick" +rowClick);
            Log.i(TAG,"row = "+row);
        }
    }

    @Override
    public void onClick(View view) {
    }

    private String decideRole() {
        userRole = "Adult";
        //userRole = firebase..a;sldkjfa;lsdkj

        if (userRole == "Adult") {
            return "Adult";
        } else {
            return "Child";
        }

//        firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser() != null) {
//            firebaseUser = firebaseAuth.getCurrentUser();
//        }

    }
}
