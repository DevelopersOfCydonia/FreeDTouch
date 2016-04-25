package io.github.developersofcydonia.freedtouch;

import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by eliseomartelli on 25/04/16.
 */
public class FreeDTouch implements View.OnTouchListener, GestureDetector.OnGestureListener {


    private OnForceTouchListener mListener;
    private View mView;
    public static final int LONG_PRESS_THRESHOLD = ViewConfiguration.getLongPressTimeout();
    private MotionEvent mLastMotionEvent;
    private float mComputedPressureThreshold;

    GestureDetectorCompat mGestureDetector;

    private final Handler mPeekHandler = new Handler();
    private final Runnable mPeekRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLastMotionEvent.getPressure() <= mComputedPressureThreshold) {
                mListener.onPeek(mView, mLastMotionEvent);
            }
        }
    };

    /**
     *
     * @param v
     * @param listener
     */
    public FreeDTouch(View v, OnForceTouchListener listener) {
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
        mComputedPressureThreshold = getComputedPressureThreshold(event.getPressure(), Sensibility.LOW);
        //TODO: Check for POP
        //TODO: Check for PEEK
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
        mListener.onClick(mView, e);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mPeekHandler.removeCallbacks(mPeekRunnable);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mPeekHandler.removeCallbacks(mPeekRunnable);
        return false;
    }

    public interface OnForceTouchListener {
        void onPeek(View v, MotionEvent e);
        void onPop(View v, MotionEvent e);
        void onClick(View v, MotionEvent e);
        void onCancel(View v, MotionEvent e);
    }

    public class Sensibility {
        public static final int LOW = 30;
    }
}
