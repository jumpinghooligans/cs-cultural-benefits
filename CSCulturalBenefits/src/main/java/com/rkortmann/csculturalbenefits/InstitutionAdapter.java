package com.rkortmann.csculturalbenefits;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.BaseAdapter;

/**
 * Created by Ryan on 6/26/13.
 */
public class InstitutionAdapter  {
    SQLiteDatabase db;
    InstitutionHandler handler;
    Context context;

    public InstitutionAdapter(Context c) {
        context = c;

        handler = new InstitutionHandler(context, handler.DATABASE_NAME, null, handler.DATABASE_VERSION);
        db = handler.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public Institution getInstitution(int id) {
        String[] cols = { handler.COLUMN_ID, handler.COLUMN_NAME, handler.COLUMN_ADDRESS_STREET, handler.COLUMN_ADDRESS_CITY, handler.COLUMN_ADDRESS_STATE, handler.COLUMN_ADDRESS_ZIP, handler.COLUMN_PHONE, handler.COLUMN_URL, handler.COLUMN_DESCRIPTION };

        Cursor c = db.query(handler.TABLE_INSTITUTIONS, cols, handler.COLUMN_ID + "=" + id, null, null, null, null);
        c.moveToFirst();

        Institution institution = new Institution(c.getString(c.getColumnIndex(handler.COLUMN_NAME)),
                c.getString(c.getColumnIndex(handler.COLUMN_ADDRESS_STREET)),
                c.getString(c.getColumnIndex(handler.COLUMN_ADDRESS_CITY)),
                c.getString(c.getColumnIndex(handler.COLUMN_ADDRESS_STATE)),
                Integer.parseInt(c.getString(c.getColumnIndex(handler.COLUMN_ADDRESS_ZIP))),
                c.getString(c.getColumnIndex(handler.COLUMN_PHONE)),
                c.getString(c.getColumnIndex(handler.COLUMN_URL)),
                c.getString(c.getColumnIndex(handler.COLUMN_DESCRIPTION)).split("\\|"));
        //Institution institution = new Institution(c.getString(2), "890 Broadway", "New York", "NY", 10003, "2124773030", "http://www.abt.org/", new String[] {"Discount tickets for select performances", "Opportunities to attend studio rehearsals"});

        return institution;
    }

    public Cursor queryInstitutions() {
        String[] cols = { handler.COLUMN_ID, handler.COLUMN_NAME, handler.COLUMN_PHONE };

        Cursor c = db.query(handler.TABLE_INSTITUTIONS, cols, "", null, null, null, null);

        return c;
    }
}