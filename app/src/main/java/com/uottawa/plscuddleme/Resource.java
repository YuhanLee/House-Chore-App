package com.uottawa.plscuddleme;

import java.util.Date;

/**
 * Created by Yuhan on 11/1/2017.
 */

public class Resource {
    private int id;
    private String itemName;
    private long dueDate;
    private String note;


    public Resource(int id, String itemName, long dueDate, String note) {
        this.id = id;
        this.itemName = itemName;
        this.dueDate = dueDate;
        this.note = note;

    }
    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setDueDate (long dueDate) {
        this.dueDate = dueDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setNote (String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }


}

