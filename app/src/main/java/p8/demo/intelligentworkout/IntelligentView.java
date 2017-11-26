package p8.demo.intelligentworkout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import javax.security.auth.login.LoginException;

public class IntelligentView extends SurfaceView implements SurfaceHolder.Callback, Runnable,GestureDetector.OnGestureListener {

	// Declaration des images
    private Bitmap 		block;
    private Bitmap 		diamant;
    private Bitmap 		perso;
    private Bitmap 		vide;    
    private Bitmap[] 	zone = new Bitmap[4];
    private Bitmap 		up;
    private Bitmap 		down;
    private Bitmap 		left;
    private Bitmap 		right; 
    private Bitmap 		win;
    private Bitmap 		blockm;
    private Bitmap 		videm;
    
	// Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources 	mRes;    
    private Context 	mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;
    int[][] carte_m;
    
    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  // coordonnées en X du point d'ancrage de notre carte

    // ancres pour pouvoir centrer la carte du jeu
    int        carte_mTopAnchor;                   // coordonnées en Y du point d'ancrage de la miniature
    int        carte_mLeftAnchor;                  // coordonnées en X du point d'ancrage de la miniature

    // taille de la carte
   /* static final int    carteWidth    = 5;
    static final int    carteHeight   = 5;
    static final int    carteTileSize = 50;*/

    // taille de la carte minateur
    //static final int    carteWidth_m    = 5;
    //static final int    carteHeight_m   = 5;
    static final int    carteTileSize_m = 15;
    GestureDetector detector;
    // constante modelisant les differentes types de cases
    static final int    CST_block     = 0;
    static final int    CST_diamant   = 1;
    static final int    CST_perso     = 2;
    static final int    CST_zone      = 3;
    static final int    CST_vide      = 4;
    static final int    CST_blockm    = 0;
    static final int    CST_videm     = 4;

    // tableau de reference du terrain
    int [][] ref    = Helper.getRandomGrill();
    int [][] ref_m1    = Helper.getGrillRef();

    // position de reference du joueur
    int refxPlayer = 4;
    int refyPlayer = 1;

    // position courante du joueur
        int xPlayer = 4;
        int yPlayer = 1;

        /* compteur et max pour animer les zones d'arriv�e des diamants */
        int currentStepZone = 0;
        int maxStepZone     = 4;

        // thread utiliser pour animer les zones de depot des diamants
        private     boolean in      = true;
        private     Thread  cv_thread;        
        SurfaceHolder holder;
        
        Paint paint;
        
    /**
     * The constructor called from the main JetBoy activity
     * 
     * @param context 
     * @param attrs 
     */
    public IntelligentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            detector=new GestureDetector(this.getContext(),this);
        }
        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed        
    	holder = getHolder();
        holder.addCallback(this);    
        
        // chargement des images
        mContext	= context;
        mRes 		= mContext.getResources();        
        block 		= BitmapFactory.decodeResource(mRes, R.drawable.block);
        diamant		= BitmapFactory.decodeResource(mRes, R.drawable.diamant);
    	//perso		= BitmapFactory.decodeResource(mRes, R.drawable.perso);
        zone[0] 	= BitmapFactory.decodeResource(mRes, R.drawable.zone_01);
        zone[1] 	= BitmapFactory.decodeResource(mRes, R.drawable.zone_02);
        zone[2] 	= BitmapFactory.decodeResource(mRes, R.drawable.zone_03);
        zone[3] 	= BitmapFactory.decodeResource(mRes, R.drawable.zone_04);
    	vide 		= BitmapFactory.decodeResource(mRes, R.drawable.vide);
    	up 			= BitmapFactory.decodeResource(mRes, R.drawable.up);
    	down 		= BitmapFactory.decodeResource(mRes, R.drawable.down);
    	left 		= BitmapFactory.decodeResource(mRes, R.drawable.left);
    	right 		= BitmapFactory.decodeResource(mRes, R.drawable.right);
    	win 		= BitmapFactory.decodeResource(mRes, R.drawable.win);
        blockm 		= BitmapFactory.decodeResource(mRes, R.drawable.blockm);
        videm 		= BitmapFactory.decodeResource(mRes, R.drawable.videm);
    	
    	// initialisation des parmametres du jeu
    	initparameters();
    	// creation du thread
        cv_thread   = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true); 

    }

    // chargement de la miniateur 1 _______________________________________________
    private void loadlevelm1() {
        for (int i=0; i< Helper.CARTEHEIGHT; i++) {
            for (int j=0; j< Helper.CARTEWIDTH; j++) {
                carte_m[j][i]= ref_m1[j][i];
            }
        }
    }


     private void loadlevel() {
    	for (int i=0; i< Helper.CARTEHEIGHT; i++) {
            for (int j=0; j< Helper.CARTEWIDTH; j++) {
                carte[j][i]= ref[j][i];
            }
        }
    }

    // initialisation du jeu
    public void initparameters() {
    	paint = new Paint();
    	paint.setColor(0xff0000);
    	paint.setDither(true);
    	paint.setColor(0xFFFFFF00);
    	paint.setStyle(Paint.Style.STROKE);
    	paint.setStrokeJoin(Paint.Join.ROUND);
    	paint.setStrokeCap(Paint.Cap.ROUND);
    	paint.setStrokeWidth(2);
    	paint.setTextAlign(Paint.Align.LEFT);

        carte_m           = new int[Helper.CARTEHEIGHT][Helper.CARTEWIDTH];
       // loadlevelm1();
        carte_m=ref_m1;
        carteTopAnchor  = (getHeight()- Helper.CARTEHEIGHT*Helper.CARTETILESIZE_MIN);
        carteLeftAnchor = (getWidth()- Helper.CARTEWIDTH*Helper.CARTETILESIZE_MIN);

        carte           = new int[Helper.CARTEHEIGHT][Helper.CARTEWIDTH];
        //loadlevel();
        carte=ref;
        carteTopAnchor  = (getHeight()- Helper.CARTEHEIGHT*Helper.CARTETILESIZE)/2;
        carteLeftAnchor = (getWidth()- Helper.CARTEWIDTH*Helper.CARTETILESIZE)/2;
        xPlayer = refxPlayer;
        yPlayer = refyPlayer;

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {        	
        	cv_thread.start();
        	Log.e("-FCT-", "cv_thread.start()");
        }
    }    

    // dessin des fleches
    private void paintarrow(Canvas canvas) {
    	canvas.drawBitmap(up, (getWidth()-up.getWidth())/2, 0, null);
    	canvas.drawBitmap(down, (getWidth()-down.getWidth())/2, getHeight()-down.getHeight(), null);
    	canvas.drawBitmap(left, 0, (getHeight()-up.getHeight())/2, null);
    	canvas.drawBitmap(right, getWidth()-right.getWidth(), (getHeight()-up.getHeight())/2, null);    	
    }

    // dessin du gagne si gagne
    private void paintwin(Canvas canvas) {
    	canvas.drawBitmap(win, carteLeftAnchor+ 3*Helper.CARTETILESIZE, carteTopAnchor+ 4*Helper.CARTETILESIZE, null);
    }    
    
    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas) {
    	for (int i=0; i< Helper.CARTEHEIGHT; i++) {
            for (int j=0; j< Helper.CARTEWIDTH; j++) {
                switch (carte[i][j]) {
                    case CST_block:
                    	canvas.drawBitmap(block, carteLeftAnchor+ j*Helper.CARTETILESIZE, carteTopAnchor+ i*Helper.CARTETILESIZE, null);
                    	break;                    
                    case CST_zone:
                    	canvas.drawBitmap(zone[currentStepZone],carteLeftAnchor+ j*Helper.CARTETILESIZE, carteTopAnchor+ i*Helper.CARTETILESIZE, null);
                        break;
                    case CST_vide:
                    	canvas.drawBitmap(vide,carteLeftAnchor+ j*Helper.CARTETILESIZE, carteTopAnchor+ i*Helper.CARTETILESIZE, null);
                        break;
                }
            }
        }
    }

    // dessin de la carte minateur  du jeu
    private void paintcarte_m(Canvas canvas) {
        for (int i=0; i< Helper.CARTEHEIGHT; i++) {
            for (int j=0; j< Helper.CARTEWIDTH; j++) {
                switch (carte_m[i][j]) {
                    case CST_blockm:
                        canvas.drawBitmap(blockm, carte_mLeftAnchor+ j*Helper.CARTETILESIZE_MIN, carte_mTopAnchor+ i*Helper.CARTETILESIZE_MIN, null);
                        break;
                    case CST_videm:
                        canvas.drawBitmap(videm,carte_mLeftAnchor+ j*Helper.CARTETILESIZE_MIN, carte_mTopAnchor+ i*Helper.CARTETILESIZE_MIN, null);
                        break;
                }
            }
        }
    }
    
    // dessin du curseur du joueur
    private void paintPlayer(Canvas canvas) {
    	canvas.drawBitmap(perso,carteLeftAnchor+ xPlayer*Helper.CARTETILESIZE, carteTopAnchor+ yPlayer*Helper.CARTETILESIZE, null);
    }

    // dessin des diamants


    // permet d'identifier si la partie est gagnee (tous les diamants à leur place)
    private boolean isWon() {

        return Helper.isSame(carte,carte_m);
    }
    
    // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau et du joueur des diamants et des fleches)
    private void nDraw(Canvas canvas) {
		canvas.drawRGB(44,44,44);
    	if (isWon()) {
            paintcarte(canvas);
        	paintcarte_m(canvas);
        	paintwin(canvas);        	
        } else {
        	paintcarte(canvas);
            paintcarte_m(canvas);
            paintPlayer(canvas);
            paintarrow(canvas);
        }    	   	
        
    }
    
    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
    }

    
    public void surfaceDestroyed(SurfaceHolder arg0) {
    	in=false;
    }    

    /**
     * run (run du thread cr��)
     * on endort le thread, on modifie le compteur d'animation, on prend la main pour dessiner et on dessine puis on lib�re le canvas
     */
    public void run() {
    	Canvas c = null;
        while (in) {
            try {
                cv_thread.sleep(40);
                currentStepZone = (currentStepZone + 1) % maxStepZone;
                try {
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                	if (c != null) {
                		holder.unlockCanvasAndPost(c);
                    }
                }
            } catch(Exception e) {
            	Log.e("-> RUN <-", "PB DANS RUN");
            }
        }
    }
    
    // verification que nous sommes dans le tableau
    private boolean IsOut(int x, int y) {
        if ((x < 0) || (x > Helper.CARTEWIDTH- 1)) {
            return true;
        }
        if ((y < 0) || (y > Helper.CARTEHEIGHT- 1)) {
            return true;
        }
        return false;
    }

    //controle de la valeur d'une cellule
    private boolean IsCell(int x, int y, int mask) {
        if (carte[y][x] == mask) {
            return true;
        }
        return false;
    }

    // controle si nous avons un diamant dans la case


    // met à jour la position d'un diamant

    // fonction permettant de recuperer les retours clavier

    public boolean onKeyDown(int keyCode, KeyEvent event,int x,int y) {

    	Log.i("-> FCT <-", "onKeyUp: "+ keyCode); 
    	
        int xTmpPlayer	= xPlayer;
        int yTmpPlayer  = yPlayer;
        int xchange 	= 0;
        int ychange 	= 0;
        if (keyCode == KeyEvent.KEYCODE_0) {
        	initparameters();
        }
    	
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
           carte= Helper.depColTop(carte,y);
        	xchange = -1;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            carte= Helper.depColDown(carte,y);
        	xchange = 1;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            carte= Helper.depLineLeft(carte,x);
            ychange = -1;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            carte= Helper.depLineRight(carte,x);
            ychange = 1;
        }

	      /*  xPlayer = x+ xchange;
	        yPlayer = y+ ychange;
	        if (!IsOut(xPlayer, yPlayer)) {
	           int aa= carte[x][y];
                carte[x][y]= carte[xPlayer][yPlayer];
                carte[xPlayer][yPlayer]=aa;
	        }  */
	    return true;   
    }


    // fonction permettant de recuperer les evenements tactiles
    @Override
    public boolean onTouchEvent (MotionEvent event) {
       /* switch(event.getAction())
        {
            case(MotionEvent.ACTION_DOWN):

                break;

            case(MotionEvent.ACTION_UP):

                break;
        }

    	Log.i("-> FCT <-", "onTouchEvent: "+ event.getX());

    	if (event.getY()<50) {
    		onKeyDown(KeyEvent.KEYCODE_DPAD_UP, null);
    	} else if (event.getY()>getHeight()-50) {
    		if (event.getX()>getWidth()-50) {
        		onKeyDown(KeyEvent.KEYCODE_0, null);
        	} else {
        		onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN, null);
        	}
    	} else if (event.getX()<50) {
    		onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
    	} else if (event.getX()>getWidth()-50) {
    		onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
    	} */
        Log.i("TAG", "onTouchEvent: "+event.getAction());
        //getinfo(event);
    	return detector.onTouchEvent(event);
    }


    public void getinfo(MotionEvent event,MotionEvent event1 )
    {
        float x1=0, x2, y1=0, y2, dx, dy;
        String direction;
        Float leftclick=event.getX()-carteLeftAnchor;
        Float topclick=event.getY()-carteTopAnchor;
        Log.i("TTTAG", "getinfo: ");
        if(leftclick>0 && topclick>0){
            Float xx =  leftclick/Helper.CARTETILESIZE;
            Float yy =topclick/Helper.CARTETILESIZE;

            if(xx < Helper.CARTEWIDTH && yy < Helper.CARTEHEIGHT){
                        x1 = event.getX();
                        y1 = event.getY();
                        Log.i("www", "getinfo: bas " );
                       // onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN, null,yy.intValue(),xx.intValue());
                        //Log.i("www", "getinfo: bas "+xx.intValue()+"ggg"+yy.intValue());
                        x2 = event1.getX();
                        y2 = event1.getY();
                        dx = x2 - x1;
                        dy = y2 - y1;
                        Log.i("www", "getinfo: haut " );
                        // Use dx and dy to determine the direction of the move
                        if (Math.abs(dx) > Math.abs(dy)) {
                            if (dx > 0)
                                onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null, yy.intValue(), xx.intValue());

                            else
                                onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null, yy.intValue(), xx.intValue());

                        } else {
                            if (dy > 0)
                                onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN, null, yy.intValue(), xx.intValue());

                            else
                                onKeyDown(KeyEvent.KEYCODE_DPAD_UP, null, yy.intValue(), xx.intValue());

                        }
                }
            else
                Log.i(""," vous avez cliqué a l'exterieur du carree");
        }else{
            Log.i(""," vous avez cliqué a l'exterieur du carree");
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        getinfo( motionEvent ,motionEvent1);
        return true;
    }
}
