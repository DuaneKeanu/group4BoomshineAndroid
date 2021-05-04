package cs.edu.pacificu.group4boomshineandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import group4Boomshine.BoomshineModel;
import group4Boomshine.BoomshinePresenter;

public class BoomshineView extends View {

  private final boolean NO_RESTART = false;
  private final boolean RESTART = true;

  private BoomshinePresenter mPresenter;
  private long mSeed;
  private Paint mBackground = new Paint();

  public BoomshineView (Context context, long seed) {
    super (context);
    mSeed = seed;

    setFocusable(true);
    setFocusableInTouchMode(true);

    mPresenter = new BoomshinePresenter(seed, getHeight(), getWidth());
  }

  public void onCreate() {};

  protected void onDraw (Canvas canvas) {
    mBackground.setColor (getResources().getColor(R.color.cNavy));
    canvas.drawRect(0, 0, getWidth(), getHeight(), mBackground);
  }

  public void onWin () {

  }

  public void onLose () {

  }

  public boolean onTouchEvent (MotionEvent motionEvent) {
    return true;
  }
}
