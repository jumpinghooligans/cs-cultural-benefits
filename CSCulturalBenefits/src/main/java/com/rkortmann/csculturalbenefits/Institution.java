package com.rkortmann.csculturalbenefits;

/**
 * Created by Ryan on 6/26/13.
 */
public class Institution {

    private int _id;
    private String _name;

    private String _address_street;
    private String _address_city;
    private String _address_state;
    private int _address_zip;

    private String _phone;
    private String _url;

    private String[] _description;

    public Institution() {

    }

    public Institution(String name, String street, String city, String state, int zip, String phone, String url, String[] description) {
        this._name = name;

        this._address_street = street;
        this._address_city = city;
        this._address_state = state;
        this._address_zip = zip;

        this._phone = phone;
        this._url = url;

        this._description = description;
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

    public void setAddressStreet(String street) {
        this._address_street = street;
    }

    public String getAddressStreet() {
        return this._address_street;
    }

    public void setAddressCity(String city) {
        this._address_city = city;
    }

    public String getAddressCity() {
        return this._address_city;
    }

    public void setAddressState(String state) {
        this._address_state = state;
    }

    public String getAddressState() {
        return this._address_state;
    }

    public void setAddressZip(int zip) {
        this._address_zip = zip;
    }

    public int getAddressZip() {
        return this._address_zip;
    }

    public void setPhone(String phone) {
        this._phone = phone;
    }

    public String getPhone() {
        return this._phone;
    }

    public void setUrl(String url) {
        this._url = url;
    }

    public String getUrl() {
        return this._url;
    }

    public void setDescription(String[] description) {
        this._description = description;
    }

    public String[] getDescription() {
        return this._description;
    }

    public String getDescriptionString(String glue) {
        String descriptionString = "";

        for(int i = 0; i < this._description.length; i++) {
            descriptionString += this._description[i];
            if(i < (this._description.length - 1)) {
                descriptionString += glue;
            }
        }

        return descriptionString;
    }
}
