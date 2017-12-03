package com.uottawa.plscuddleme;

/**
 * Created by Yuhan on 11/1/2017.
 */

public class Resource {
    private int id;
    private String resourceName;
    private Housechore housechore;
    private String note;


    public Resource(int id, String name, Housechore housechore, String note) {
        this.id = id;
        this.resourceName = name;
        this.housechore = housechore;
        this.note = note;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String name) {
        this.resourceName = name;
    }


    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public Housechore getHousechore() {
        return housechore;
    }

    public void setHousechore(Housechore chore) {
        this.housechore = chore;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }


}

