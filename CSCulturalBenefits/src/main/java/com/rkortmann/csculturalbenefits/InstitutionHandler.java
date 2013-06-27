package com.rkortmann.csculturalbenefits;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Ryan on 6/26/13.
 */
public class InstitutionHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "institutions.db";
    public static final String TABLE_INSTITUTIONS = "institutions";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME= "name";

    public InstitutionHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INSTITUTIONS_TABLE = "create table " + TABLE_INSTITUTIONS + " (" + COLUMN_ID + " integer primary key, " + COLUMN_NAME + " text)";

        db.execSQL(CREATE_INSTITUTIONS_TABLE);

        ContentValues values;
        String[] institutions = { "American Ballet Theatre", "American Museum of Natural History", "Asia Society", "Brooklyn  Academy of Music", "Brooklyn Botanic Garden", "Brooklyn Children's Museum", "Brooklyn Museum of Art", "Carnegie Hall", "Central Park Conservancy", "Chamber Music Society of Lincoln Center", "Children's Museum of Manhattan", "Film Society of Lincoln Center", "The Frick Museum", "Guggenheim Museum", "Jazz at Lincoln Center", "Jewish Museum", "Liberty Science Center", "Lincoln Center for the Performing Arts", "Lincoln Center Theater", "Metropolitan Museum of Art/The Cloisters", "The Metropolitan Opera", "Morgan Library and Museum", "Museum of Math", "Museum of Modern Art", "Neue Galerie", "New York Botanical Garden", "New York City Ballet", "New York Hall of Science", "New York Historical Society", "New York Philharmonic", "New York Public Library", "Queens Botanical Garden", "Rubin Museum of Art", "Swiss Institute of Art", "Whitney Museum of American Art", "Wildlife Conservation Society" };

        for(int i = 0; i < institutions.length; i++) {
            values = new ContentValues();
            values.put(COLUMN_NAME, institutions[i]);
            db.insert(TABLE_INSTITUTIONS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTITUTIONS);
        onCreate(db);
    }
}
