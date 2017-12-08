package com.example.m.intelligentworkout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kahina on 08/12/2017.
 */

public class ScoreDB extends HelperDB {
    public ScoreDB(Context context) {
        super(context);
    }



   /*
   public Jeux updateJeux(Score score)
   {
        String sql="Select * from "+SAUVEGARDE+" where id = ?";
        Cursor cursor;
        cursor=getDb().rawQuery(sql,new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        Jeux jeux=new Jeux(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getInt(cursor.getColumnIndex("nbdeplacement")),
                cursor.getInt(cursor.getColumnIndex("temps")),
                cursor.getInt(cursor.getColumnIndex("niveau")),
                Helper.StringToArray(cursor.getString(cursor.getColumnIndex("grill")))
        );
        return jeux;
    }
    */

    public ArrayList<Score> getallScore()
    {
        String sql="Select * from "+SAUVEGARDE+" order by nbsecdep asc";
        Cursor cursor;
        cursor = getDb().rawQuery(sql, new String[]{});
        ArrayList<Score> allScore = new ArrayList<>();
        while (cursor.moveToNext()) {
            Score score=new Score(cursor.getInt(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("nbdeplacement")),cursor.getInt(cursor.getColumnIndex("temps")),
                    cursor.getInt(cursor.getColumnIndex("nbsecdep"))  );
            allScore.add(score);
        }
        return allScore;
    }
}
