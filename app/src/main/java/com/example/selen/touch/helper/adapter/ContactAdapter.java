package com.example.selen.touch.helper.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactAdapter {

    private static final String KEY_ROWID = "_id";
    private static final String KEY_ID = "id_struttura";
    private static final String KEY_SITO = "sito";
    private static final String KEY_MAIL = "mail";

    private static final String TAG = "ContactAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "contacts";
    private static final String SQLITE_TABLE = "contacts";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_ID + " integer," +
                    KEY_SITO + "," +
                    KEY_MAIL  + ");";


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

    public ContactAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public ContactAdapter open() throws SQLException {
        mDbHelper = new ContactAdapter.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createContact(Integer id, String sito, String mail) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, id);
        initialValues.put(KEY_SITO, sito);
        initialValues.put(KEY_MAIL, mail);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllContact() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    /*
    public Cursor fetchStructuresByCategory(String category) throws SQLException {
        Log.w(TAG, category);
        Cursor mCursor = null;
        if (category == null  ||  category.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_SITO,
                            KEY_MAIL},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID, KEY_SITO,
                            KEY_MAIL},
                    KEY_MAIL + " like '%" + category + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }*/

    public Cursor fetchAllContact() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_ID, KEY_SITO,
                        KEY_MAIL},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getContactById(Integer id){
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_ID, KEY_SITO,
                        KEY_MAIL},
                KEY_ID + " = " + id, null, null, null, null);

        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
