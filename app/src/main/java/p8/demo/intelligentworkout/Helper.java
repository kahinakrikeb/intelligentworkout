package p8.demo.intelligentworkout;

import android.util.Log;

import java.util.Random;

/**
 * Created by kahina on 24/11/2017.
 */

public class Helper {

    // taille de la carte
    static final int    CARTEWIDTH    = 5;
    static final int    CARTEHEIGHT   = 5;
    static final int    CARTETILESIZE = 50;

    // taille de la carte minateur

    static final int   CARTETILESIZE_MIN = 15;

    // constante modelisant les differentes types de cases
    static final int    CST_BLEU     = 4;
    static final int    CST_ROUGE  = 0;

    static int[][] getGrillRef()
    {
        int [][] ref    = {
                {CST_BLEU, CST_BLEU,CST_ROUGE, CST_BLEU,CST_BLEU },
                {CST_BLEU, CST_BLEU,CST_ROUGE, CST_BLEU, CST_BLEU},
                {CST_ROUGE, CST_ROUGE,CST_ROUGE, CST_ROUGE, CST_ROUGE},
                {CST_BLEU, CST_BLEU, CST_ROUGE, CST_BLEU, CST_BLEU},
                {CST_BLEU, CST_BLEU, CST_ROUGE,CST_BLEU, CST_BLEU},
        };
        return ref;
    }

    static int[][] getRandomGrill()
    {
        int nbbleu=16,nbroug=9,couleur=0;
        int [][] ref    = new int[CARTEHEIGHT][CARTEWIDTH];
        for (int i=0;i<CARTEHEIGHT;i++)
        {
            for (int j=0;j<CARTEWIDTH;j++)
            {

                if (nbbleu>0 && nbroug>0)
                {
                    couleur=(int)(Math.random()*2);
                }
                else {
                    if (nbbleu>0)
                        couleur=0;
                    if (nbroug>0)
                        couleur=1;
                }
                switch (couleur){
                    case 0:nbbleu--;ref[i][j]=CST_BLEU;break;
                    case 1:nbroug--;ref[i][j]=CST_ROUGE;break;
                }
            }

        }
        return ref;
    }

    static  int[][] dupCarte(int[][] carte)
    {
        int [][] tmpcarte=new int[CARTEHEIGHT][CARTEWIDTH];
        for(int i=0;i<CARTEHEIGHT;i++)
        {
            for (int j=0;j<CARTEWIDTH;j++)
            {
                tmpcarte[i][j]=carte[i][j];
            }
        }
        return tmpcarte;
    }
    static  int[][] depColTop(int[][]carte,int colone)
    {
        int [][] tmpcarte=dupCarte(carte);
        for (int i=0;i<CARTEHEIGHT;i++) {
            if(i==0)
                carte[CARTEHEIGHT-1][colone]=tmpcarte[i][colone];
            else {
                    carte[i-1][colone]=tmpcarte[i][colone];
            }
        }
        return carte;
    }
    static  int[][] depColDown(int[][]carte,int colone)
    {
        int [][] tmpcarte=dupCarte(carte);
        for (int i=0;i<CARTEHEIGHT;i++) {
            if(i==0)
                carte[i][colone]=tmpcarte[CARTEHEIGHT-1][colone];
            else
                carte[i][colone]=tmpcarte[i-1][colone];

        }
        return carte;
    }
 static  int[][] depLineLeft(int[][]carte,int line)
    {
        int [][] tmpcarte=dupCarte(carte);
        for (int i=0;i<CARTEWIDTH;i++) {
            if(i==0)
                carte[line][CARTEWIDTH-1]=tmpcarte[line][i];
            else {
                carte[line][i-1]=tmpcarte[line][i];
            }
        }
        return carte;
    }
    static  int[][] depLineRight(int[][]carte,int line)
    {
        
        int [][] tmpcarte=dupCarte(carte);
        for (int i=0;i<CARTEWIDTH;i++) {
            if(i==0)
                carte[line][i]=tmpcarte[line][CARTEWIDTH-1];
            else
                carte[line][i]=tmpcarte[line][i-1];

        }
        return carte;
    }


    static Boolean isSame(int[][] grill,int[][]ref)
    {
        boolean same=true;
        for (int i=0;i<CARTEHEIGHT;i++) {
            for (int j = 0; j < CARTEWIDTH; j++) {
                if(grill[i][j]!=ref[i][j]){
                    same=false;
                    break;
                }
            }
            if(!same)
                break;
        }
        return same;
    }
}
