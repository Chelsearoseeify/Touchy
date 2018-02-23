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

public class StructuresAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_STRUTTURA = "struttura";
    public static final String KEY_CATEGORIA = "categoria";
    public static final String KEY_SEGMENTO = "segmento";
    public static final String KEY_TIPOLOGIA = "tipologia";

    private static final String TAG = "StructuresAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "strutture";
    private static final String SQLITE_TABLE = "strutture";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY," +
                    KEY_STRUTTURA + "," +
                    KEY_CATEGORIA + "," +
                    KEY_SEGMENTO + "," +
                    KEY_TIPOLOGIA +"," +
                    " UNIQUE (" + KEY_STRUTTURA +","+ KEY_CATEGORIA +","+ KEY_SEGMENTO +","+ KEY_TIPOLOGIA+"));";


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

    public StructuresAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public StructuresAdapter open() throws SQLException {
        mDbHelper = new StructuresAdapter.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createStructure(Integer id, String struttura, String categoria,
                              String segmento, String tipologia) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_STRUTTURA, struttura);
        initialValues.put(KEY_CATEGORIA, categoria);
        initialValues.put(KEY_SEGMENTO, segmento);
        initialValues.put(KEY_TIPOLOGIA, tipologia);

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
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,KEY_STRUTTURA,
                            KEY_CATEGORIA, KEY_SEGMENTO, KEY_TIPOLOGIA},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,KEY_STRUTTURA,
                            KEY_CATEGORIA, KEY_SEGMENTO, KEY_TIPOLOGIA},
                    KEY_CATEGORIA + " like '%" + category + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllStructures() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,KEY_STRUTTURA,
                        KEY_CATEGORIA, KEY_SEGMENTO, KEY_TIPOLOGIA},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getStructureById(Integer id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_STRUTTURA,
                        KEY_CATEGORIA, KEY_SEGMENTO, KEY_TIPOLOGIA},
                KEY_ROWID + " = " + id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeStructures() {

        createStructure(1,"ACI Patera Motors","Viaggiare","Emergenza","Soccorso Stradale");
        createStructure(2,"AGIP","Viaggiare","Sosta","Stazione di Servizio");
        createStructure(3,"Campeggio Punta di Crabbia","Alloggiare","Low cost","Campeggio");

    }
}
