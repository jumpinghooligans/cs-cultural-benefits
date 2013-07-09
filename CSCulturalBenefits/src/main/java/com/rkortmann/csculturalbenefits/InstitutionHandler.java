package com.rkortmann.csculturalbenefits;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ryan on 6/26/13.
 */
public class InstitutionHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 18;

    public static final String DATABASE_NAME = "institutions.db";
    public static final String TABLE_INSTITUTIONS = "institutions";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_ADDRESS_STREET = "address_street";
    public static final String COLUMN_ADDRESS_CITY = "address_city";
    public static final String COLUMN_ADDRESS_STATE = "address_state";
    public static final String COLUMN_ADDRESS_ZIP = "address_zip";

    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_URL = "url";

    public static final String COLUMN_DESCRIPTION = "description";

    private Context ctx;

    public InstitutionHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INSTITUTIONS_TABLE = "create table " + TABLE_INSTITUTIONS + " (" + COLUMN_ID + " integer primary key, "
                + COLUMN_NAME + " text, "
                + COLUMN_ADDRESS_STREET + " text, "
                + COLUMN_ADDRESS_CITY + " text, "
                + COLUMN_ADDRESS_STATE + " text, "
                + COLUMN_ADDRESS_ZIP + " text, "
                + COLUMN_PHONE + " text, "
                + COLUMN_URL + " text, "
                + COLUMN_DESCRIPTION + " text)";

        db.execSQL(CREATE_INSTITUTIONS_TABLE);

        ContentValues values;

        List<Institution> institutions = new ArrayList<Institution>();

        /*
        institutions.add(new Institution("American Ballet Theatre", "890 Broadway", "New York", "NY", 10003, "(212) 477-3030", "http://www.abt.org/", new String[] {"Discount tickets for select performances", "Opportunities to attend studio rehearsals"}));
        institutions.add(new Institution("American Museum of Natural History", "Central Park West at 79th Street", "New York", "NY", 10024, "(212) 769-5100", "http://www.amnh.org/", new String[] {"Free admission for employees and five guests"}));
        institutions.add(new Institution("Asia Society", "725 Park Avenue", "New York", "NY", 10021, "(212) 288-6400", "http://www.asiasociety.org/", new String[] {"Free admission to Museum for employees plus one guest, their family members, alumni and retired employees", "Discounted rates for cultural and policy programs"}));
        institutions.add(new Institution("Brooklyn Academy of Music", "30 Lafayette Ave", "Brooklyn", "NY", 11217, "(718) 636-4100", "http://www.bam.org/", new String[] {"15% Ticket Discount for BAM mainstage performances. This excludes price-class one tickets"}));
        */

        //institutions.add(new Institution("", "", "New York", "NY", 100, 212, new String[] {""}));

        for(Institution institution : institutions) {
            values = new ContentValues();

            values.put(COLUMN_NAME, institution.getName());
            values.put(COLUMN_ADDRESS_STREET, institution.getAddressStreet());
            values.put(COLUMN_ADDRESS_CITY, institution.getAddressCity());
            values.put(COLUMN_ADDRESS_STATE, institution.getAddressState());
            values.put(COLUMN_ADDRESS_ZIP, institution.getAddressZip());
            values.put(COLUMN_PHONE, institution.getPhone());
            values.put(COLUMN_URL, institution.getUrl());
            values.put(COLUMN_DESCRIPTION, institution.getDescriptionString("|"));

            db.insert(TABLE_INSTITUTIONS, null, values);
        }

    }

    public void updateDBWithJSON(JSONObject json) {
        System.out.println("UPDATE DB: " + json.length());

        Iterator keys = json.keys();
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTITUTIONS);
        onCreate(db);

        while(keys.hasNext()) {
            String key = (String) keys.next();
            if(!key.substring(0, 4).equals("jcr:")) {
                System.out.println("Processing: " + key);
                try {

                    JSONObject institute = json.getJSONObject(key).getJSONObject("jcr:content");

                    values = new ContentValues();

                    values.put(COLUMN_NAME, institute.getString("jcr:title"));
                    values.put(COLUMN_ADDRESS_STREET, "street");
                    values.put(COLUMN_ADDRESS_CITY,  "city");
                    values.put(COLUMN_ADDRESS_STATE, "state");
                    values.put(COLUMN_ADDRESS_ZIP, "55555");

                    values.put(COLUMN_PHONE, "phone");
                    values.put(COLUMN_URL, institute.getString("linkUrl"));

                    String desc = "";
                    try {
                        JSONArray descJson = institute.getJSONArray("listItems");

                        for(int i = 0; i < descJson.length(); i++) {
                            desc += descJson.getString(i);
                            if(i < (descJson.length() - 1)) {
                                desc += "|";
                            }
                        }
                    } catch (JSONException je) {
                        desc = institute.getString("listItems");
                    }
                    values.put(COLUMN_DESCRIPTION, desc);

                    db.insert(TABLE_INSTITUTIONS, null, values);

                } catch (Exception e) {
                    System.out.println("JSON PARSE: " + e);
                }

            }
        }
    }

    public String parseAddress(String address, String section) {
        String result = "";

        if(section.equals("street")) {
            result = address.replace("\r\n", "<br />");
            result = result.replace("\n", "");
        }
        else if (section.equals("city")) {

        }
        else if (section.equals("state")) {

        }
        else if (section.equals("zip")) {

        } else {
            result = "Uh oh.";
        }

        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTITUTIONS);
        onCreate(db);
    }
}
