package io.github.developersofcydonia.freedtouch.demo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.github.developersofcydonia.freedtouch.FreeDTouch;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private static final int POPUP_LAYOUT_RES = R.layout.popup_example;

    private static final String DIALOG_TAG = "dialog";


    Button mButton, mButtonFragment;

    private FreeDTouch.OnForceTouchListener onForceTouchListener = new FreeDTouch.OnForceTouchListener() {
        @Override
        public void onPeek(View popup, View v, MotionEvent e) {
            Log.d(TAG, "onPeek");
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(20);
            if (mButtonFragment.equals(v)) {
                new DialogTestFragment().show(getSupportFragmentManager(), DIALOG_TAG);
            }
        }

        @Override
        public void onPop(View popup, View v, MotionEvent e) {
            Log.d(TAG, "onPop");
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(20);
            onClick(popup, v, e);
            destroyDialog();
        }

        @Override
        public void onClick(View popup, View v, MotionEvent e) {
            Log.d(TAG, "onClick");
            startActivity(new Intent(MainActivity.this, PopActivity.class));
        }

        @Override
        public void onCancel(View popup, View v, MotionEvent e) {
            Log.d(TAG, "onCancel");
            destroyDialog();
        }
    };

    public void destroyDialog() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (fragment instanceof DialogFragment) {
            ((DialogTestFragment) fragment).dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button);
        mButtonFragment = (Button) findViewById(R.id.button_fragment);
        View popupContainer = findViewById(R.id.popup_container);

        FreeDTouch.setup(mButton, onForceTouchListener)
                .addPopup(popupContainer, POPUP_LAYOUT_RES)
                .start();

        FreeDTouch.setup(mButtonFragment, onForceTouchListener).start();
    }
}
