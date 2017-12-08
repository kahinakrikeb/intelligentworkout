package com.example.m.intelligentworkout;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccueilActivity extends Activity {
        Button accueil_apropos,accueil_load,accueil_best,accueil_play,accueil_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        HelperDB helper = new HelperDB(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        accueil_exit=(Button) findViewById(R.id.accueil_exit);
        accueil_apropos=(Button) findViewById(R.id.accueil_apropos);
        accueil_load=(Button) findViewById(R.id.accueil_load);
        accueil_best=(Button) findViewById(R.id.accueil_best);
        accueil_play=(Button) findViewById(R.id.accueil_play);
        accueil_apropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AccueilActivity.this,AproposActivity.class);
                startActivity(intent);
            }
        });

        accueil_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AccueilActivity.this,LoadActivity.class);
                startActivity(intent);
            }
        });

        accueil_best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AccueilActivity.this,BestPlayerActivity.class);
                startActivity(intent);
            }
        });

        accueil_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AccueilActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        accueil_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
            }
        });


    }
}
