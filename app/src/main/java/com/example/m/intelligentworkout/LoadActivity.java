package com.example.m.intelligentworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {

    RecyclerView lvListe;
    ArrayList<Jeux> alljeux=new ArrayList<Jeux>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        alljeux= new JeuxDB(this).getallJeux();
        lvListe = (RecyclerView) findViewById(R.id.Load_recyclerView);
        lvListe.setLayoutManager(new LinearLayoutManager(this));
        lvListe.setAdapter(new LoadAdapter(alljeux));
    }
}
