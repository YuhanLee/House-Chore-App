package com.uottawa.plscuddleme;

import java.util.Date;

/**
 * Created by Yuhan on 10/31/2017.
 */

public class Housechore {
    private String id;
    private String housechoreName;
    private String assignedBy;
    private String assignedTo;
    private long dueDate;
    private String priority;
    private String category;
    private Boolean statusCompleted;
    private int reward;
    private String note;


    public Housechore() {

    }

    public Housechore(String id, String housechoreName, String assignedTo, String assignedBy, long dueDate, String priority, String category, Boolean statusCompleted, int reward, String note) {
        this.id = id;
        this.housechoreName = housechoreName;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
        this.statusCompleted = statusCompleted;
        this.reward = reward;
        this.note = note;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public void setHousechoreName (String housechoreName) {
        this.housechoreName = housechoreName;
    }
    public String getHousechoreName() {
        return housechoreName;
    }

    public void setAssignedTo (String personAssigned) {
        this.assignedTo = personAssigned;
    }
    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedBy (String taskAssignedBy) {
        this.assignedBy = taskAssignedBy;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setDueDate (long dueDate) {
        this.dueDate = dueDate;
    }
    public long getDueDate() {
        return dueDate;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCompletionStatus(boolean completed) {
        this.statusCompleted = completed;
    }

    public boolean getCompletedStatus() {
        return statusCompleted;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
    public int getReward() {
        return reward;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public String getNote() {
        return note;
    }

}

