package cs.edu.pacificu.group4boomshineandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cs.edu.pacificu.group4Boomshine.*;

public class BoomshineView extends View {

  private BoomshinePresenter mPresenter;
  private BoomshineModel mModel;
  private Paint mBackground = new Paint();

  public BoomshineView (Context context) {
    super (context);
    Log.d("BoomshineView", "ConstructorCall");
    setFocusable(true);
    setFocusableInTouchMode(true);
    mPresenter = new BoomshinePresenter(this, mModel);
    mPresenter.newGame();
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
