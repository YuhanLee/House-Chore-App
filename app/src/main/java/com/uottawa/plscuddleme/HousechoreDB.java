package com.uottawa.plscuddleme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.Date;

/**
 * Created by Yuhan on 10/31/2017.
 */

public class HousechoreDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="housechoreDB.db";
    private static final int DATABASE_VERSION =1;
    public static final String TABLE_HOUSECHORE = "housechore";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_HOUSECHORENAME = "housechoreName";
    public static final String COLUMN_ASSIGNEDBY = "assigneBy";
    public static final String COLUMN_ASSIGNEDTO = "assigneTo";
    public static final String COLUMN_DUEDATE = "dueDate";
    public static final String COLUMN_NOTE = "TEXT";

    //constructor
    public HousechoreDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override



    public void onCreate(SQLiteDatabase db) {
        String CREATE_HOUSECHORE_TABLE =
                "CREATE TABLE " +TABLE_HOUSECHORE+ "("
                        +COLUMN_ID+ "INTEGER PRIMARY KEY,"
                        +COLUMN_HOUSECHORENAME+ "TEXT,"
                        +COLUMN_ASSIGNEDBY+ "TEXT,"
                        +COLUMN_ASSIGNEDTO+ "TEXT,"
                        +COLUMN_DUEDATE+ "STRING,"
                        +COLUMN_NOTE+ "TEXT,"
                        +")";
        db.execSQL(CREATE_HOUSECHORE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSECHORE);
        onCreate(db);
    }

    public void addHousechore(Housechore housechore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOUSECHORENAME, housechore.getHousechoreName());
        values.put(COLUMN_ASSIGNEDBY, housechore.getAssignedBy());
        values.put(COLUMN_ASSIGNEDTO, housechore.getAssignedTo());
        values.put(COLUMN_DUEDATE, housechore.getDueDate().toString());
        values.put(COLUMN_NOTE, housechore.getNote());
        db.insert(TABLE_HOUSECHORE, null, values);
        db.close();
    }


    public Housechore findHousechore(String housechorename) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * FROM " +TABLE_HOUSECHORE + " WHERE " +
                COLUMN_HOUSECHORENAME + " = \"" +housechorename + "\"";
        Cursor cursor = db.rawQuery(query,null);

        Housechore housechore = new Housechore();
        if (cursor.moveToFirst()) {
            housechore.setID(Integer.parseInt(cursor.getString(0)));
            housechore.setHousechoreName((cursor.getString(1));
            //TODO get more values set!!!!!!!!!!!!!!!!!!

            cursor.close();
        }   else {
            housechore = null;
        }
        db.close();
        return housechore;
    }

    public boolean deleteHousechore(String housechoreName) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * FROM " +TABLE_HOUSECHORE + " WHERE " +
                COLUMN_HOUSECHORENAME + " = \"" +housechoreName + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String idStr = cursor.getString(0);
            db.delete(TABLE_HOUSECHORE, COLUMN_ID + " = " +idStr, null);
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
