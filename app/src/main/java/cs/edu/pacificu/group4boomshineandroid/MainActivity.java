package cs.edu.pacificu.group4boomshineandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

  private BoomshineView mBoomshineView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBoomshineView = new BoomshineView(this);
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
}