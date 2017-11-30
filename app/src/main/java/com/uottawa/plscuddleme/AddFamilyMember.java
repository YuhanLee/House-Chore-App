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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by user on 11/29/2017.
 */

public class AddFamilyMember extends AppCompatActivity {
    private static final String TAG = "AddFamilyMember";
    private DatabaseReference dataBaseFamilyMember;
    EditText editTextNickName;
    Spinner userRole;
    Button bAddMember;
    String memberEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_family_member);

        //need to get the email val from SignupActivity
        Intent intent = getIntent();
        memberEmail = intent.getStringExtra(SignupActivity.EXTRA_MESSAGE);
        Log.i(TAG,"AddFamilyMember!!!! memberEmail = "+memberEmail);

        dataBaseFamilyMember = FirebaseDatabase.getInstance().getReference("familyMembers");
        editTextNickName = (EditText) findViewById(R.id.fam_userName);
        userRole = (Spinner) findViewById(R.id.fam_userRole);
        bAddMember = (Button) findViewById(R.id.bAddMember);

        bAddMember.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                addFamilyMember();
            }
        });

    }

    private void addFamilyMember() {
        Log.i(TAG,"Inside addFamilyMember");

        DatabaseReference databaseFamilyMembers;
        databaseFamilyMembers = FirebaseDatabase.getInstance().getReference("familyMembers");
        String nickName = editTextNickName.getText().toString().trim();
        if (!TextUtils.isEmpty(nickName)) {
            String id = databaseFamilyMembers.push().getKey();
            String stringNickName = editTextNickName.getText().toString().trim();
            String stringRole = userRole.getSelectedItem().toString();

            Member newMember = new Member(id, memberEmail, stringNickName,stringRole,0,0);
            databaseFamilyMembers.child(id).setValue(newMember);
            editTextNickName.setText("");

            Toast.makeText(this, "Family Member Added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enter a nickName", Toast.LENGTH_LONG).show();
        }
        Intent openDrawer = new Intent (AddFamilyMember.this, drawer.class);
        startActivity(openDrawer);

    }
}
