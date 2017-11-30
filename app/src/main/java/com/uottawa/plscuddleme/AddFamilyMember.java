package com.uottawa.plscuddleme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by user on 11/29/2017.
 */

public class AddFamilyMember extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddFamilyMember";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseFamilyMembers;

    private EditText editTextNickName;
    private EditText editTextEmail;
    private Spinner userRole;
    private TextView textViewUserEmail;
    private Button buttonSave;
    private Button buttonLogOut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_family_member);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent (this, SignInActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextEmail = (EditText) findViewById(R.id.userEmail);
        editTextNickName = (EditText) findViewById(R.id.userNickName);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        userRole = (Spinner) findViewById(R.id.memberRole);

        FirebaseUser user = firebaseAuth.getCurrentUser();

 //       textViewUserEmail.setText("Welcome " +user.getEmail());

        buttonLogOut.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogOut:
                logOut();
                break;
            case R.id.buttonSave:
                saveFamilyMember();
                openDrawer();
                break;
        }
    }

    private void logOut () {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent (this, SignInActivity.class));

    }

    private void saveFamilyMember() {


        Log.i(TAG,"Inside addFamilyMember");
        String name = editTextNickName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String memberRole = userRole.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a nick name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG).show();
        }
     databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseFamilyMembers = FirebaseDatabase.getInstance().getReference("familyMembers");
        Member member = new Member(email, name, memberRole,0,0);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseFamilyMembers.child(user.getUid()).setValue(member);
        Toast.makeText(this, "Family Member Added", Toast.LENGTH_LONG).show();
    }

    private void openDrawer() {
        Intent gotoDrawer = new Intent (this, drawer.class);
        startActivity(gotoDrawer);
    }
}
