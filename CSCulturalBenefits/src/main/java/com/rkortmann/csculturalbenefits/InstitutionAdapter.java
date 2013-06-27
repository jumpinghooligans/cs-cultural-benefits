package com.rkortmann.csculturalbenefits;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ryan on 6/26/13.
 */
public class InstitutionAdapter {
    SQLiteDatabase db;
    InstitutionHandler handler;
    Context context;

    public InstitutionAdapter(Context c) {
        context = c;

        handler = new InstitutionHandler(context, handler.DATABASE_NAME, null, handler.DATABASE_VERSION);
        db = handler.getReadableDatabase();
    }

    public void close() {
        db.close();
    }

    public Cursor queryInstitutions() {
        String[] cols = { handler.COLUMN_ID, handler.COLUMN_NAME };

        Cursor c = db.query(handler.TABLE_INSTITUTIONS, cols, "", null, null, null, null);

        return c;
    }
}