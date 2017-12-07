package com.example.m.intelligentworkout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable{
    MenuItem pauseResume;
    private IntelligentView mIntelligent;
    TextView int_move,int_timer;
    Boolean runthread=true;
    int nbtimer=0;
    private     Thread timer_thread;
    MediaPlayer music;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int_move=(TextView) this.findViewById(R.id.int_move);
        int_timer=(TextView) this.findViewById(R.id.int_timer);
        // recuperation de la vue une voie cree à partir de son id
        mIntelligent = (IntelligentView)findViewById(R.id.IntelligentView);
        // rend visible la vue
        mIntelligent.settextview(int_move,int_timer);
         mIntelligent.setVisibility(View.VISIBLE);
       int toload= getIntent().getIntExtra("toload",0);
        music= MediaPlayer.create(MainActivity.this,R.raw.zeldadubstep);

        music.setLooping(true);
        music.start();

       if(toload==1)
       {
           nbtimer=getIntent().getIntExtra("nbtimer",0);
           int_move.setText(String.valueOf(getIntent().getIntExtra("nbmove",0)));
           int_timer.setText(String.valueOf(nbtimer));
           mIntelligent.setCarte_m(Helper.getGrillRef(getIntent().getIntExtra("level",0)));
           mIntelligent.setCarte(Helper.StringToArray(getIntent().getStringExtra("carte")));
           mIntelligent.nbmove=getIntent().getIntExtra("nbmove",0);


       }
        mIntelligent.initparameters();
        Log.i("onCreate", "onCreate: fffff");

        timer_thread   = new Thread(this);
        if ((timer_thread!=null) && (!timer_thread.isAlive())) {
            timer_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        music.stop();
    }


    @Override
    protected void onResume() {

        super.onResume();
        music.start();
        mIntelligent.resume();

    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mIntelligent.setCarte((int[][])  Helper.StringToArray(savedInstanceState.getString("carte")));
        mIntelligent.setCarte_m((int[][]) Helper.StringToArray(savedInstanceState.getString("carte_m")));
        nbtimer=savedInstanceState.getInt("nbtimer");
        mIntelligent.nbmove=savedInstanceState.getInt("nbmove");
        mIntelligent.initparameters();
        int_move.setText(String.valueOf(mIntelligent.nbmove));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("carte",  Helper.ArrayToString(mIntelligent.getCarte()));
        savedInstanceState.putString("carte_m", Helper.ArrayToString(mIntelligent.getCarte_m()));
        savedInstanceState.putInt("nbtimer", nbtimer);
        savedInstanceState.putInt("nbmove", mIntelligent.nbmove);
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.menu_accueil);
        pauseResume=menu.findItem(R.id.menu_pause);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pause:
                Log.i("pause", "onOptionsItemSelected: "+mIntelligent.isIspause());
                if(!mIntelligent.isIspause()){
                    mIntelligent.setIspause(true);
                    pauseResume.setTitle("Resume");
                }
                else {
                    mIntelligent.setIspause(false);
                    pauseResume.setTitle("Pause");
                }
                return true;
            case R.id.menu_save:
                Intent intent=new Intent(MainActivity.this,SaveActivity.class);
                intent.putExtra("nbtimer",nbtimer);
                intent.putExtra("nbmove",mIntelligent.nbmove);
                intent.putExtra("carte_m",Helper.ArrayToString(mIntelligent.getCarte_m()));
                intent.putExtra("carte",Helper.ArrayToString(mIntelligent.getCarte()));
                intent.putExtra("level",mIntelligent.niveau);
                mIntelligent.debutjeux=true;

                startActivity(intent);
                return true;
            case R.id.menu_accueil:
                Intent intent1= new Intent(MainActivity.this,AccueilActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void run() {
        while (runthread){
            try {
                timer_thread.sleep(1000);
                if(!mIntelligent.isIspause() && mIntelligent.debutjeux)
                nbtimer++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        int_timer.setText(String.valueOf(nbtimer));
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
