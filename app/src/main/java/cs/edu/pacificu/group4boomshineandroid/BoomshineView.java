package cs.edu.pacificu.group4boomshineandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

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
  private Paint mTextColor = new Paint();
  private Paint mBarColor = new Paint();
  private Paint mLevelColor = new Paint();
  private ArrayList<BoomshineBall> mBoomshineBalls;
  private ArrayList<Paint> mBallColors = new ArrayList<Paint>();
  private ExpandingBall mBomb;

  private int mHeight;
  private int mWidth;
  private int mNightmare = 0;

  private int mToCatch;
  private int mCaught = 0;
  private int mScore;
  private int mLevel;
  private int mHighScore;
  private int mLives;
  private int barHeight = 50;

  private boolean mbNightmareTrigger = false;
  private boolean mbBombDown;
  private boolean mbBallsHitGone;
  private boolean mbAllBallsGone;
  private boolean mbBallsMaxSize;
  private boolean mbBombMaxSize;
  private boolean mbBombGone;
  private boolean mbIsHit;

  private MainActivity mMainActivity;

  private MediaPlayer mMP = MediaPlayer.create (getContext (), R.raw.normal);

  public BoomshineView (Context context, long seed, String mode) {
    super (context);
    mSeed = seed;

    setFocusable(true);
    setFocusableInTouchMode(true);

    this.mMainActivity = (MainActivity) context;

    mPresenter = new BoomshinePresenter(seed);
    mbBombDown = false;
    mbBallsHitGone = false;
    mbBallsMaxSize = false;
    mbBallsHitGone = false;
    mbBombMaxSize = false;
    mbAllBallsGone = false;
    mbBombGone = false;
    mbIsHit = false;

    if (mode.equals("nightmare")) {
      mMP = MediaPlayer.create (getContext (), R.raw.nightmare);
      mMP.start();
      mPresenter.setNightmareOn();
    }
    else
    {
      mMP = MediaPlayer.create (getContext (), R.raw.normal);
      mMP.start();
    }
  }

  protected void onDraw (Canvas canvas) {
    mBackground.setColor (getResources().getColor(R.color.cNavy));
    mTextColor.setColor (getResources().getColor(R.color.cWhite));
    mTextColor.setTextSize(28);
    mLevelColor.setColor(getResources().getColor(R.color.cBlack));
    mLevelColor.setTextSize(40);
    mBarColor.setColor (getResources().getColor(R.color.cPurple));
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
      mHeight = getHeight() - barHeight;
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

        mPresenter.hitBalls();
        mPresenter.stopBallsHit();
        mPresenter.expandBalls();

        if (!mbBallsMaxSize)
        {
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
          else if (mbBallsMaxSize)
          {
            if (!mbAllBallsGone)
            {
              mPresenter.stopAllBalls();
              mPresenter.shrinkAllBalls ();

              mbAllBallsGone = mPresenter.areAllGone();
            }
          }


          if (mbBombGone && !mbIsHit)
          {
            mPresenter.stopAllBalls();
            mPresenter.shrinkAllBalls ();

            mbAllBallsGone = mPresenter.areAllGone();
          }

          if (mbAllBallsGone)
          {
            mPresenter.nextLevel();
            mBoomshineBalls = mPresenter.getBoomshineBalls().getBoomshineBalls();
            setColors();

            mbBombDown = false;
            mbBallsHitGone = false;
            mbBallsMaxSize = false;
            mbBombMaxSize = false;
            mbAllBallsGone = false;
            mbBombGone = false;
          }
        }
        else
        {
          mbIsHit = mPresenter.isHit();
        }
      }
    }

    drawBottomBar(canvas);

    invalidate ();
  }

  public void setColors () {
    mBoomshineBalls = mPresenter.getBoomshineBalls().getBoomshineBalls();

    for (int i = 0; i < mBoomshineBalls.size(); i++) {
      Random random = new Random();
      mBallColors.add(new Paint());
      mBallColors.get(i).setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
      invalidate();
    }
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

  private void drawBottomBar (Canvas canvas) {
    canvas.drawText("Level:", 10, 40, mLevelColor);
    mLevel = mPresenter.getLevel();
    canvas.drawText(Integer.toString(mLevel), 120, 40, mLevelColor);
    canvas.drawText("Lives:", 620, 40, mLevelColor);
    mLives = mPresenter.getNumLives();
    canvas.drawText(Integer.toString(mLives), 730, 40, mLevelColor);

    canvas.drawRect(0, getHeight() - (float) barHeight, getWidth(), getHeight(), mBarColor);
    canvas.drawText("Balls to catch:", 10, getHeight() - 10, mTextColor);
    mToCatch = mPresenter.getLevel();
    canvas.drawText(Integer.toString(mToCatch), 205, getHeight() - 10, mTextColor);
    canvas.drawText("Balls caught:", 240, getHeight() - 10, mTextColor);
    mCaught = mPresenter.numBallsHit();
    canvas.drawText(Integer.toString(mCaught), 420, getHeight() - 10, mTextColor);
    canvas.drawText("Score:", 455, getHeight() - 10, mTextColor);
    mScore = mPresenter.getCurrentScore();
    canvas.drawText(Integer.toString(mScore), 550, getHeight() - 10, mTextColor);
    canvas.drawText("High Score:", 585, getHeight() - 10, mTextColor);
    mHighScore = mPresenter.getHighScore();
    canvas.drawText(Integer.toString(mHighScore), 740, getHeight() - 10, mTextColor);
  }

  public void stopMusic() {
    mMP.stop();
    mMP.release();
  }
}
