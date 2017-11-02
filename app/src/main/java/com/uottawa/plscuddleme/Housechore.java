package com.uottawa.plscuddleme;

import java.util.Date;

/**
 * Created by Yuhan on 10/31/2017.
 */

public class Housechore {
    private int _id;
    private String _housechoreName;
    private String _assignedBy;
    private String _assignedTo;
    private String _deletedBy;
    private Date _dueDate;
    private String _priority;
    private String _category;
    private Boolean _statusCompleted;
    private int _reward;
    private String _note;
//TODO consider if should add user previlage for deletion --> if so then need to add an extra attribute.



    public Housechore (int id, String housechoreName, String assignedTo, String assignedBy, String deletedBy, Date dueDate, String priority, String category, Boolean statusCompleted, int reward, String note) {
        _id = id;
        _housechoreName = housechoreName;
        _assignedTo = assignedTo;
        _assignedBy = assignedBy;
        _deletedBy = deletedBy; //null at the beginning
        _dueDate = dueDate;
        _priority = priority;
        _category = category;
        _statusCompleted = statusCompleted;
        _reward = reward;
        _note = note;
    }

    public void setID(int id) {
        _id = id;
    }

    public int getID() {
        return _id;
    }

    public void setHousechoreName (String housechoreName) {
        _housechoreName = housechoreName;
    }
    public String getHousechoreName() {
        return _housechoreName;
    }

    public void setAssignedTo (String personAssigned) {
        _assignedTo = personAssigned;
    }
    public String getAssignedTo() {
        return _assignedTo;
    }

    public void setAssignedBy (String taskAssignedBy) {
        _assignedBy = taskAssignedBy;
    }

    public String getAssignedBy() {
        return _assignedBy;
    }

    public void setDeletedBy (String taskDeletedBy) {
        _deletedBy = taskDeletedBy;
    }

    public String getDeletedBy () {
       return _deletedBy;
    }

    public void setDueDate (Date dueDate) {
        _dueDate = dueDate;
    }
    public Date getDueDate() {
        return _dueDate;
    }

    public void setPriority(int priority) {
        _priority = priority;
    }

    public String getPriority() {
        return _priority;
    }

    public void setCategory(String category) {
        _category = category;
    }

    public String getCategory() {
        return _category;
    }

    public void setCompletionStatus(boolean completed) {
        _statusCompleted = completed;
    }

    public boolean getCompletedStatus() {
        return _statusCompleted;
    }

    public void setReward(int reward) {
        _reward = reward;
    }
    public int getReward() {
        return _reward;
    }

    public void setNote(String note) {
        _note = note;
    }
    public String getNote() {
        return _note;
    }

}

