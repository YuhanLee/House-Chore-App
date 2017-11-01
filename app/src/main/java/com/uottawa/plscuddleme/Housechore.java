package com.uottawa.plscuddleme;

import java.util.Date;

/**
 * Created by Yuhan on 10/31/2017.
 */

public class Housechore {
    private int _id;
    private String _housechoreName;
    private String _assignedTo;
    private String _assignedBy;
    private Date _dueDate;
    private int _reward;
    private String _note;


    public Housechore (int id, String housechoreName, String assignedTo, String assignedBy, Date dueDate, int reward, String note) {
        _id = id;
        _housechoreName = housechoreName;
        _assignedTo = assignedTo;
        _assignedBy = assignedBy;
        _dueDate = dueDate;
        _reward = reward;
        _note = note;
    }
    public void setID(int id) {
        _id = id;
    }

    public int getID() {
        return _id;
    }

    public String getAssignedTo() {
        return _assignedTo;
    }

    public void setAssignedTo (String personAssigned) {
        _assignedTo = personAssigned;
    }

    public String getAssignedBy() {
        return _assignedBy;
    }

    public void setAssignedBy (String taskAssignedBy) {
        _assignedBy = taskAssignedBy;
    }

    public void setHousechoreName (String housechoreName) {
        _housechoreName = housechoreName;
    }

    public String getHousechoreName() {
        return _housechoreName;
    }

    public void setDueDate (Date dueDate) {
        _dueDate = dueDate;
    }

    public Date getDueDate() {
        return _dueDate;
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

