package io.github.developersofcydonia.freedtouch;

import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Made by DeveloperOfCydonia
 * --------------------------
 * Eliseo Martelli
 * Matteo Lobello
 */
public class FreeDTouch implements View.OnTouchListener, GestureDetector.OnGestureListener {


    private OnForceTouchListener mListener;
    private View mView;
    private boolean mIsPeeking = false;
    public static final int LONG_PRESS_THRESHOLD = ViewConfiguration.getLongPressTimeout();
    private MotionEvent mLastMotionEvent;
    private float mComputedPressureThreshold;
    private int mSensibility = Sensibility.MEDIUM;

    GestureDetectorCompat mGestureDetector;

    private final Handler mPeekHandler = new Handler();
    private final Runnable mPeekRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsPeeking && mLastMotionEvent.getPressure() <= mComputedPressureThreshold) {
                mIsPeeking = true;
                mListener.onPeek(mView, mLastMotionEvent);
            }
        }
    };

    public static FreeDTouch add(View v, OnForceTouchListener listener) {
        return new FreeDTouch(v, listener);
    }

    public static FreeDTouch add(View v, OnForceTouchListener listener, int sensibility) {
        return new FreeDTouch(v, listener, sensibility);
    }

    private FreeDTouch(View v, OnForceTouchListener listener, int sensibility) {
        new FreeDTouch(v, listener);
        mSensibility = sensibility;
    }

    private FreeDTouch(View v, OnForceTouchListener listener) {
        if (listener != null) {
            mListener = listener;
        } else {
            mListener = DUMMY_LISTENER;
        }
        if (v != null) {
            mView = v;
            v.setClickable(true);
            v.setOnTouchListener(this);
            mGestureDetector = new GestureDetectorCompat(v.getContext(), this);
        }
    }

    private float getComputedPressureThreshold(float pressure, int sensibility) {
        return ((float)sensibility/100*pressure) + pressure;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mLastMotionEvent = event;
        if (!mIsPeeking) {
            mComputedPressureThreshold =
                    getComputedPressureThreshold(event.getPressure(), mSensibility);
        } else {
            mPeekHandler.removeCallbacks(mPeekRunnable);
            if (event.getPressure() >= mComputedPressureThreshold) {
                mIsPeeking = false;
                mListener.onPop(v, event);
            }

            if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
                mIsPeeking = false;
                mPeekHandler.removeCallbacks(mPeekRunnable);
                mListener.onCancel(mView, event);
            }
        }

        return mGestureDetector.onTouchEvent(event);
    }

    /*
        This dummy listener prevents future checks for listener being null.
     */
    private static final OnForceTouchListener DUMMY_LISTENER = new OnForceTouchListener() {
        @Override
        public void onPeek(View v, MotionEvent e) {}
        @Override
        public void onPop(View v, MotionEvent e) {}
        @Override
        public void onClick(View v, MotionEvent e) {}
        @Override
        public void onCancel(View v, MotionEvent e) {}
    };

    @Override
    public boolean onDown(MotionEvent e) {
        mPeekHandler.removeCallbacks(mPeekRunnable);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        mPeekHandler.removeCallbacks(mPeekRunnable);
        mPeekHandler.postDelayed(mPeekRunnable, LONG_PRESS_THRESHOLD);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mPeekHandler.removeCallbacks(mPeekRunnable);
        if (!mIsPeeking) {
            mListener.onClick(mView, e);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mPeekHandler.removeCallbacks(mPeekRunnable);
        if (!mIsPeeking) {
            return false;
        }
        mIsPeeking = false;
        mListener.onCancel(mView, e1);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mPeekHandler.removeCallbacks(mPeekRunnable);
        if (mIsPeeking) {
            mListener.onCancel(mView, e1);
        }
        return false;
    }

    public interface OnForceTouchListener {
        void onPeek(View v, MotionEvent e);
        void onPop(View v, MotionEvent e);
        void onClick(View v, MotionEvent e);
        void onCancel(View v, MotionEvent e);
    }

    public class Sensibility {
        public static final int HIGH = 10;
        public static final int MEDIUM = 15;
        public static final int LOW = 20;
    }
}
