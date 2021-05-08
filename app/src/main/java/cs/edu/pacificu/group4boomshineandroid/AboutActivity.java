/**
 * The About Activity that displays version and author information
 *
 * @author John Duong & Duane Stokes
 */
package cs.edu.pacificu.group4boomshineandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity
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
    setContentView (R.layout.activity_about);
  }
}
