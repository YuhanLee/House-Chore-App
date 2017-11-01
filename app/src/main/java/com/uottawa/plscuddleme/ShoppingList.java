package com.uottawa.plscuddleme;

import java.util.Date;

/**
 * Created by Yuhan on 11/1/2017.
 */

public class ShoppingList {
    private int _id;
    private String _shoppingListName;
    private Date _dueDate;
    private String _note;


    public ShoppingList (int id, String shoppingListName, Date dueDate, String note) {
        _id = id;
        _shoppingListName = shoppingListName;
        _dueDate = dueDate;
        _note = note;
    }
    public void setID(int id) {
        _id = id;
    }

    public int getID() {
        return _id;
    }

    public void setDueDate (Date dueDate) {
        _dueDate = dueDate;
    }

    public Date getDueDate() {
        return _dueDate;
    }

    public void setNote (String note) {
        _note = note;
    }

    public String getNote() {
        return _note;
    }


}

