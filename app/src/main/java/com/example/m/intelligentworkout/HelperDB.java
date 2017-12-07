package com.example.m.intelligentworkout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kahina on 02/12/2017.
 */

public class HelperDB extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "intelli.db";
        final String SCORE="score",SAUVEGARDE="sauvgarde";

    protected SQLiteDatabase getDb() {
        if (mDb == null || !mDb.isOpen())
            mDb = getWritableDatabase();
        return mDb;
    }

    static protected SQLiteDatabase mDb = null;

    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+SCORE+" (id integer primary key , name string(255),nbdeplacement integer(255),temps integer);");
        sqLiteDatabase.execSQL("create table "+SAUVEGARDE+" " +
                "(id integer primary key , name string(255),nbdeplacement integer," +
                "temps integer,niveau integer,grill string(255));");
        sqLiteDatabase.execSQL("insert into "+SCORE+" (id, name,nbdeplacement ,temps )values" +
                "(1, \" \",0 ,0 )," +
                "(2, \" \",0 ,0 )," +
                "(3, \" \",0 ,0 )," +
                "(4, \" \",0 ,0 )," +
                "(5, \" \",0 ,0 )," +
                "(6, \" \",0 ,0 )," +
                "(7, \" \",0 ,0 )," +
                "(8, \" \",0 ,0 )," +
                "(9, \" \",0 ,0 )," +
                "(10, \" \",0 ,0 );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
