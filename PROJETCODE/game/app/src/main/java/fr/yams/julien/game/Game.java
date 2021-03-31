package fr.yams.julien.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;


/**
 * Created by dorskk on 07/03/14.
 */
public class Game extends Activity {

    // données du tableau
    final String [] col1 = {"","AS","DEUX","TROIS","QUATRE","CINQ","SIX","Prime (35)","","BRELAN","CARRE","FULL",
            "PETITE SUITE","GRANDE SUITE", "CHANCE","YAHTZEE","TOTAL"};

    final String [] col2 = {"","","","","","","","","","","","",
            "","", "","",""};


    private int    numberRoll   = 0;
    private int[]  tabNumber    = null;
    private Dice[] tabdice      = new Dice[5];
    TextView tabViewJ1 [] = null;
    TextView tabViewJ2 [] = null;
    int nbTourJ1 = 1;
    int nbTourJ2 = 1;
    int totalPrime =0;
    int totalJ2;
    boolean canPlay;
    int tabValeur [][];
    int tabValeurSpeciale [];
    Engine engine;
    Button button;
    TextView annonce;
    TextView totalJoueur;
    TextView prime;
    ImageView v1;
    ImageView v2;
    ImageView v3;
    ImageView v4;
    ImageView v5;
    MediaPlayer mp;

    int numTempo;

    TableLayout table; // on prend le tableau défini dans le layout
    TableRow row; // création d'un élément : ligne
    TextView tv1,tv2,tv3; // création des cellules

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yams);
        final Context context = this;
    //    final SharedPreferences sharedPref = context.getSharedPreferences(
    //            getString(R.string.saved_high_score), Context.MODE_PRIVATE);

        mp = MediaPlayer.create(this, R.raw.clozee);
        mp.setLooping(false);
        mp.start();

        engine = new Engine();
        tabValeur = new int [2][6];
        tabValeurSpeciale = new int [7];
        table =  (TableLayout) findViewById(R.id.idTable);
        tabViewJ1 = new TextView[17];
        tabViewJ2 = new TextView[17];
        totalJ2 = 0;
        totalPrime=0;
        boolean b =true;
        canPlay = false;


        // Création tableau de score
        // pour chaque ligne
        for(int i=0; i < col1.length ; i++) {
            row = new TableRow(this); // création d'une nouvelle ligne

            tv1 = new TextView(this); // création cellule
            tv1.setText(col1[i]); // ajout du texte
            tv1.setGravity(Gravity.CENTER); // centrage dans la cellule
            // adaptation de la largeur de colonne à l'écran : // android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            tv1.setLayoutParams( new TableRow.LayoutParams( 0, 85, 1 ) );
            if (b)
                tv1.setBackgroundColor(getResources().getColor(R.color.grey));



            // idem 2ème cellule
            tabViewJ2[i] = new TextView(this);
            tabViewJ2[i].setText(col2[i]);
            tabViewJ2[i].setGravity(Gravity.CENTER);
            tabViewJ2[i].setLayoutParams( new TableRow.LayoutParams( 0, 85, 1 ) );
            tabViewJ2[i].setId(View.generateViewId());
            if (b) {
                tabViewJ2[i].setBackgroundColor(getResources().getColor(R.color.grey));
                b=false;
            }else
                b=true;

            if (i == 16)
            {
                totalJoueur = tabViewJ2[i];
            }
            if (i == 7)
            {
                prime = tabViewJ2[i];
            }

            // ajout des cellules à la ligne
            row.addView(tv1);
            row.addView(tabViewJ2[i]);

            // ajout de la ligne au tableau + saut de ligne
            table.addView(row);

        }

        //////////////////////////////////////////////////////////
        //         Appel
        //////////////////////////////////////////////////////////
        for(int i=0;i<5;i++)
            tabdice[i] = new Dice();

        button = (Button) findViewById(R.id.roll);
        v1 = (ImageView) findViewById(R.id.dee1);
        v2 = (ImageView) findViewById(R.id.dee2);
        v3 = (ImageView) findViewById(R.id.dee3);
        v4 = (ImageView) findViewById(R.id.dee4);
        v5 = (ImageView) findViewById(R.id.dee5);
        annonce = (TextView) findViewById(R.id.annonce);

        //////////////////////////////////////////////////////////
        //          Button ROLL
        //
        //////////////////////////////////////////////////////////
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 3 essais
                if(nbTourJ2 < 14 && nbTourJ1 < 14) {

                    if (totalPrime > 63 )
                    {
                        prime.setText(String.valueOf(35));
                        totalJ2 = totalJ2 + 35;
                        totalPrime = (-99);
                    }
                    if (numberRoll == 0 && canPlay == false) {
                        resetCellEditable();
                        unlockDice();
                        resetImages();
                        numberRoll = 3;
                        canPlay = true;
                        annonce.setText(getString(R.string.TourNum) + String.valueOf(nbTourJ2) + getString(R.string.lancer1));
                        annonce.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        totalJoueur.setText(String.valueOf(totalJ2));
                    }

                    if (numberRoll == 3 ) {
                        ramdonDice();
                        tabValeur = engine.checkNumber(tabdice);
                        tabValeurSpeciale[0] = engine.checkBrelan(tabdice);
                        tabValeurSpeciale[1] = engine.checkCarre(tabdice);
                        tabValeurSpeciale[2] = engine.checkFull(tabdice);
                        tabValeurSpeciale[3] = engine.checkPetiteSuite(tabdice);
                        tabValeurSpeciale[4] = engine.checkGrandeSuite(tabdice);
                        tabValeurSpeciale[5] = engine.checkYahtzee(tabdice);
                        tabValeurSpeciale[6] = engine.checkChance(tabdice);
                        setImages();
                        setCellEditable();
                        numberRoll--;
                        totalJoueur.setText(String.valueOf(totalJ2));
                    } else if (numberRoll == 2 ) {
                        ramdonDice();
                        tabValeur = engine.checkNumber(tabdice);
                        tabValeurSpeciale[0] = engine.checkBrelan(tabdice);
                        tabValeurSpeciale[1] = engine.checkCarre(tabdice);
                        tabValeurSpeciale[2] = engine.checkFull(tabdice);
                        tabValeurSpeciale[3] = engine.checkPetiteSuite(tabdice);
                        tabValeurSpeciale[4] = engine.checkGrandeSuite(tabdice);
                        tabValeurSpeciale[5] = engine.checkYahtzee(tabdice);
                        tabValeurSpeciale[6] = engine.checkChance(tabdice);
                        setImages();
                        setCellEditable();
                        numberRoll--;
                        totalJoueur.setText(String.valueOf(totalJ2));
                        annonce.setText(getString(R.string.TourNum) + String.valueOf(nbTourJ2) + getString(R.string.lancer2));
                    }
                    else if (numberRoll == 1 ) {
                        ramdonDice();
                        tabValeur = engine.checkNumber(tabdice);
                        tabValeurSpeciale[0] = engine.checkBrelan(tabdice);
                        tabValeurSpeciale[1] = engine.checkCarre(tabdice);
                        tabValeurSpeciale[2] = engine.checkFull(tabdice);
                        tabValeurSpeciale[3] = engine.checkPetiteSuite(tabdice);
                        tabValeurSpeciale[4] = engine.checkGrandeSuite(tabdice);
                        tabValeurSpeciale[5] = engine.checkYahtzee(tabdice);
                        tabValeurSpeciale[6] = engine.checkChance(tabdice);
                        setImages();
                        setCellEditable();
                        totalJoueur.setText(String.valueOf(totalJ2));
                        annonce.setText(getString(R.string.TourNum) + String.valueOf(nbTourJ2) + getString(R.string.lancer1));
                        numberRoll--;
                    }

                    if (numberRoll == 0 && canPlay == true) {
                        annonce.setText(getString(R.string.TourNum) + String.valueOf(nbTourJ2) + getString(R.string.lancer3));
                        annonce.setTextColor(getResources().getColor(R.color.red));
                        totalJoueur.setText(String.valueOf(totalJ2));
                    }

                }
                else
                {
                    annonce.setText(getString(R.string.EndGame) + totalJ2 ) ;
                    annonce.setTextColor(getResources().getColor(R.color.red));
                    totalJoueur.setText(String.valueOf(totalJ2));
                   // readFile(totalJ2,"savehighscore.txt", context);

                }
            }
        });

        // check si le � est rejouer ou non
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabdice[0].setRejouer(v1);
            }
        });
        // check si le d� est rejouer ou non
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabdice[1].setRejouer(v2);
            }
        });
        // check si le d� est rejouer ou non
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabdice[2].setRejouer(v3);
            }
        });
        /////////////////////////////////////////
        // check si le d� est rejouer ou non
        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabdice[3].setRejouer(v4);
            }
        });
        // check si le d� est rejouer ou non
        v5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabdice[4].setRejouer(v5);
            }
        });

        //////////////////////////////////
        // tabViewJ1 set listnner
        /////////////////////////////////
        tabViewJ2[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    if (tabViewJ2[1].getText().equals("")) {
                        if (tabValeur[0][0] > 0) {
                            tabViewJ2[1].setText(String.valueOf(tabValeur[1][0] * tabValeur[0][0]));
                            totalJ2 = totalJ2 + (tabValeur[1][0] * tabValeur[0][0]);
                            totalPrime = totalPrime + (tabValeur[1][0] * tabValeur[0][0]);
                            canPlay =false;
                            numberRoll=0;
                            nbTourJ2++;
                        }
                    }
                }
            }
        });
        tabViewJ2[2].setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if(canPlay) {
                    if (tabViewJ2[2].getText().equals("")) {
                        if (tabValeur[1][1] > 0) {
                            tabViewJ2[2].setText(String.valueOf(tabValeur[1][1] * tabValeur[0][1]));
                            canPlay = false;
                            totalJ2 = totalJ2 +(tabValeur[1][1] * tabValeur[0][1]);
                            totalPrime = totalPrime + (tabValeur[1][1] * tabValeur[0][1]);
                            numberRoll=0;
                            nbTourJ2++;
                        }
                    }
                }
            }
        });
        tabViewJ2[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(canPlay) {
                     if (tabViewJ2[3].getText().equals("")) {
                         if (tabValeur[1][2] > 0) {
                             tabViewJ2[3].setText(String.valueOf(tabValeur[1][2] * tabValeur[0][2]));
                             canPlay = false;
                             totalJ2 = totalJ2 +(tabValeur[1][2] * tabValeur[0][2]);
                             totalPrime = totalPrime +(tabValeur[1][2] * tabValeur[0][2]);
                             numberRoll=0;
                             nbTourJ2++;
                         }
                     }
                 }
            }
        });
        tabViewJ2[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    if (tabViewJ2[4].getText().equals("")) {
                        if (tabValeur[1][3] > 0) {
                            tabViewJ2[4].setText(String.valueOf(tabValeur[1][3] * tabValeur[0][3]));
                            canPlay = false;
                            totalJ2 = totalJ2 +(tabValeur[1][3] * tabValeur[0][3]);
                            totalPrime = totalPrime + (tabValeur[1][3] * tabValeur[0][3]);
                            numberRoll=0;
                            nbTourJ2++;
                        }
                    }
                }
            }
        });
        tabViewJ2[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    if (tabViewJ2[5].getText().equals("")) {
                        if (tabValeur[1][4] > 0) {
                            tabViewJ2[5].setText(String.valueOf(tabValeur[1][4] * tabValeur[0][4]));
                            canPlay = false;
                            totalJ2 = totalJ2 +(tabValeur[1][4] * tabValeur[0][4]);
                            totalPrime = totalPrime + (tabValeur[1][4] * tabValeur[0][4]);
                            numberRoll=0;
                            nbTourJ2++;
                        }
                    }
                }
            }
        });
        tabViewJ2[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    if (tabViewJ2[6].getText().equals("")) {
                        if (tabValeur[1][5] > 0) {
                            tabViewJ2[6].setText(String.valueOf(tabValeur[1][5] * tabValeur[0][5]));
                            canPlay = false;
                            totalJ2 = totalJ2 + (tabValeur[1][5] * tabValeur[0][5]);
                            totalPrime = totalPrime + (tabValeur[1][5] * tabValeur[0][5]);
                            numberRoll=0;
                            nbTourJ2++;
                        }
                    }
                }
            }
        });
        tabViewJ2[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tabViewJ2[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tabViewJ2[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    tabViewJ2[9].setText(String.valueOf(tabValeurSpeciale[0]));
                    canPlay=false;
                    totalJ2 = totalJ2 +tabValeurSpeciale[0];
                    numberRoll=0;
                    nbTourJ2++;
                }
            }
        });
        tabViewJ2[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    tabViewJ2[10].setText(String.valueOf(tabValeurSpeciale[1]));
                    canPlay=false;
                    totalJ2 = totalJ2 + tabValeurSpeciale[1];
                    numberRoll=0;
                    nbTourJ2++;
                }
            }
        });
        tabViewJ2[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    tabViewJ2[11].setText(String.valueOf(tabValeurSpeciale[2]));
                    canPlay=false;
                    totalJ2 = totalJ2 + tabValeurSpeciale[2];
                    numberRoll=0;
                    nbTourJ2++;
                }
            }
        });
        tabViewJ2[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    tabViewJ2[12].setText(String.valueOf(tabValeurSpeciale[3]));
                    canPlay=false;
                    totalJ2 = totalJ2 + tabValeurSpeciale[3];
                    numberRoll=0;
                    nbTourJ2++;
                }
            }
        });
        tabViewJ2[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    tabViewJ2[13].setText(String.valueOf(tabValeurSpeciale[4]));
                    canPlay=false;
                    totalJ2 = totalJ2 + tabValeurSpeciale[4];
                    numberRoll=0;
                    nbTourJ2++;
                }
            }
        });

        tabViewJ2[14].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    tabViewJ2[14].setText(String.valueOf(tabValeurSpeciale[6]));
                    canPlay=false;
                    totalJ2 = totalJ2 + tabValeurSpeciale[6];
                    numberRoll=0;
                    nbTourJ2++;
                }
            }
        });

        tabViewJ2[15].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPlay) {
                    tabViewJ2[15].setText(String.valueOf(tabValeurSpeciale[5]));
                    canPlay=false;
                    totalJ2 = totalJ2 + tabValeurSpeciale[5];
                    numberRoll=0;
                    nbTourJ2++;
                }
            }
        });


    }

    // methode de random
    void ramdonDice()
    {
        tabdice[0].randomm();
        tabdice[1].randomm();
        tabdice[2].randomm();
        tabdice[3].randomm();
        tabdice[4].randomm();
    }

    // methode de random
    void setImages()
    {
        tabdice[0].setImage(v1);
        tabdice[1].setImage(v2);
        tabdice[2].setImage(v3);
        tabdice[3].setImage(v4);
        tabdice[4].setImage(v5);
    }


    void resetImages()
    {
        tabdice[0].resetImage(v1);
        tabdice[1].resetImage(v2);
        tabdice[2].resetImage(v3);
        tabdice[3].resetImage(v4);
        tabdice[4].resetImage(v5);
    }

    void setCellEditable(){

        if (tabViewJ2[1].getText().equals("")) {
            if (tabValeur[1][0] > 0)
                tabViewJ2[1].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[1].setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (tabViewJ2[2].getText().equals("")) {
            if (tabValeur[1][1] > 0)
                tabViewJ2[2].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[2].setBackgroundColor(getResources().getColor(R.color.grey));
        }if (tabViewJ2[3].getText().equals("")) {
            if ( tabValeur[1][2] > 0)
                tabViewJ2[3].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[3].setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (tabViewJ2[4].getText().equals("")) {
            if ( tabValeur[1][3] > 0)
                tabViewJ2[4].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[4].setBackgroundColor(getResources().getColor(R.color.grey));
        }if (tabViewJ2[5].getText().equals("")) {
            if ( tabValeur[1][4] > 0)
                tabViewJ2[5].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[5].setBackgroundColor(getResources().getColor(R.color.white));
        }if (tabViewJ2[6].getText().equals("")) {
            if ( tabValeur[1][5] > 0)
                tabViewJ2[6].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[6].setBackgroundColor(getResources().getColor(R.color.grey));
        }
        // Brelan à Yams
        if (tabViewJ2[9].getText().equals("")) {
            if ( tabValeurSpeciale[0] > 0)
                tabViewJ2[9].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[9].setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (tabViewJ2[10].getText().equals("")) {
            if ( tabValeurSpeciale[1] > 0)
                tabViewJ2[10].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[10].setBackgroundColor(getResources().getColor(R.color.grey));
        }
        if (tabViewJ2[11].getText().equals("")) {
            if ( tabValeurSpeciale[2] > 0)
                tabViewJ2[11].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[11].setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (tabViewJ2[12].getText().equals("")) {
            if ( tabValeurSpeciale[3] > 0)
                tabViewJ2[12].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[12].setBackgroundColor(getResources().getColor(R.color.grey));
        }
        if (tabViewJ2[13].getText().equals("")) {
            if ( tabValeurSpeciale[4] > 0)
                tabViewJ2[13].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[13].setBackgroundColor(getResources().getColor(R.color.white));
        }

        if (tabViewJ2[15].getText().equals("")) {
            if ( tabValeurSpeciale[5] > 0)
                tabViewJ2[15].setBackgroundColor(getResources().getColor(R.color.green));
            else
                tabViewJ2[15].setBackgroundColor(getResources().getColor(R.color.white));
        }


    }

    // reset background IHM
    void resetCellEditable(){

        tabViewJ2[1].setBackgroundColor(getResources().getColor(R.color.white));
        tabViewJ2[2].setBackgroundColor(getResources().getColor(R.color.grey));
        tabViewJ2[3].setBackgroundColor(getResources().getColor(R.color.white));
        tabViewJ2[4].setBackgroundColor(getResources().getColor(R.color.grey));
        tabViewJ2[5].setBackgroundColor(getResources().getColor(R.color.white));
        tabViewJ2[6].setBackgroundColor(getResources().getColor(R.color.grey));

        tabViewJ2[9].setBackgroundColor(getResources().getColor(R.color.white));
        tabViewJ2[10].setBackgroundColor(getResources().getColor(R.color.grey));
        tabViewJ2[11].setBackgroundColor(getResources().getColor(R.color.white));
        tabViewJ2[12].setBackgroundColor(getResources().getColor(R.color.grey));
        tabViewJ2[13].setBackgroundColor(getResources().getColor(R.color.white));
        tabViewJ2[14].setBackgroundColor(getResources().getColor(R.color.grey));
        tabViewJ2[15].setBackgroundColor(getResources().getColor(R.color.white));

    }

    void unlockDice()
    {
        tabdice[0].resetRejouer();
        tabdice[1].resetRejouer();
        tabdice[2].resetRejouer();
        tabdice[3].resetRejouer();
        tabdice[4].resetRejouer();
    }


    void readFile(int totalJ2,String filename, Context context)
    {
        FileInputStream fIn = null;
        InputStreamReader isr = null;

        char[] inputBuffer = new char[255];
        String data = null;

        try{
            fIn = context.openFileInput("config.dat");
            isr = new InputStreamReader(fIn);
            isr.read(inputBuffer);
            data = new String(inputBuffer);
            if (totalJ2 > Integer.parseInt(data))
            {
                WriteSettings(context, data);
            }
        }
        catch (Exception e) {
        }
           /* finally {
                try {
                    isr.close();
                    fIn.close();
                } catch (IOException e) {
                }
            }*/
    }

    public void WriteSettings(Context context, String data){
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;

        try{
            fOut = context.openFileOutput("config.dat",MODE_APPEND);
            osw = new OutputStreamWriter(fOut);
            osw.write(data);
            osw.flush();
            //popup surgissant pour le résultat
        }
        catch (Exception e) {
        }
        finally {
            try {
                osw.close();
                fOut.close();
            } catch (IOException e) {
            }
        }
    }
    @Override
    protected void onPause() {
        mp.stop();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mp.stop();
        super.onStop();
    }



    @Override
    protected void onDestroy() {
        mp.stop();
        super.onDestroy();
    }
}