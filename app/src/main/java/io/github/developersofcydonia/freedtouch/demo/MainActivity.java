package io.github.developersofcydonia.freedtouch.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import io.github.developersofcydonia.freedtouch.FreeDTouch;

public class MainActivity extends AppCompatActivity {

    /**
     * Used for debug proposes
     */
    private static final String TAG = "FreeDTouch";

    /**
     * Set to false to disable logs
     */
    private static final boolean DEBUG = true;

    /**
     * The tag of the dialog
     */
    private static final String DIALOG_TAG = "dialog";

    /**
     * The layout resource of the popup
     */
    private static final int POPUP_LAYOUT_RES = R.layout.popup_example;

    /**
     * The Vibrator service
     */
    private Vibrator mVibrator;

    /**
     * UI Views
     */
    private View mButton;
    private View mButtonFragment;
    private View mPopupContainer;

    private final FreeDTouch.OnForceTouchListener onForceTouchListener = new FreeDTouch.OnForceTouchListener() {
        @Override
        public void onPeek(View popup, View view, MotionEvent e) {
            log("onPeek");

            mVibrator.vibrate(20);

            if (mButtonFragment.equals(view)) {
                new DialogTestFragment().show(getSupportFragmentManager(), DIALOG_TAG);
            }
        }

        @Override
        public void onPop(View popup, View view, MotionEvent e) {
            log("onPop");

            mVibrator.vibrate(20);

            onClick(popup, view, e);

            destroyDialog();
        }

        @Override
        public void onClick(View popup, View view, MotionEvent e) {
            log("onClick");

            startActivity(new Intent(MainActivity.this, PopActivity.class));
            overridePendingTransition(0, 0);
        }

        @Override
        public void onCancel(View popup, View view, MotionEvent e) {
            log("onCancel");

            destroyDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mButton = findViewById(R.id.button);
        mButtonFragment = findViewById(R.id.button_fragment);
        mPopupContainer = findViewById(R.id.popup_container);

        FreeDTouch.setup(mButton, onForceTouchListener)
                .addPopup(mPopupContainer, POPUP_LAYOUT_RES)
                .start();

        FreeDTouch.setup(mButtonFragment, onForceTouchListener).start();
    }

    private void destroyDialog() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (fragment instanceof DialogFragment) {
            ((DialogTestFragment) fragment).dismiss();
        }
    }

    private void log(Object whatToLog) {
        if (DEBUG) {
            Log.d(TAG, " *** " + whatToLog);
        }
    }
}
