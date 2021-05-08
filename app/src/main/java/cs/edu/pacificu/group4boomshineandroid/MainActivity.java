package cs.edu.pacificu.group4boomshineandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  private BoomshineView mBoomshineView;

  private int mSeed = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    newGame (mSeed, "normal");
    setContentView(mBoomshineView);
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu)
  {
    MenuInflater inflater = getMenuInflater ();
    inflater.inflate (R.menu.menu, menu);
    return true;
  }

  public boolean onMenuItemSelected (MenuItem menuItem)
  {
    switch (menuItem.getItemId ())
    {
      case R.id.menuNewGame:
        Log.d("onOptionsItemSelected", "New Game");
        newGame(mSeed, "normal");
        return true;

      case R.id.menuNightmareMode:
        Log.d("OnOptionsItemSelected", "New Game, Nightmare Mode");
        newGame(mSeed, "nightmare");
        return true;

      case R.id.seedZero:
        Log.d("OnOptionsItemSelected", "Seed Zero");
        mSeed = 0;
        return true;

      case R.id.seedTimeOfDay:
        Log.d("OnOptionsItemSelected", "Seed Time of Day");
        mSeed = Math.abs ((int) System.currentTimeMillis ());
        return true;

      case R.id.about:
        Log.d ("onOptionsItemSelected", "About");
        startActivity (new Intent (this, AboutActivity.class));
        return true;

      case R.id.help:
        Log.d ("onOptionsItemSelected", "Help");
        startActivity (new Intent (this, HelpActivity.class));
        return true;

      default:
        return super.onOptionsItemSelected (menuItem);
    }
  }

  public void newGame (int seed, String mode)
  {
    mBoomshineView = new BoomshineView(this, seed, mode);
    setContentView(mBoomshineView);
  }

  @Override
  public void onConfigurationChanged (Configuration newConfig)
  {
    mBoomshineView.stopMusic();
    newGame (mSeed, "normal");
  }

  @Override
  protected void onStop() {
    mBoomshineView.stopMusic();
    super.onStop();
  }
}