/**
 * The Help Activity that displays the game rules
 *
 * @author John Duong & Duane Stokes
 */
package cs.edu.pacificu.group4boomshineandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity
{
  /**
   * Creates a new Activity
   *
   * @param savedInstanceState the saved instance state
   */
  @Override
  protected void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);
    setContentView (R.layout.activity_help);
  }
}
