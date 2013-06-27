package com.rkortmann.csculturalbenefits;

/**
 * Created by Ryan on 6/26/13.
 */
public class Institution {

    private int _id;
    private String _name;

    public Institution() {

    }

    public Institution(String name) {
        this._name = name;
    }

    public int getID() {
        return this._id;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }
}
