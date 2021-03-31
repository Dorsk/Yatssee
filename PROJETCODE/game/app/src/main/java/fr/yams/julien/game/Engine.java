package fr.yams.julien.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mj003121 on 14/06/2016.
 */
public class Engine {

    // compte le nombre d'AS à 6
    public int[][] checkNumber(Dice [] dice)
    {
        int ret [][] = new int[2][6];
        for (int i=0;i<6;i++)
        {
            ret[0][i] = i+1;
            ret[1][i] = 0;
        }

        for (int i=0;i<dice.length;i++)
        {
            if(dice[i].getNumero() == 1)
            {
                ret[1][(dice[i].getNumero()-1)] = ret[1][(dice[i].getNumero()-1)] +1;
            }
            else if(dice[i].getNumero() == 2)
            {
                ret[1][(dice[i].getNumero()-1)] = ret[1][(dice[i].getNumero()-1)] +1;
            }
            else if(dice[i].getNumero() == 3)
            {
                ret[1][(dice[i].getNumero()-1)] = ret[1][(dice[i].getNumero()-1)] +1;
            }
            else if(dice[i].getNumero() == 4)
            {
                ret[1][(dice[i].getNumero()-1)] = ret[1][(dice[i].getNumero()-1)] + 1;
            }
            else if(dice[i].getNumero() == 5)
            {
                ret[1][(dice[i].getNumero()-1)] = ret[1][(dice[i].getNumero()-1)] +1;
            }
            else if(dice[i].getNumero() == 6)
            {
                ret[1][(dice[i].getNumero()-1)] = ret[1][(dice[i].getNumero()-1)] +1;
            }
        }
        return ret;
    }


    /*
     *  check s'il y a un Carre
     *  return if OK 30 points
     *         else 0
     */

    public int checkCarre(Dice[] dices) {

        int Sum = 0;
        for( int i = 1; i <= 5; i++ )
        {
            int Count = 0;
            for( int j = 0; j < 5; j++ )
            {
                if( dices[j].getNumero() == i )
                    Count++;

                if( Count == 4 )
                    Sum = 30;
            }
        }
        return Sum;
     }

    /*
     *  check s'il y a un FULL
     *  return if OK 25 points
     *         else 0
     */
    public int checkFull(Dice[] myDice)
    {
        int ret=0;
        int[] i = new int[5];
        i[0] = myDice[0].getNumero();
        i[1] = myDice[1].getNumero();
        i[2] = myDice[2].getNumero();
        i[3] = myDice[3].getNumero();
        i[4] = myDice[4].getNumero();

        Arrays.sort(i);

        if (((i[0] == i[1]) && i[1] == i[2]) && (i[3] == i[4]))
        {
            ret = 25;
        }

        return ret;
    }

    // Check brelan
    public int checkBrelan( Dice[] myDice )
    {
        int Sum = 0;

        boolean ThreeOfAKind = false;

        for( int i = 1; i <= 6; i++ )
        {
            int Count = 0;
            for( int j = 0; j < 5; j++ )
            {
                if( myDice[j].getNumero() == i )
                    Count++;

                if( Count > 2 )
                    ThreeOfAKind = true;
            }
        }

        if( ThreeOfAKind )
        {
            for( int k = 0; k < 5; k++ )
            {
                Sum += myDice[k].getNumero();
            }
        }

        return Sum;
    }

    // check  GrandeSuite
    public int checkGrandeSuite( Dice[] myDice )
    {
        int Sum = 0;
        int[] i = new int[5];
        i[0] = myDice[0].getNumero();
        i[1] = myDice[1].getNumero();
        i[2] = myDice[2].getNumero();
        i[3] = myDice[3].getNumero();
        i[4] = myDice[4].getNumero();

        Arrays.sort(i);

        if( ((i[0] == 1) &&
                (i[1] == 2) &&
                (i[2] == 3) &&
                (i[3] == 4) &&
                (i[4] == 5)) ||
                ((i[0] == 2) &&
                        (i[1] == 3) &&
                        (i[2] == 4) &&
                        (i[3] == 5) &&
                        (i[4] == 6)) )
        {
            Sum = 40;
        }

        return Sum;
    }

    //
    // donne le nombre le plus grand
    public int checkChance( Dice[] myDice ) {
        return  myDice[0].getNumero()+ myDice[1].getNumero()+myDice[2].getNumero()+myDice[3].getNumero()+myDice[4].getNumero();
    }

    // check petite Suite =
    public int checkPetiteSuite( Dice[] myDice )
    {
        int Sum = 0;

        int[] i = new int[5];

        i[0] = myDice[0].getNumero();
        i[1] = myDice[1].getNumero();
        i[2] = myDice[2].getNumero();
        i[3] = myDice[3].getNumero();
        i[4] = myDice[4].getNumero();

        Arrays.sort(i);

        // Problem can arise hear, if there is more than one of the same number, so
        // we must move any doubles to the end
        for( int j = 0; j < 4; j++ )
        {
            int temp = 0;
            if( i[j] == i[j+1] )
            {
                temp = i[j];

                for( int k = j; k < 4; k++ )
                {
                    i[k] = i[k+1];
                }
                i[4] = temp;
            }
        }

        if( ((i[0] == 1) && (i[1] == 2) && (i[2] == 3) && (i[3] == 4)) ||
                ((i[0] == 2) && (i[1] == 3) && (i[2] == 4) && (i[3] == 5)) ||
                ((i[0] == 3) && (i[1] == 4) && (i[2] == 5) && (i[3] == 6)) ||
                ((i[1] == 1) && (i[2] == 2) && (i[3] == 3) && (i[4] == 4)) ||
                ((i[1] == 2) && (i[2] == 3) && (i[3] == 4) && (i[4] == 5)) ||
                ((i[1] == 3) && (i[2] == 4) && (i[3] == 5) && (i[4] == 6)) )
        {
            Sum = 30;
        }
        return Sum;
    }

    public int checkYahtzee( Dice[] myDice )
    {
        int Sum = 0;

        for( int i = 1; i <= 6; i++ )
        {
            int Count = 0;
            for( int j = 0; j < 5; j++ )
            {
                if( myDice[j].getNumero() == i )
                    Count++;

                if( Count == 5 )
                    Sum = 50;
            }
        }

        return Sum;
    }

    // check


}
