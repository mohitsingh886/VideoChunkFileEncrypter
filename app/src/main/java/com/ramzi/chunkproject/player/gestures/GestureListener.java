package com.ramzi.chunkproject.player.gestures;

import android.content.Context;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.ramzi.chunkproject.R;

import static com.ramzi.chunkproject.BuildConfig.DEBUG;
import static com.ramzi.chunkproject.ChunkMainActivity.TAG;
import static com.ramzi.chunkproject.player.animation.AnimationUtils.Type.SCALE_AND_ALPHA;
import static com.ramzi.chunkproject.player.animation.AnimationUtils.animateView;

/**
 * Created by Brajendr on 1/26/2017.
 */

abstract public class GestureListener implements View.OnTouchListener,IGestureListener {

  private final GestureDetector gestureDetector;
  public static final int ONE_FINGER = 1;

  private static final int MOVEMENT_THRESHOLD = 40;

//        private final boolean isVolumeGestureEnabled = PlayerHelper.isVolumeGestureEnabled(getApplicationContext());
//        private final boolean isBrightnessGestureEnabled = PlayerHelper.isBrightnessGestureEnabled(getApplicationContext());

  private final boolean isVolumeGestureEnabled = true;
  private final boolean isBrightnessGestureEnabled = true;
  private boolean isMoving;

  View rootview;
  public GestureListener(Context ctx,View rootview) {
    gestureDetector = new GestureDetector(ctx, new MyGestureListener());
    this.rootview=rootview;
  }

  public boolean onTouch(final View view, final MotionEvent motionEvent) {
       gestureDetector.onTouchEvent(motionEvent);
      if (motionEvent.getAction() == MotionEvent.ACTION_UP && isMoving) {
          isMoving = false;
          onScrollEnd();
      }
      return true;
  }

  private final class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    @Override
    public boolean onDown(MotionEvent e) {


      return true;
    }

    @Override
    public boolean onScroll(MotionEvent initialEvent, MotionEvent movingEvent, float distanceX, float distanceY) {
      float deltaY = movingEvent.getY() - initialEvent.getY();
      float deltaX = movingEvent.getX() - initialEvent.getX();

      if (Math.abs(deltaX) > Math.abs(deltaY)) {
        if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
          onHorizontalScroll(movingEvent, deltaX);
        }
      }


      if (!isVolumeGestureEnabled && !isBrightnessGestureEnabled) return false;

      //noinspection PointlessBooleanExpression
      if (DEBUG && false) Log.d(TAG, "MainVideoPlayer.onScroll = " +
              ", e1.getRaw = [" + initialEvent.getRawX() + ", " + initialEvent.getRawY() + "]" +
              ", e2.getRaw = [" + movingEvent.getRawX() + ", " + movingEvent.getRawY() + "]" +
              ", distanceXy = [" + distanceX + ", " + distanceY + "]");

      final boolean insideThreshold = Math.abs(movingEvent.getY() - initialEvent.getY()) <= MOVEMENT_THRESHOLD;
          /*  if (!isMoving && (insideThreshold || Math.abs(distanceX) > Math.abs(distanceY))
                    || playerImpl.getCurrentState() == BasePlayer.STATE_COMPLETED) {
                return false;
            }
*/
      if (!isMoving && (insideThreshold || Math.abs(distanceX) > Math.abs(distanceY))
              || false) {
        return false;
      }

      isMoving = true;

      boolean acceptAnyArea = isVolumeGestureEnabled != isBrightnessGestureEnabled;
      boolean acceptVolumeArea = acceptAnyArea || initialEvent.getX() > rootview.getWidth() / 2;
      boolean acceptBrightnessArea = acceptAnyArea || !acceptVolumeArea;

      if (isVolumeGestureEnabled && acceptVolumeArea) {
                /*playerImpl.getVolumeProgressBar().incrementProgressBy((int) distanceY);
                float currentProgressPercent =
                        (float) playerImpl.getVolumeProgressBar().getProgress() / playerImpl.getMaxGestureLength();
                int currentVolume = (int) (maxVolume * currentProgressPercent);
                playerImpl.getAudioReactor().setVolume(currentVolume);

                if (DEBUG) Log.d(TAG, "onScroll().volumeControl, currentVolume = " + currentVolume);

                final int resId =
                        currentProgressPercent <= 0 ? R.drawable.ic_volume_off_white_72dp
                                : currentProgressPercent < 0.25 ? R.drawable.ic_volume_mute_white_72dp
                                : currentProgressPercent < 0.75 ? R.drawable.ic_volume_down_white_72dp
                                : R.drawable.ic_volume_up_white_72dp;

                playerImpl.getVolumeImageView().setImageDrawable(
                        AppCompatResources.getDrawable(getApplicationContext(), resId)
                );

                if (playerImpl.getVolumeRelativeLayout().getVisibility() != View.VISIBLE) {
                    animateView(playerImpl.getVolumeRelativeLayout(), SCALE_AND_ALPHA, true, 200);
                }
                if (playerImpl.getBrightnessRelativeLayout().getVisibility() == View.VISIBLE) {
                    playerImpl.getBrightnessRelativeLayout().setVisibility(View.GONE);
                }*/
                Log.d("koppikoooooo",(int) distanceY+">>>>>");
                volume((int) distanceY);
      } else if (isBrightnessGestureEnabled && acceptBrightnessArea) {
//        brPG.incrementProgressBy((int) distanceY);
        Log.d("koppikoooooZZZZo",(int) distanceY+">>>>>");

        brightness((int) distanceY);

//      } else {
//        if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
//          onVerticalScroll(e2,deltaY);
//        }
//      }
        return false;
      }
      return true;

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
      onTap();
      return false;
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
      // Fling event occurred.  Notification of this one happens after an "up" event.
      if(e2.getAction()==MotionEvent.ACTION_DOWN)
      {
        float diffY = e2.getY() - e1.getY();
        Log.d("tendiz","TOUCH DOWN>>>"+diffY);

      }
      boolean result = false;
      try {
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
          if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffX > 0) {
              onSwipeRight();
            } else {
              onSwipeLeft();
            }
          }
          result = true;
        } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
          if (diffY > 0) {
            onSwipeBottom();
          } else {
            onSwipeTop();
          }
        }
        result = true;

      } catch (Exception exception) {
        exception.printStackTrace();
      }
      return result;
    }
  }

}