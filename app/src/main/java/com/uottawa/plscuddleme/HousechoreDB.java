package com.uottawa.plscuddleme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

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
    public static final String COLUMN_DELETEDBY = "deletedBy";
    public static final String COLUMN_DUEDATE = "dueDate";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_REWARD = "reward";
    public static final String COLUMN_NOTE = "TEXT";

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
                        +COLUMN_DELETEDBY+ "TEXT,"
                        +COLUMN_DUEDATE+ "STRING,"
                        +COLUMN_PRIORITY+ "STRING,"
                        +COLUMN_CATEGORY+ "STRING,"
                        +COLUMN_STATUS+ "STRING,"
                        +COLUMN_REWARD+ "TEXT,"
                        +COLUMN_NOTE+ "TEXT,"
                        +")";
        db.execSQL(CREATE_HOUSECHORE_TABLE);
    }

    //TODO know the difference between TEXT and STRING in SQL

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSECHORE);
        onCreate(db);
    }

    public void addHousechore(Housechore housechore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOUSECHORENAME, housechore.getHousechoreName());
        values.put(COLUMN_ASSIGNEDTO, housechore.getAssignedTo());
        values.put(COLUMN_ASSIGNEDBY, housechore.getAssignedBy());
        values.put(COLUMN_DELETEDBY, housechore.getDeletedBy());
        values.put(COLUMN_DUEDATE, housechore.getDueDate().toString());
        values.put(COLUMN_PRIORITY, housechore.getPriority());
        values.put(COLUMN_CATEGORY, housechore.getCategory());
        values.put(COLUMN_STATUS, housechore.getCompletedStatus());
        values.put(COLUMN_REWARD, housechore.getReward());
        values.put(COLUMN_NOTE, housechore.getNote());

       // values.put(COLUMN_REWARD, housechore.getNote());
        db.insert(TABLE_HOUSECHORE, null, values);
        db.close();
    }
    //TODO check for duplicate here??? or should call findHousechore before adding a housechore?


    public Housechore findHousechore(String housechorename) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * FROM " +TABLE_HOUSECHORE + " WHERE " +
                COLUMN_HOUSECHORENAME + " = \"" +housechorename + "\"";
        Cursor cursor = db.rawQuery(query,null);

        Housechore housechore = new Housechore();
        if (cursor.moveToFirst()) {
            housechore.setID(Integer.parseInt(cursor.getString(0)));
            housechore.setHousechoreName((cursor.getString(1)));
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

    //TODO implement a method so that the table can be seen
}
