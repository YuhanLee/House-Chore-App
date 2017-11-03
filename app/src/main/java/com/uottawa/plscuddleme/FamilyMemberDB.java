package com.uottawa.plscuddleme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;


/**
 * Created by user on 11/1/2017.
 */

public class FamilyMemberDB extends SQLiteOpenHelper{

    private static final String DATABASE_NAME="familyMember.db";
    private static final int DATABASE_VERSION =1;
    public static final String TABLE_FAMILYMEMBER = "familyMember";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_FAMILYMEMBERNAME = "familyMemberName";
    public static final String COLUMN_USERROLE = "Role";
    public static final String COLUMN_NUMBEROFTASKS = "numberOfTasks";
    public static final String COLUMN_REWARDPOINTS = "reward";

    public FamilyMemberDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAMILYMEMBER_TABLE =
                "CREATE TABLE " +TABLE_FAMILYMEMBER+ "("
                        +COLUMN_ID+ "INTEGER PRIMARY KEY,"
                        +COLUMN_FAMILYMEMBERNAME+ "TEXT,"
                        +COLUMN_USERROLE+ "TEXT,"
                        +COLUMN_NUMBEROFTASKS+ "INTEGER PRIMARY KEY,"
                        +COLUMN_REWARDPOINTS+ "INTEGER PRIMARY KEY,"
                        +")";
        db.execSQL(CREATE_FAMILYMEMBER_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILYMEMBER);
        onCreate(db);
    }

    public void addFamilyMember(FamilyMember famMember){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAMILYMEMBERNAME, famMember.getfamilyMemberName());
        values.put(COLUMN_USERROLE, famMember.getUserRole());
        values.put(COLUMN_NUMBEROFTASKS, famMember.getNumberOfAssignedTasks());
        values.put(COLUMN_REWARDPOINTS, famMember.getRewards());
        db.insert(TABLE_FAMILYMEMBER, null, values);
        db.close();
    }

    public FamilyMember findFamilyMember(String familyMemberName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * FROM " +TABLE_FAMILYMEMBER + " WHERE " +
                COLUMN_FAMILYMEMBERNAME + " = \"" +familyMemberName + "\"";
        Cursor cursor = db.rawQuery(query,null);

        FamilyMember familyMember = new FamilyMember();
        if (cursor.moveToFirst()) {
            familyMember.setID(Integer.parseInt(cursor.getString(0)));
            familyMember.setfamilyMemberName((cursor.getString(1)));
            //TODO get more values set!!!!!!!!!!!!!!!!!!

            cursor.close();
        }   else {
            familyMember = null;
        }
        db.close();
        return familyMember;
    }



}


