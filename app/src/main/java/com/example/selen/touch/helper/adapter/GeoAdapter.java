package com.example.selen.touch.helper.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 27/11/2017.
 */

public class GeoAdapter {


    private static final String KEY_ROWID = "_id";
    private static final String KEY_ID = "id_struttura";
    private static final String KEY_TELEFONO = "telefono";
    private static final String KEY_LATITUDINE = "latitudine";
    private static final String KEY_LONGITUDINE = "longitudine";
    private static final String KEY_INDIRIZZO = "indirizzo";
    private static final String KEY_COMUNE = "comune";


    private static final String TAG = "GeoAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "geolocal";
    private static final String SQLITE_TABLE = "geolocal";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_ID + " integer, " +
                    KEY_TELEFONO + "," +
                    KEY_LATITUDINE + " real," +
                    KEY_LONGITUDINE + " real," +
                    KEY_INDIRIZZO +"," +
                    KEY_COMUNE + ");";


    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public GeoAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public GeoAdapter open() throws SQLException {
        mDbHelper = new GeoAdapter.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createGeo(Integer id, String telefono, Float latitudine,
                              Float longitudine, String indirizzo, String comune) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, id);
        initialValues.put(KEY_TELEFONO, telefono);
        initialValues.put(KEY_LATITUDINE, latitudine);
        initialValues.put(KEY_LONGITUDINE, longitudine);
        initialValues.put(KEY_INDIRIZZO, indirizzo);
        initialValues.put(KEY_COMUNE, comune);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllStructures() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchStructuresByCategory(String category) throws SQLException {
        Log.w(TAG, category);
        Cursor mCursor = null;
        if (category == null  ||  category.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_ID,  KEY_TELEFONO,
                            KEY_LATITUDINE, KEY_LONGITUDINE, KEY_INDIRIZZO, KEY_COMUNE},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID, KEY_ID,  KEY_TELEFONO,
                            KEY_LATITUDINE, KEY_LONGITUDINE, KEY_INDIRIZZO, KEY_COMUNE},
                    KEY_LATITUDINE + " like '%" + category + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllStructures() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_ID,  KEY_TELEFONO,
                        KEY_LATITUDINE, KEY_LONGITUDINE, KEY_INDIRIZZO, KEY_COMUNE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getGeoById(Integer id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_ID, KEY_TELEFONO,
                        KEY_LATITUDINE, KEY_LONGITUDINE, KEY_INDIRIZZO, KEY_COMUNE},
                KEY_ID + " = " + id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
