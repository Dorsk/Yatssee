package fr.yams.julien.game;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mp = MediaPlayer.create(this, R.raw.clozee);
        mp.setLooping(false);
        mp.start();
    }

    public void ButtonOnClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                Intent i = new Intent(MainActivity.this, Game.class);
                mp.stop();
                startActivity(i);
                break;
            case R.id.setting:
                Intent j = new Intent(MainActivity.this, Setting.class);
                mp.stop();
                startActivity(j);
                break;
            case R.id.regles:
                Intent k = new Intent(MainActivity.this, Rules.class);
                mp.stop();
                startActivity(k);
                break;
        }
    }

    @Override
    public void onBackPressed() {
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
    protected void onResume() {
       super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        mp.stop();
        super.onDestroy();
    }

    // Surcharge du bouton retour
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mp.stop();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        return true;
    }

}
