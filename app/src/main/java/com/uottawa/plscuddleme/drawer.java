package com.uottawa.plscuddleme;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textViewWelcome;
    TextView logoutText;
    private static final String TAG = "Drawer";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    String userId;
    String userName;
    String memberEmail;
    int numberOfAssignedTasks;
    int rewards;
    String userRole;
    String memberInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            firebaseUser = firebaseAuth.getCurrentUser();
            userId = firebaseUser.getUid();
            Log.i(TAG, "&&&&&&userId = " + userId);
        }

        DatabaseReference databaseMembers;
        databaseMembers = FirebaseDatabase.getInstance().getReference().child("familyMembers");
        databaseMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id;
                for (DataSnapshot familyMembers : dataSnapshot.getChildren()) {
                    Member member = familyMembers.getValue(Member.class);
                    id = familyMembers.getKey();
                    if (userId.equals(id)) {
                        getMember(member);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toy1 = new Intent(drawer.this, AddHouseChore.class);
                startActivity(toy1);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        displaySelectedScreen(R.id.nav_openChores);
}


    public void getMember(Member member) {
        userName = member.getfamilyMemberName();
        memberEmail = member.getMemberEmail();
        numberOfAssignedTasks = member.getNumberOfAssignedTasks();
        userRole = member.getUserRole();
        rewards = member.getRewards();
        memberInfo = member.toString();

        Log.v(TAG,"************************************************");
        Log.v(TAG, "userName = " + userName);
        Log.v(TAG, "memberEmail = " + memberEmail);
        Log.v(TAG, "numberOfAssignedTasks = " + numberOfAssignedTasks);
        Log.v(TAG, "userRole = " + userRole);
        Log.v(TAG, "rewards = " + rewards);
        Log.v(TAG, "memberInfo = " + memberInfo);

        setView();

    }


    private void setView() {
        textViewWelcome.setText("Welcome "+userName);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.logout_confirm, null);
        dialogBuilder.setView(dialogView);

        String tmp = "Do you wish to log out?";
        TextView logoutText = (TextView)dialogView.findViewById(R.id.confirmLogout);
        logoutText.setText(tmp);

        dialogBuilder.setTitle("Bye Bye "+userName+" :'(");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.cancelButton);
        final Button buttonConfirm = (Button) dialogView.findViewById(R.id.confirmButton);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                b.dismiss();
                finish();
                startActivity(new Intent (getApplicationContext(), SignInActivity.class));
            }
        });
    }

    private void displaySelectedScreen(int navId) {
        Fragment fragment = null;
        switch (navId) {
            case R.id.nav_account:
                fragment = new Profile();
                setDrawerLayoutGone();
                break;
            case R.id.nav_chores:
                fragment = new OpenChore();
                setDrawerLayoutGone();
                break;
            case R.id.nav_shoppingList:
                fragment = new ShoppingList();
                setDrawerLayoutGone();
                break;
            case R.id.nav_schedule:
                fragment = new Schedule();
                setDrawerLayoutGone();
                break;
            case R.id.nav_people:
                fragment = new FamilyMembers();
                setDrawerLayoutGone();
                break;
            case R.id.nav_logout:
                showLogoutDialog();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setDrawerLayoutGone() {
        RelativeLayout drawerLayout = (RelativeLayout) findViewById(R.id.drawerInner);
        drawerLayout.setVisibility(View.GONE);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }
}

