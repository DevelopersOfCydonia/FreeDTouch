/*
 * Copyright (C) 2016 DevelopersOfCydonia 
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.developersofcydonia.freedtouch;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class FreeDTouch implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private OnForceTouchListener mListener;
    private View mView;

    /**
     * Wrap your layout in a RelativeLayout, then add a FrameLayout as the
     * last root view child (it'll be your popup container).
     *
     * See https://github.com/DevelopersOfCydonia/FreeDTouch/blob/master/app/src/main/res/layout/activity_main.xml
     */
    private ViewGroup mPopupContainer;
    private View mPopup;
    private int mPopupLayoutRes;

    private boolean mIsPeeking;

    private MotionEvent mLastMotionEvent;

    private float mComputedPressureThreshold;
    private int mSensibility;

    private GestureDetectorCompat mGestureDetector;

    public static final int LONG_PRESS_THRESHOLD = ViewConfiguration.getLongPressTimeout();

    private final Handler mPeekHandler = new Handler();
    private final Runnable mPeekRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsPeeking && mLastMotionEvent.getPressure() <= mComputedPressureThreshold) {
                mIsPeeking = true;
                if (mPopupContainer != null) {
                    mPopup = LayoutInflater.from(mView.getContext())
                            .inflate(mPopupLayoutRes, mPopupContainer);
                    mPopup.setVisibility(View.INVISIBLE);

                    handlePopupVisibility(true);
                }
                mListener.onPeek(mPopup, mView, mLastMotionEvent);
            }
        }
    };

    /**
     * @param view
     * @param listener
     */
    public static FreeDTouch setup(View view, OnForceTouchListener listener) {
        return new FreeDTouch(view, listener, Sensibility.LOW);
    }

    /**
     * @param view
     * @param listener
     * @param sensibility
     */
    public static FreeDTouch setup(View view, OnForceTouchListener listener, int sensibility) {
        return new FreeDTouch(view, listener, sensibility);
    }

    private FreeDTouch(View v, OnForceTouchListener listener) {
        new FreeDTouch(v, listener, Sensibility.LOW);
    }

    private FreeDTouch(View v, OnForceTouchListener listener, int sensibility) {
        mListener = listener != null ? listener : DUMMY_LISTENER;
        mSensibility = sensibility;
        mView = v;
    }

    public void start() {
        if (mView == null) {
            throw new NullPointerException("View is null!");
        }

        mView.setClickable(true);
        mView.setOnTouchListener(this);
        mGestureDetector = new GestureDetectorCompat(mView.getContext(), this);
    }

    public FreeDTouch addPopup(View popupContainer, int layout) {
        this.mPopupContainer = (ViewGroup) popupContainer;
        this.mPopupLayoutRes = layout;

        return this;
    }

    private float getComputedPressureThreshold(float pressure, int sensibility) {
        return ((float) sensibility / 100 * pressure) + pressure;
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
                destroyPopup();

                mListener.onPop(mPopup, v, event);
            }

            if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                mIsPeeking = false;
                destroyPopup();

                mPeekHandler.removeCallbacks(mPeekRunnable);
                mListener.onCancel(mPopup, mView, event);
            }
        }

        return mGestureDetector.onTouchEvent(event);
    }

    private void destroyPopup() {
        if (mPopup != null) {
            handlePopupVisibility(false);
        }
    }

    private void handlePopupVisibility(final boolean in) {
        Animation fade = AnimationUtils.loadAnimation(mView.getContext(), in
                ? android.R.anim.fade_in
                : android.R.anim.fade_out);
        fade.setDuration(350);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (in) {
                    mPopup.setVisibility(View.VISIBLE);
                } else {
                    mPopupContainer.removeAllViews();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        if (mPopup != null) {
            mPopup.startAnimation(fade);
        }
    }

    /*
     * This dummy listener prevents future checks for listener being null.
     */
    private static final OnForceTouchListener DUMMY_LISTENER = new OnForceTouchListener() {
        @Override
        public void onPeek(View popup, View v, MotionEvent e) {}
        @Override
        public void onPop(View popup, View v, MotionEvent e) {}
        @Override
        public void onClick(View popup, View v, MotionEvent e) {}
        @Override
        public void onCancel(View popup, View v, MotionEvent e) {}
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
            mListener.onClick(mPopup, mView, e);
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
        mListener.onCancel(mPopup, mView, e1);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mPeekHandler.removeCallbacks(mPeekRunnable);
        if (mIsPeeking) {
            mListener.onCancel(mPopup, mView, e1);
        }
        return false;
    }

    public interface OnForceTouchListener {
        void onPeek(@Nullable View popup, View v, MotionEvent e);
        void onPop(@Nullable View popup, View v, MotionEvent e);
        void onClick(@Nullable View popup, View v, MotionEvent e);
        void onCancel(@Nullable View popup, View v, MotionEvent e);
    }

    public class Sensibility {
        public static final int HIGH = 10;
        public static final int MEDIUM = 15;
        public static final int LOW = 20;
    }
}
