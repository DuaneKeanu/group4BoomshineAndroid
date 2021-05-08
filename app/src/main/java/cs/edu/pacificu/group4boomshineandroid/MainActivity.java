/**
 * Main Activity that launches the boomshine game and handles menu
 * events
 *
 * @author John Duong & Duane Stokes
 */
package cs.edu.pacificu.group4boomshineandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{

  private BoomshineView mBoomshineView;

  private int mSeed;
  private String mMode;

  /**
   * Creates a new Boomshine game by calling newGame and sets the
   * contenet view to the Boomshine View
   *
   * @param savedInstanceState the saved instance state
   */
  @Override
  protected void onCreate (Bundle savedInstanceState)
  {
    mSeed = 0;
    mMode = "normal";
    super.onCreate (savedInstanceState);
    newGame (mSeed, mMode);
    setContentView (mBoomshineView);
  }

  /**
   * Adds the menu to the screen
   *
   * @param menu the menu to be added
   */
  @Override
  public boolean onCreateOptionsMenu (Menu menu)
  {
    MenuInflater inflater = getMenuInflater ();
    inflater.inflate (R.menu.menu, menu);
    return true;
  }

  /**
   * Handles the selection of a menu item
   *
   * @param menuItem the menu item that was selected
   */
  public boolean onMenuItemSelected (MenuItem menuItem)
  {
    switch (menuItem.getItemId ())
    {
      case R.id.menuNewGame:
        Log.d ("onOptionsItemSelected", "New Game");
        mBoomshineView.stopMusic ();
        mMode = "normal";
        newGame (mSeed, mMode);
        return true;

      case R.id.menuNightmareMode:
        Log.d ("OnOptionsItemSelected", "New Game, Nightmare Mode");
        mBoomshineView.stopMusic ();
        mMode = "nightmare";
        newGame (mSeed, mMode);
        return true;

      case R.id.seedZero:
        Log.d ("OnOptionsItemSelected", "Seed Zero");
        mBoomshineView.stopMusic ();
        mSeed = 0;
        newGame (mSeed, mMode);
        return true;

      case R.id.seedTimeOfDay:
        Log.d ("OnOptionsItemSelected", "Seed Time of Day");
        mBoomshineView.stopMusic ();
        mSeed = Math.abs ((int) System.currentTimeMillis ());
        newGame (mSeed, mMode);
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

  /**
   * Creates a new Boomshine game
   *
   * @param seed the seed for the game
   * @param mode the mode the user wants to play
   */
  public void newGame (int seed, String mode)
  {
    mBoomshineView = new BoomshineView (this, seed, mode);
    setContentView (mBoomshineView);
  }

  /**
   * Starts a new game if the orientation of the phone is changed
   *
   * @param newConfig the new orientation of the phone
   */
  @Override
  public void onConfigurationChanged (Configuration newConfig)
  {
    mBoomshineView.stopMusic ();
    newGame (mSeed, mMode);
  }

  /**
   * Stops the music that is playing
   */
  @Override
  protected void onStop ()
  {
    mBoomshineView.stopMusic ();
    super.onStop ();
  }
}