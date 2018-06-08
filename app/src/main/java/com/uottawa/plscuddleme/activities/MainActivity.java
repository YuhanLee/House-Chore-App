package com.uottawa.plscuddleme.activities;

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
import com.uottawa.plscuddleme.R;

/**
 * Created by Yuhan on 11/29/2017.
 */

//first activity, when user opens app, will come to this page automatically
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MainActivity";
    //firebase References
    private FirebaseAuth firebaseAuth;

    //UI References of the sign in fields
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize UI references
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);

        //attaching listener to button
        buttonRegister.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

        //checks if there is a current user context. If there is one, will directly go to the drawer activity
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, DrawerActivity.class));
            finish();
        }
    }



    /**
     * implements the onclick for the buttonRegister and textView for signin (if the user
     * already have a log in email and password and wants to sign in with their existing credentials
     * @param v
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                registerUser();
                break;

            case R.id.textViewLogin:
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                break;
        }
    }

    /**
     * This methods registers a user for authentication. And returns whether registration is
     * successful or not. If registration unsucessful, the dialog will show the error messages
     * for registration
     */
    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //stop function execution if empty fields. The email and password must be entered
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter an Email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter password ", Toast.LENGTH_LONG).show();
            return;
        }

        buttonRegister.setText("Registering...");


        //This directly creates a user in the authentication in firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                            //This sends the registeredEmail to the AddFamilyMemberActivity class which attaches the email
                            //to the memberEmail attribute of the Member class
                            Intent intent = new Intent(getApplicationContext(), AddFamilyMemberActivity.class);
                            intent.putExtra("RegisteredEmail", email);
                            Log.v(TAG, email);
                            startActivity(intent);
                            finish();
                        } else {
                            //if there is a registration error, the error message will be displayed
                            //possible erros may include weak passwords, or preexisting members with the same email as the current registration
                            Toast.makeText(MainActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            buttonRegister.setText("Register");
                        }
                    }
                });
    }
}
