package cs.edu.pacificu.group4boomshineandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import group4Boomshine.BoomshineBall;
import group4Boomshine.BoomshineBalls;
import group4Boomshine.BoomshineModel;
import group4Boomshine.BoomshinePresenter;

public class BoomshineView extends View {

  private final boolean NO_RESTART = false;
  private final boolean RESTART = true;

  private BoomshinePresenter mPresenter;
  private long mSeed;
  private Paint mBackground = new Paint();
  private Paint mBallColor = new Paint();
  private ArrayList<BoomshineBall> mBoomshineBalls;
  private ArrayList<ShapeDrawable> mBalls;

  public BoomshineView (Context context, long seed) {
    super (context);
    mSeed = seed;

    setFocusable(true);
    setFocusableInTouchMode(true);

    mPresenter = new BoomshinePresenter(seed, getHeight(), getWidth());
    mPresenter.newGame();
    mBoomshineBalls = mPresenter.getBoomshineBalls().getBoomshineBalls();
  }

  protected void onDraw (Canvas canvas) {
    mBackground.setColor (getResources().getColor(R.color.cNavy));
    canvas.drawRect(0, 0, getWidth(), getHeight(), mBackground);
    mBoomshineBalls = mPresenter.getBoomshineBalls().getBoomshineBalls();
    for (int i = 0; i < mBoomshineBalls.size(); i++) {
      mBallColor.setColor(Color.RED);
      canvas.drawCircle((float) mBoomshineBalls.get(i).getX(), (float) mBoomshineBalls.get(i).getY(), (float) mBoomshineBalls.get(i).getRadius(), mBallColor);
      mBoomshineBalls.get(i).move();
    }
  }

  public void onWin () {

  }

  public void onLose () {

  }

  public boolean onTouchEvent (MotionEvent motionEvent) {
    return true;
  }
}
