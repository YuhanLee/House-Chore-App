package com.uottawa.plscuddleme;

/**
 * Created by user on 11/1/2017.
 */

public class FamilyMember {
    private int id;
    private String familyMemberName;
    private String userRole;
    private int numberOfAssignedTasks;
    private int rewards;


    public FamilyMember() {}

    public FamilyMember(int id, String familyMemberName, String userRole, int numberOfAssignedTasks, int rewards) {
        this.id = id;
        this.familyMemberName = familyMemberName;
        this.userRole = userRole;
        this.numberOfAssignedTasks = numberOfAssignedTasks;
        this.rewards = rewards;
    }



    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getfamilyMemberName() {
        return familyMemberName;
    }

    public void setfamilyMemberName(String familyMemberName) {
        this.familyMemberName = familyMemberName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getNumberOfAssignedTasks() {
        return numberOfAssignedTasks;
    }

    public void setNumberOfAssignedTasks(int numberOfAssignedTasks) {
        this.numberOfAssignedTasks = numberOfAssignedTasks;
    }
    public int getRewards() {
        return rewards;
    }

    public void setRewards(int rewards) {
        this.rewards = rewards;
    }
}
