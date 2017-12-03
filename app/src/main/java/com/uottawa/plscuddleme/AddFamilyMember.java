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
    private Spinner userRole;
    private TextView textViewUserEmail;
    private Button buttonSave;
    private String emailFromRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_family_member);

        Bundle extras = getIntent().getExtras();
        emailFromRegister = extras.getString("RegisteredEmail");
        Log.v(TAG,emailFromRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent (this, SignInActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextNickName = (EditText) findViewById(R.id.userNickName);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        userRole = (Spinner) findViewById(R.id.memberRole);

        FirebaseUser user = firebaseAuth.getCurrentUser();


        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonSave:
                buttonSave();
                break;
        }
    }


    public void buttonSave() {
        Log.i(TAG,"Inside addFamilyMember");
        String name = editTextNickName.getText().toString().trim();
        String memberRole = userRole.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a nick name", Toast.LENGTH_LONG).show();
            return;
        }

        addFamilyMemberInDb(name, emailFromRegister, memberRole);
        openDrawer();
    }

    private void addFamilyMemberInDb(String name, String email, String memberRole) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseFamilyMembers = FirebaseDatabase.getInstance().getReference("familyMembers");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String id = user.getUid();
        Member member = new Member(id,email, name, memberRole,0,0);
        databaseFamilyMembers.child(user.getUid()).setValue(member);
        Toast.makeText(this, "Family Member Added", Toast.LENGTH_LONG).show();

        editTextNickName.setText("");
        finish();
    }

    private void openDrawer() {
        Intent gotoDrawer = new Intent (this, drawer.class);
        startActivity(gotoDrawer);
    }
}
