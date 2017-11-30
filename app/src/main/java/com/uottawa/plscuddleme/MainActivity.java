package com.uottawa.plscuddleme;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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


public class MainActivity extends Activity {
    private static final String TAG = "Main Activity";
    private String[] mPageNames;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ListView mDrawerList;

    public Button but1;

    public void init() {
        but1 = (Button)findViewById(R.id.signin_button);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toy = new Intent(MainActivity.this, drawer.class);
                startActivity(toy);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }


}
