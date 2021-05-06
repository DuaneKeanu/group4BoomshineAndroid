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
import group4Boomshine.BoundedMovingBall;
import group4Boomshine.ExpandingBall;

public class BoomshineView extends View {

  private final boolean NO_RESTART = false;
  private final boolean RESTART = true;
  private final boolean BOMB = true;
  private final boolean NO_BOMB = false;

  private BoomshinePresenter mPresenter;
  private long mSeed;
  private Paint mBackground = new Paint();
  private Paint mBallColor = new Paint();
  private ArrayList<BoomshineBall> mBoomshineBalls;
  private ArrayList<Paint> mBallColors = new ArrayList<Paint>();
  private Canvas mCanvas;
  private ExpandingBall mBomb;

  private int mHeight;
  private int mWidth;

  private boolean mbBombDown;
  private boolean mbBallsHitGone;
  private boolean mbAllBallsGone;
  private boolean mbBallsMaxSize;
  private boolean mbBombMaxSize;


  public BoomshineView (Context context, long seed, String mode) {
    super (context);
    mSeed = seed;

    setFocusable(true);
    setFocusableInTouchMode(true);

    mPresenter = new BoomshinePresenter(seed);
    mbBombDown = false;
    mbBallsHitGone = false;
    mbBallsMaxSize = false;
    mbBallsHitGone = false;
    mbBombMaxSize = false;
  }

  protected void onDraw (Canvas canvas) {
    mCanvas = canvas;
    mBackground.setColor (getResources().getColor(R.color.cNavy));
    canvas.drawRect(0, 0, getWidth(), getHeight(), mBackground);

    if (!mPresenter.gameStatus())
    {
      mHeight = getHeight();
      mWidth = getWidth();

      mPresenter.setHeight (mHeight);
      mPresenter.setWidth (mWidth);

      mPresenter.newGame();
      mBoomshineBalls = mPresenter.getBoomshineBalls().getBoomshineBalls();
      setColors();
    }
    else
    {
      mBoomshineBalls = mPresenter.getBoomshineBalls().getBoomshineBalls();

      for (int i = 0; i < mBoomshineBalls.size(); i++) {
        canvas.drawCircle((float) mBoomshineBalls.get(i).getX(), (float) mBoomshineBalls.get(i).getY(), (float) mBoomshineBalls.get(i).getRadius(), mBallColors.get(i));
        mBoomshineBalls.get(i).moveAndBounce();
      }

      if (mbBombDown)
      {
        mBomb = mPresenter.getBomb();
        canvas.drawCircle((float) mBomb.getX(), (float) mBomb.getY(), (float) mBomb.getRadius(), mBallColor);

        if (!mbBombMaxSize)
        {
          mPresenter.expandBomb();
          mbBombMaxSize = mPresenter.isBombMaxSize();
        }

        if (mbBombMaxSize)
        {
          if (!mbBallsMaxSize)
          {
            mPresenter.hitBalls();
            mPresenter.stopBallsHit();
            mPresenter.expandBalls();
            mbBallsMaxSize = mPresenter.ballsMaxSize();
          }

          if (mbBallsMaxSize)
          {
            if (!mPresenter.bombGone())
            {
              mPresenter.shrinkBomb();
            }

            if (!mbBallsHitGone)
            {
              mPresenter.shrinkBalls();
              mbBallsHitGone = mPresenter.ballsGone ();
            }

            if (mbBallsHitGone)
            {
              mPresenter.stopAllBalls();
              mPresenter.shrinkAllBalls ();
            }
          }
        }
      }
    }

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

  public boolean onTouchEvent (MotionEvent event) {
    if (event.getAction () != MotionEvent.ACTION_DOWN)
    {
      return super.onTouchEvent (event);
    }

    if (!mbBombDown)
    {
      mPresenter.bomb (event.getX(), event.getY ());
      mBallColor.setColor (Color.YELLOW);
      mbBombDown = true;
    }

    return true;
  }
}
