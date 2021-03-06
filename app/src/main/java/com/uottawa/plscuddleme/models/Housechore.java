package com.uottawa.plscuddleme.models;

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
    private String statusCompleted;
    private int reward;
    private boolean rewarded;
    private String note;


    public Housechore() {

    }

    public Housechore(String id, String housechoreName, String assignedTo, String assignedBy,
                      long dueDate, String priority, String category, String statusCompleted, int reward, String note) {
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

    public Housechore(String id, String housechoreName, String assignedTo, String assignedBy,
                      long dueDate, String priority, String category, String statusCompleted, int reward, String note,
                      Boolean rewarded) {
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
        this.rewarded = rewarded;
    }

    // Getters and Setters
    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public void setHousechoreName(String housechoreName) {
        this.housechoreName = housechoreName;
    }

    public String getHousechoreName() {
        return housechoreName;
    }

    public void setAssignedTo(String personAssigned) {
        this.assignedTo = personAssigned;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedBy(String taskAssignedBy) {
        this.assignedBy = taskAssignedBy;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setDueDate(long dueDate) {
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

    public void setCompletedStatus(String completed) {
        this.statusCompleted = completed;
    }

    public String getCompletedStatus() {
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

    public void setRewarded(boolean rewarded) {
        this.rewarded = rewarded;
    }

    public boolean getRewarded() {
        return rewarded;
    }
    @Override
    public String toString() {
        return "Housechore{" +
                "id='" + id + '\'' +
                ", housechoreName='" + housechoreName + '\'' +
                ", assignedBy='" + assignedBy + '\'' +
                ", assignedTo='" + assignedTo + '\'' +
                ", dueDate=" + dueDate +
                ", priority='" + priority + '\'' +
                ", category='" + category + '\'' +
                ", statusCompleted='" + statusCompleted + '\'' +
                ", reward=" + reward +
                ", note='" + note + '\'' +
                '}';
    }
}

