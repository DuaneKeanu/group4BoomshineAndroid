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
  private Paint mNightmareColor = new Paint();
  private ArrayList<BoomshineBall> mBoomshineBalls;
  private ArrayList<Paint> mBallColors = new ArrayList<Paint>();
  private ExpandingBall mBomb;

  private int mHeight;
  private int mWidth;
  private int mNightmare = 0;

  private boolean mbNightmareTrigger = false;
  private boolean mbBombDown;
  private boolean mbBallsHitGone;
  private boolean mbAllBallsGone;
  private boolean mbBallsMaxSize;
  private boolean mbBombMaxSize;
  private boolean mbBombGone;


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
    mbAllBallsGone = false;
    mbBombGone = false;

    if (mode.equals("nightmare")) {
      mPresenter.setNightmareOn();
    }
  }

  protected void onDraw (Canvas canvas) {
    mBackground.setColor (getResources().getColor(R.color.cNavy));
    canvas.drawRect(0, 0, getWidth(), getHeight(), mBackground);
    mNightmareColor.setColor (getResources().getColor(R.color.cNavy));

    if (mPresenter.getMode()) {
      mNightmare++;
      if (!mbBombDown) {
        if (mNightmare % 200 == 0) {
          mbNightmareTrigger = !mbNightmareTrigger;
        }
      }
      else {
        mbNightmareTrigger = false;
      }
    }

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
        if (mPresenter.getMode())
        {
          if (mbNightmareTrigger)
          {
            canvas.drawCircle((float) mBoomshineBalls.get(i).getX(), (float) mBoomshineBalls.get(i).getY(), (float) mBoomshineBalls.get(i).getRadius(), mNightmareColor);
            mBoomshineBalls.get(i).moveAndBounce();
          }
          else
          {
            canvas.drawCircle((float) mBoomshineBalls.get(i).getX(), (float) mBoomshineBalls.get(i).getY(), (float) mBoomshineBalls.get(i).getRadius(), mBallColors.get(i));
            mBoomshineBalls.get(i).moveAndBounce();
          }
        }
        else
        {
          canvas.drawCircle((float) mBoomshineBalls.get(i).getX(), (float) mBoomshineBalls.get(i).getY(), (float) mBoomshineBalls.get(i).getRadius(), mBallColors.get(i));
          mBoomshineBalls.get(i).moveAndBounce();
        }
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

        if (!mbBallsMaxSize)
        {
          mPresenter.hitBalls();
          mPresenter.stopBallsHit();
          mPresenter.expandBalls();
          mbBallsMaxSize = mPresenter.ballsMaxSize();
        }

        if (mbBombMaxSize)
        {
          if (!mPresenter.bombGone())
          {
            mPresenter.shrinkBomb();
            mbBombGone = mPresenter.bombGone();
          }
        }

        if (mbBallsMaxSize)
        {
          if (!mbBallsHitGone)
          {
            mPresenter.shrinkBallsHit();
            mbBallsHitGone = mPresenter.ballsGone();
          }
        }

        if (mbBallsHitGone)
        {
          mPresenter.stopAllBalls();
          mPresenter.shrinkAllBalls ();
        }

        if (mbBombGone)
        {
          if (!mbBallsHitGone && mbBallsMaxSize)
          {
            mPresenter.shrinkBallsHit();
            mbBallsHitGone = mPresenter.ballsGone();
          }
          else if (!mbBallsMaxSize)
          {
            if (!mbAllBallsGone)
            {
              mPresenter.stopAllBalls();
              mPresenter.shrinkAllBalls ();

              mbAllBallsGone = mPresenter.areAllGone();
            }
          }
        }

        if (mbAllBallsGone)
        {
          mbBombDown = false;
          mbBallsHitGone = false;
          mbBallsMaxSize = false;
          mbBallsHitGone = false;
          mbBombMaxSize = false;
          mbAllBallsGone = false;
          mbBombGone = false;

          mPresenter.nextLevel();
        }
      }
    }

    invalidate ();
  }

  public void setColors () {
    for (int i = 0; i < mBoomshineBalls.size(); i++) {
      Random random = new Random();
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
