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
import java.util.Random;

import group4Boomshine.BoomshineBall;
import group4Boomshine.BoomshinePresenter;
import group4Boomshine.ExpandingBall;

public class BoomshineView extends View {

  private final boolean NO_RESTART = false;
  private final boolean RESTART = true;

  private BoomshinePresenter mPresenter;
  private long mSeed;
  private Paint mBackground = new Paint();
  private Paint mBallColor = new Paint();
  private ArrayList<BoomshineBall> mBoomshineBalls;
  private ArrayList<Paint> mBallColors = new ArrayList<Paint>();
  private ExpandingBall mBomb = new ExpandingBall(0, 0, 0);
  private Canvas mCanvas;

  public BoomshineView (Context context, long seed, String mode) {
    super (context);
    mSeed = seed;

    setFocusable(true);
    setFocusableInTouchMode(true);

    mPresenter = new BoomshinePresenter(seed);
    mPresenter.setHeight (context.getResources ().getDisplayMetrics ().heightPixels);
    mPresenter.setWidth (context.getResources ().getDisplayMetrics ().widthPixels);
    mPresenter.newGame();
    mBoomshineBalls = mPresenter.getBoomshineBalls().getBoomshineBalls();
    setColors();
  }

  protected void onDraw (Canvas canvas) {
    mCanvas = canvas;
    mBackground.setColor (getResources().getColor(R.color.cNavy));
    canvas.drawRect(0, 0, getWidth(), getHeight(), mBackground);
    mBoomshineBalls = mPresenter.getBoomshineBalls().getBoomshineBalls();
    for (int i = 0; i < mBoomshineBalls.size(); i++) {
      canvas.drawCircle((float) mBoomshineBalls.get(i).getX(), (float) mBoomshineBalls.get(i).getY(), (float) mBoomshineBalls.get(i).getRadius(), mBallColors.get(i));
      mBoomshineBalls.get(i).moveAndBounce();
    }
    canvas.drawCircle((float) mBomb.getX(), (float) mBomb.getY(), (float) mBomb.getRadius(), mBallColor);
    mBomb.expand();

    invalidate ();
  }

  public void setColors () {
    for (int i = 0; i < mBoomshineBalls.size(); i++) {
      Random random = new Random();
      //int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
      //mBallColor.setColor(color);
      mBallColors.add(new Paint());
      mBallColors.get(i).setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
      invalidate();
    }
  }

  public void onWin () {

  }

  public void onLose () {

  }

  public boolean onTouchEvent (MotionEvent motionEvent) {
    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
      int x = (int) motionEvent.getX();
      int y = (int) motionEvent.getY();
      mBomb.setPosition(x, y);
      mBomb.setRadius(20);
      mBallColor.setColor(Color.WHITE);
    }
    return true;
  }
}
