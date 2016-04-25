package io.github.developersofcydonia.freedtouch;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Calendar;

/**
 * Created by eliseomartelli on 25/04/16.
 */
public class FreeDTouch implements View.OnTouchListener {


    private OnForceTouchListener mListener;
    private View mView;
    public static final int LONG_PRESS_THRESHOLD = ViewConfiguration.getLongPressTimeout();

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
        }

    }

    long startTime = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = Calendar.getInstance().getTimeInMillis();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                long duration = Calendar.getInstance().getTimeInMillis() - startTime;
                if (duration < LONG_PRESS_THRESHOLD) {
                    mListener.onClick(v, event);
                } else {
                    mListener.onCancel(v, event);
                }
                break;
        }

        //TODO: Check for POP
        //TODO: Check for PEEK

        return true;
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

    public interface OnForceTouchListener {
        void onPeek(View v, MotionEvent e);
        void onPop(View v, MotionEvent e);
        void onClick(View v, MotionEvent e);
        void onCancel(View v, MotionEvent e);
    }
}
