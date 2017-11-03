package com.uottawa.plscuddleme;

/**
 * Created by user on 11/1/2017.
 */

public class FamilyMember {
    private int _id;
    private String _userName;
    private String _userRole;
    private int _numberOfAssignedTasks;
    private int _rewards;


    public FamilyMember(int _id, String _userName, String _userRole, int _numberOfAssignedTasks, int _rewards) {
        this._id = _id;
        this._userName = _userName;
        this._userRole = _userRole;
        this._numberOfAssignedTasks = _numberOfAssignedTasks;
        this._rewards = _rewards;
    }

    // TODO a lot


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_userRole() {
        return _userRole;
    }

    public void set_userRole(String _userRole) {
        this._userRole = _userRole;
    }

    public int get_numberOfAssignedTasks() {
        return _numberOfAssignedTasks;
    }

    public void set_numberOfAssignedTasks(int _numberOfAssignedTasks) {
        this._numberOfAssignedTasks = _numberOfAssignedTasks;
    }

    public int get_rewards() {
        return _rewards;
    }

    public void set_rewards(int _rewards) {
        this._rewards = _rewards;
    }
}
