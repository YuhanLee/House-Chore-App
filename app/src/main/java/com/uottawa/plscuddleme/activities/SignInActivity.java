
package com.uottawa.plscuddleme.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "com.uottawa.plscuddleme.EMAIL";

    private Button buttonSignIn;
    private EditText enterEmail;
    private EditText enterPassword;
    private TextView textViewSignUp;
    private FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        enterEmail = (EditText) findViewById(R.id.enterEmail);
        enterPassword = (EditText) findViewById(R.id.enterPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, AddFamilyMemberActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignIn:
                userLogIn();
                break;

            case R.id.textViewSignUp:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private void userLogIn() {
        String email = enterEmail.getText().toString().trim();
        String password = enterPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter an Email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter an Email", Toast.LENGTH_LONG).show();
            return;
        }

        //at this time, both fields are filled in
        buttonSignIn.setText(R.string.Signing_in);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //start add profile
                            startActivity(new Intent(getApplicationContext(), DrawerActivity.class));
                            finish();
                        } else {
                            buttonSignIn.setText(R.string.signin);
                            Toast.makeText(getApplicationContext(), "Sign in unsuccessful: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}


