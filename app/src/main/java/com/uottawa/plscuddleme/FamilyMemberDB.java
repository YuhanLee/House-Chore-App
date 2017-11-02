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
    public static final String COLUMN_NUMBEROFTASKS = "numberOfTasks";
    public static final String COLUMN_REWARDPOINTS = "reward";
    public static final String COLUMN_NOTE = "TEXT";

    public FamilyMemberDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}


