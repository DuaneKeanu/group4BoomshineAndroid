/**
 * View for our Boomshine game
 *
 * @author John Duong & Duane Stokes
 */
package cs.edu.pacificu.group4boomshineandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import group4Boomshine.BoomshineBall;
import group4Boomshine.BoomshinePresenter;
import group4Boomshine.ExpandingBall;

public class BoomshineView extends View
{
  private BoomshinePresenter mPresenter;
  private long mSeed;
  private Paint mBackground = new Paint ();
  private Paint mBallColor = new Paint ();
  private Paint mNightmareColor = new Paint ();
  private Paint mTextColor = new Paint ();
  private Paint mBarColor = new Paint ();
  private Paint mLevelColor = new Paint ();
  private ArrayList<BoomshineBall> mBoomshineBalls;
  private ArrayList<Paint> mBallColors = new ArrayList<Paint> ();
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

  private MediaPlayer mMP = MediaPlayer.create (getContext (),
      R.raw.normal);
  /**
   * Constructor that initializes all member variables
   *
   * @param context the context
   * @param seed the seed
   * @param mode the mode of the game
   */
  public BoomshineView (Context context, long seed, String mode)
  {
    super (context);
    mSeed = seed;

    setFocusable (true);
    setFocusableInTouchMode (true);

    this.mMainActivity = (MainActivity) context;

    mPresenter = new BoomshinePresenter (seed);
    mbBombDown = false;
    mbBallsHitGone = false;
    mbBallsMaxSize = false;
    mbBallsHitGone = false;
    mbBombMaxSize = false;
    mbAllBallsGone = false;
    mbBombGone = false;
    mbIsHit = false;

    if (mode.equals ("nightmare"))
    {
      mMP = MediaPlayer.create (getContext (), R.raw.nightmare);
      mMP.start ();
      mPresenter.setNightmareOn ();
    }
    else
    {
      mMP = MediaPlayer.create (getContext (), R.raw.normal);
      mMP.start ();
    }
  }
  /**
   * On draw will draw all of the balls and bomb and expands the bombs/balls accordingly
   *
   * @param canvas the canvas
   */
  protected void onDraw (Canvas canvas)
  {
    mBackground.setColor (getResources ().getColor (R.color.cNavy));
    mTextColor.setColor (getResources ().getColor (R.color.cWhite));
    mTextColor.setTextSize (28);
    mLevelColor.setColor (getResources ().getColor (R.color.cBlack));
    mLevelColor.setTextSize (40);
    mBarColor.setColor (getResources ().getColor (R.color.cPurple));
    canvas.drawRect (0, 0, getWidth (), getHeight (), mBackground);
    mNightmareColor.setColor (getResources ().getColor
        (R.color.cNavy));

    if (mPresenter.getMode ())
    {
      mNightmare++;
      if (!mbBombDown)
      {
        if (mNightmare % 200 == 0)
        {
          mbNightmareTrigger = !mbNightmareTrigger;
        }
      }
      else
      {
        mbNightmareTrigger = false;
      }
    }

    if (!mPresenter.gameStatus ())
    {
      mHeight = getHeight () - barHeight;
      mWidth = getWidth ();

      mPresenter.setHeight (mHeight);
      mPresenter.setWidth (mWidth);

      mPresenter.newGame ();
      mBoomshineBalls = mPresenter.getBoomshineBalls ().
          getBoomshineBalls ();
      setColors ();
    }
    else
    {
      mBoomshineBalls = mPresenter.getBoomshineBalls ().
          getBoomshineBalls ();

      for (int i = 0; i < mBoomshineBalls.size (); i++)
      {
        if (mPresenter.getMode ())
        {
          if (mbNightmareTrigger)
          {
            canvas.drawCircle ((float) mBoomshineBalls.get (i).getX (),
                (float) mBoomshineBalls.get (i).getY (), (float)
                    mBoomshineBalls.get (i).getRadius (),
                mNightmareColor);
            mBoomshineBalls.get (i).moveAndBounce ();
          }
          else
          {
            canvas.drawCircle ((float) mBoomshineBalls.get (i).getX (),
                (float) mBoomshineBalls.get (i).getY (), (float)
                    mBoomshineBalls.get (i).getRadius (), mBallColors.
                    get (i));
            mBoomshineBalls.get (i).moveAndBounce ();
          }
        }
        else
        {
          canvas.drawCircle ((float) mBoomshineBalls.get (i).getX (),
              (float) mBoomshineBalls.get (i).getY (), (float)
                  mBoomshineBalls.get (i).getRadius (), mBallColors.
                  get (i));
          mBoomshineBalls.get (i).moveAndBounce ();
        }
      }

      if (mbBombDown)
      {
        mBomb = mPresenter.getBomb ();
        canvas.drawCircle ((float) mBomb.getX (), (float)
            mBomb.getY (), (float) mBomb.getRadius (),
            mBallColor);

        if (!mbBombMaxSize)
        {
          mPresenter.expandBomb ();
          mbBombMaxSize = mPresenter.isBombMaxSize ();
        }

        mPresenter.hitBalls ();
        mPresenter.stopBallsHit ();
        mPresenter.expandBalls ();

        if (!mbBallsMaxSize)
        {
          mbBallsMaxSize = mPresenter.ballsMaxSize ();
        }

        if (mbBombMaxSize)
        {
          if (!mPresenter.bombGone ())
          {
            mPresenter.shrinkBomb ();
            mbBombGone = mPresenter.bombGone ();
          }
        }

        if (mbBallsMaxSize)
        {
          if (!mbBallsHitGone)
          {
            mPresenter.shrinkBallsHit ();
            mbBallsHitGone = mPresenter.ballsGone ();
          }
        }

        if (mbBallsHitGone)
        {
          mPresenter.stopAllBalls ();
          mPresenter.shrinkAllBalls ();
        }

        if (mbBombGone)
        {
          if (!mbBallsHitGone && mbBallsMaxSize)
          {
            mPresenter.shrinkBallsHit ();
            mbBallsHitGone = mPresenter.ballsGone ();
          }
          else if (mbBallsMaxSize)
          {
            if (!mbAllBallsGone)
            {
              mPresenter.stopAllBalls ();
              mPresenter.shrinkAllBalls ();

              mbAllBallsGone = mPresenter.areAllGone ();
            }
          }


          if (mbBombGone && !mbIsHit)
          {
            mPresenter.stopAllBalls ();
            mPresenter.shrinkAllBalls ();

            mbAllBallsGone = mPresenter.areAllGone ();
          }

          if (mbAllBallsGone)
          {
            mPresenter.nextLevel ();
            mBoomshineBalls = mPresenter.getBoomshineBalls ().
                getBoomshineBalls ();
            setColors ();

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
          mbIsHit = mPresenter.isHit ();
        }
      }
    }

    drawBottomBar (canvas);

    invalidate ();
  }
  /**
   * sets random colors for the balls
   */
  public void setColors ()
  {
    mBoomshineBalls = mPresenter.getBoomshineBalls ().
        getBoomshineBalls ();

    for (int i = 0; i < mBoomshineBalls.size (); i++) {
      Random random = new Random ();
      mBallColors.add (new Paint ());
      mBallColors.get (i).setARGB (255, random.nextInt (256),
          random.nextInt (256), random.nextInt (256));
      invalidate ();
    }
  }
  /**
   * Registers on touch events, places bomb where screen is touched
   */
  public boolean onTouchEvent (MotionEvent event)
  {
    if (event.getAction () != MotionEvent.ACTION_DOWN)
    {
      return super.onTouchEvent (event);
    }

    if (!mbBombDown)
    {
      mPresenter.bomb (event.getX (), event.getY ());
      mBallColor.setColor (Color.YELLOW);
      mbBombDown = true;
    }

    return true;
  }
  /**
   * Draws the bottom bar
   *
   * @param canvas the canvas
   */
  private void drawBottomBar (Canvas canvas)
  {
    canvas.drawText ("Level:", 10, (float) (getHeight () * .05),
        mLevelColor);
    mLevel = mPresenter.getLevel ();
    canvas.drawText (Integer.toString (mLevel), (float)
        (getWidth () * .16), (float) (getHeight () * .05),
        mLevelColor);
    canvas.drawText ("Lives:", (float) (getWidth () * .8), (float)
        (getHeight () * .05), mLevelColor);
    mLives = mPresenter.getNumLives ();
    canvas.drawText (Integer.toString (mLives), (float)
        (getWidth () * .95), (float) (getHeight () * 0.05),
        mLevelColor);

    canvas.drawRect (0, getHeight () - (float) barHeight, getWidth (),
        getHeight (), mBarColor);
    canvas.drawText ("Catch:", 10, (float)
        (getHeight () * 0.99), mTextColor);
    mToCatch = mPresenter.getLevel ();
    canvas.drawText (Integer.toString (mToCatch), (float)
        (getWidth () * .13), (float) (getHeight () * 0.99),
        mTextColor);
    canvas.drawText ("Caught:", (float) (getWidth () * .25), (float)
        (getHeight () * 0.99), mTextColor);
    mCaught = mPresenter.numBallsHit ();
    canvas.drawText (Integer.toString (mCaught), (float)
        (getWidth () * .39), (float) (getHeight () * 0.99),
        mTextColor);
    canvas.drawText ("Score:", (float) (getWidth () * .5), (float)
        (getHeight () * 0.99), mTextColor);
    mScore = mPresenter.getCurrentScore ();
    canvas.drawText (Integer.toString (mScore), (float)
        (getWidth () * .62), (float) (getHeight () * 0.99),
        mTextColor);
    canvas.drawText ("High Score:", (float) (getWidth () * .72),
        (float) (getHeight () * 0.99), mTextColor);
    mHighScore = mPresenter.getHighScore();
    canvas.drawText (Integer.toString (mHighScore), (float)
        (getWidth () * .92), (float) (getHeight () * 0.99),
        mTextColor);
  }
  /**
   * Stops the media player then releases it
   */
  public void stopMusic ()
  {
    mMP.stop ();
    mMP.release ();
  }
}
