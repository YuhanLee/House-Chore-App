package com.uottawa.plscuddleme;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Yuhan on 11/19/2017.
 */


public class Profile extends Fragment {

    //UI references in the view
    TextView textViewUserNickName;
    TextView textViewProfileWelcome;
    TextView textViewUserRole;
    TextView textViewUserEmail;
    TextView textViewNumberOfChores;
    TextView textViewRewardPoints;
    ImageView imageViewProfile;

    //firebase references
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String userId;

    //firebase member info
    String userName;
    String memberEmail;
    String userRole;
    int numberOfAssignedTasks;
    int rewards;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("My Profile");
        textViewUserNickName = (TextView) getView().findViewById(R.id.textViewUserNickName);
        textViewProfileWelcome = (TextView) getView().findViewById(R.id.textViewProfileWelcome);
        textViewUserRole = (TextView) getView().findViewById(R.id.textViewUserRole);
        textViewUserEmail = (TextView) getView().findViewById(R.id.textViewUserEmail);
        textViewNumberOfChores = (TextView) getView().findViewById(R.id.textViewNumberOfChores);
        textViewRewardPoints = (TextView) getView().findViewById(R.id.textViewRewardPoints);
        imageViewProfile = (ImageView) getView().findViewById(R.id.imageViewProfile);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();

        //Gets the current user context as a member object, calls getMember to get all attributes
        DatabaseReference databaseMembers;
        databaseMembers = FirebaseDatabase.getInstance().getReference().child("familyMembers");
        databaseMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id;
                for (DataSnapshot familyMembers : dataSnapshot.getChildren()) {
                    Member member = familyMembers.getValue(Member.class);
                    id = member.getID();
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


    }


    /**
     * Gets the member data and retrives all the information (attributes) on the user
     * calls setView() to set all the textViews on the profile itself 
     * @param member
     */
    private void getMember(Member member) {
        userName = member.getfamilyMemberName();
        memberEmail = member.getMemberEmail();
        numberOfAssignedTasks = member.getNumberOfAssignedTasks();
        userRole = member.getUserRole();
        rewards = member.getRewards();
        setView();
    }

    /**
     * This function sets all the textViews in the profile page to the user info
     */
    private void setView() {
        textViewUserNickName.setText(getString(R.string.profile_disp_userName) + " " + userName);
        textViewProfileWelcome.setText("Welcome "+userName+ "!");
        textViewUserRole.setText(getString(R.string.profile_disp_userRole) + " " + userRole);
        textViewUserEmail.setText(getString(R.string.profile_disp_userEmail) + " " + memberEmail);
        textViewNumberOfChores.setText(getString(R.string.profile_disp_userChores) + " " + numberOfAssignedTasks);
        textViewRewardPoints.setText(getString(R.string.profile_disp_userRewardPoints) + " " + rewards);


        if (userRole.equals("Adult")) {
            imageViewProfile.setImageResource(R.drawable.profile_img_adult);
        } else {
            imageViewProfile.setImageResource(R.drawable.profile_img_child);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_profile, container, false);
    }
}
