package com.example.m.intelligentworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class BestPlayerActivity extends AppCompatActivity {
    ArrayList<Score> scorelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_player);
        scorelist=new ScoreDB(this).getallScore();
        RecyclerView recylcer=(RecyclerView) findViewById(R.id.Score_recyclerView);
        recylcer.setLayoutManager(new LinearLayoutManager(this));
        recylcer.setAdapter(new BestPlayerAdapter(scorelist));
    }
}
