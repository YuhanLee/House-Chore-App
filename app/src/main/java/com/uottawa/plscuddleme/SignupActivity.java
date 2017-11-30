
package com.uottawa.plscuddleme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "com.uottawa.plscuddleme.EMAIL";
    private static final String TAG = "SigninAct";
    //firebase obj
    private DatabaseReference databaseFamilyMembers;
    private FirebaseAuth firebaseAuth;
    private Button buttonSignup;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextNickName;
    private TextView existingMemberSignin;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize firebase auth obj
        firebaseAuth = FirebaseAuth.getInstance();

        //initialize views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignup = (Button) findViewById(R.id.signup_button);
        existingMemberSignin = (TextView) findViewById(R.id.goto_signin);
        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void registerUser() {
        String error_message="";

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog
        progressDialog.setMessage("Registering. Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            Toast.makeText(SignupActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);

                        }  else {
                            Log.i(TAG,"signInwithEmailFailed");
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(SignupActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signup_button:
                registerUser();
                addFamilyMember();
                break;

            case R.id.goto_signin:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }


    private void addFamilyMember() {
        Intent gotoAddFamMember = new Intent (SignupActivity.this, AddFamilyMember.class);
        String newUserEmail = editTextEmail.getText().toString().trim();
        gotoAddFamMember.putExtra(EXTRA_MESSAGE,newUserEmail);
        startActivity(gotoAddFamMember);
    }

}

