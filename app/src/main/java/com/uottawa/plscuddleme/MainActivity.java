package com.uottawa.plscuddleme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// MANDATORY
//TODO [x] Can add a chore with name and description
//TODO [x] Delete chore must update Chore List Page
//TODO [] Can edit a chore
//TODO [] Can delete a chore
//TODO [] Completing a chore gives points to user who completed
//TODO [] Can see the points obtained based on the completion of a chore
//TODO [] Can list all chores assigned to a user
//TODO [] Can list all chores belonging to a group
//TODO [] Can change the status of a chore
//TODO [] Can assign a chore
//TODO [] Can deallocate a chore from a user
//TODO [] Can allocate resources to a chore
//TODO [] Can deallocate resources from a chore
//TODO [] Can see the list of all resources needed for all chores belonging to a group

// OPTIONAL
//TODO [] Handle adding a duplicate chore
//TODO [] Handle date must be after today when editting and adding
//TODO []

/**
 * Created by Yuhan on 11/29/2017.
 */

//first activity, when user opens app, will come to this page automatically
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MainActivity";

    private DatabaseReference databaseFamilyMembers;
    private FirebaseAuth firebaseAuth;
    private Button buttonSignin;
    private EditText enterEmail;
    private EditText enterPassword;
    private TextView toRegisterLink;
    private ProgressDialog progressDialog;


//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize views
        enterEmail = (EditText) findViewById(R.id.enterEmail);
        enterPassword = (EditText) findViewById(R.id.enterPassword);
        buttonSignin = (Button) findViewById(R.id.signIn_button);
        toRegisterLink = (TextView) findViewById(R.id.goto_register);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignin.setOnClickListener(this);
        toRegisterLink.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signIn_button:
                signIn(enterEmail.getText().toString().trim(),enterPassword.getText().toString().trim());
                break;

            case R.id.goto_register:
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }
    }

    private void signIn(String email, String password) {
        Intent tmp = new Intent(MainActivity.this, drawer.class);
        startActivity(tmp);

        //TODO fix signin if have time
//        if (!validateForm()) {
//            return;
//        }
//        progressDialog.show();
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(MainActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                        progressDialog.dismiss();
//                    }
//                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = enterEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            enterEmail.setError("Email Required.");
            valid = false;
        } else {
            enterEmail.setError(null);
        }

        String password = enterPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            enterPassword.setError("Password Required.");
            valid = false;
        } else {
            enterPassword.setError(null);
        }
        return valid;
    }


    private void updateUI (FirebaseUser user) {
        progressDialog.dismiss();
        if (user != null) {
//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
//            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
        //TODO --> need to update the UI. Update the drawer username to userNickName
        openDrawer();
    }
    private void openDrawer() {
        startActivity(new Intent (this, drawer.class));
    }
}
