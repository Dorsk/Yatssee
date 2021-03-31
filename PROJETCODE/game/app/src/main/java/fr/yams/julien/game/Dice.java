package fr.yams.julien.game;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by mj003121 on 26/05/2016.
 */
public class Dice extends Activity {

    private int numero;
    private boolean rejouer;

    Dice() {
        numero = 1;
        rejouer = true;
    }

    public int getNumero()
    {
        return numero;
    }
    public void resetRejouer()
    {
        rejouer = true;
    }


    // random
    public void randomm() {
        if (rejouer) {
            Random rand = new Random();
            this.numero = rand.nextInt(6) + 1;
        }
    }

    // set rejouer
    public void setRejouer(ImageView v) {
        if (rejouer) {
            this.rejouer = false;
            setImage(v);
        } else {
            this.rejouer = true;
            setImage(v);
        }
    }

    // Set image
    public void setImage(ImageView v) {
        if (numero == 1) {
            if (rejouer)
                v.setImageResource(R.drawable.dee1);
            else
                v.setImageResource(R.drawable.dee1selected);
        } else if (numero == 2) {
            if (rejouer)
                v.setImageResource(R.drawable.dee2);
            else
                v.setImageResource(R.drawable.dee2selected);
        }else if (numero == 3){
            if (rejouer)
                v.setImageResource(R.drawable.dee3);
            else
                v.setImageResource(R.drawable.dee3selected);
        }
         else if(numero==4){
            if (rejouer)
                v.setImageResource(R.drawable.dee4);
            else
                v.setImageResource(R.drawable.dee4selected);
        }   else if(numero==5) {
            if (rejouer)
                v.setImageResource(R.drawable.dee5);
            else
                v.setImageResource(R.drawable.dee5selected);
        }
        else if(numero==6) {
            if (rejouer)
                v.setImageResource(R.drawable.dee6);
            else
                v.setImageResource(R.drawable.dee6selected);
        }
    }

    // reset image
    public void resetImage(ImageView v) {
      //  v.setImageResource(View.NO_ID);
        v.setImageDrawable(null);
    }

}
