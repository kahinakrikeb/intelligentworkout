package com.example.m.intelligentworkout;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kahina on 05/12/2017.
 */

public class LoadAdapter extends RecyclerView.Adapter<LoadAdapter.ViewHolder>  {

    ArrayList<Jeux> list;

    public LoadAdapter(ArrayList<Jeux> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.load_item,parent,false);
        LoadAdapter.ViewHolder holder=new LoadAdapter.ViewHolder(view);

        return  holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Jeux jeux;
        TextView load_nom,load_nbmove,load_temps;
        public ViewHolder(View itemView) {
            super(itemView);
            load_nom=(TextView) itemView.findViewById(R.id.load_nom);
            load_nbmove=(TextView) itemView.findViewById(R.id.load_nbmove);
            load_temps=(TextView) itemView.findViewById(R.id.load_temps);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(view.getContext(),MainActivity.class);
                    intent.putExtra("toload",1);
                    intent.putExtra("nbmove",jeux.getNbmove());
                    intent.putExtra("nbtimer",jeux.getNbtimer());
                    intent.putExtra("level",jeux.getLevel());
                    intent.putExtra("carte",Helper.ArrayToString(jeux.getMatrice()));
                    Log.i("nbmove", "nbmove: "+jeux.getNbmove());
                    Log.i("nbtimer", "nbtimer: "+jeux.getNbtimer());
                    Log.i("level", "level: "+jeux.getLevel());
                    Log.i("getMatrice", "getMatrice: "+Helper.ArrayToString(jeux.getMatrice()));
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void bind(Jeux jeux) {
            this.jeux=jeux;
            load_nom.setText(jeux.getName());
            load_nbmove.setText(String.valueOf(jeux.getNbmove()));
            load_temps.setText(String.valueOf(jeux.getNbtimer()));
        }
    }
}
