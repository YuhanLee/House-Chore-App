package com.uottawa.plscuddleme;

/**
 * Created by Yuhan on 11/1/2017.
 */

public class Resource {
    private String id;
    private String resourceName;
    private String housechore;

    public Resource() {}

    public Resource(String id, String name, String housechoreId) {
        this.id = id;
        this.resourceName = name;
        this.housechore = housechoreId;
    }

    // Getters and Setters

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

    public String getHousechore() {
        return housechore;
    }

    public void setHousechore(String housechoreId) {
        this.housechore = housechoreId;
    }


}

