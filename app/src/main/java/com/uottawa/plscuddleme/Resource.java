package com.uottawa.plscuddleme;

/**
 * Created by Yuhan on 11/1/2017.
 */

public class Resource {
    private String id;
    private String resourceName;
    private Housechore housechore;


    public Resource(String id, String name, Housechore housechore) {
        this.id = id;
        this.resourceName = name;
        this.housechore = housechore;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String name) {
        this.resourceName = name;
    }


    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public Housechore getHousechore() {
        return housechore;
    }

    public void setHousechore(Housechore chore) {
        this.housechore = chore;
    }



}

