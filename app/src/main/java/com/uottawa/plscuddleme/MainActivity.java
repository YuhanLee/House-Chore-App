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

// MANDATORY
//TODO [x] Can add a chore with name and description
//TODO [x] Delete chore must update Chore List Page
//TODO [] Can edit a chore
//TODO [] Can delete a chore
//TODO [x] Completing a chore gives points to user who completed
//TODO [x] Can see the points obtained based on the completion of a chore
//TODO [x] Can list all chores assigned to a user
//TODO [x] Can list all chores belonging to a group
//TODO [x] Can change the status of a chore
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
    //firebase Ref
    private FirebaseAuth firebaseAuth;
    //UI Ref
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewLogin;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonRegister.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent (this, DrawerActivity.class));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                registerUser();
                break;

            case R.id.textViewLogin:
                startActivity(new Intent(this, SignInActivity.class));
                break;
        }
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter an Email",Toast.LENGTH_LONG).show();
            //stop function execution
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter password ",Toast.LENGTH_LONG).show();
            return;
        }

        //if valid
        progressDialog.setMessage("Registering User. Please wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), AddFamilyMember.class);
                            intent.putExtra("RegisteredEmail",email);
                            Log.v(TAG, email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        progressDialog.dismiss();
    }


}
