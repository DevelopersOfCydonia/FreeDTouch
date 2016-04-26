package io.github.developersofcydonia.freedtouch.demo;

import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.github.developersofcydonia.freedtouch.FreeDTouch;

public class MainActivity extends AppCompatActivity implements FreeDTouch.OnForceTouchListener {

    private static final String TAG = MainActivity.class.getName();

    private static final int POPUP_LAYOUT_RES = R.layout.popup_example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        View popupContainer = findViewById(R.id.popup_container);

        FreeDTouch.setup(button, this)
                .addPopup(popupContainer, POPUP_LAYOUT_RES)
                .start();
    }

    @Override
    public void onPeek(View popup, View v, MotionEvent e) {
        Log.d(TAG, "onPeek");
        Toast.makeText(MainActivity.this, "PEEK", Toast.LENGTH_SHORT).show();
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(20);
    }

    @Override
    public void onPop(View popup, View v, MotionEvent e) {
        Log.d(TAG, "onPop");
        Toast.makeText(MainActivity.this, "POP", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, PopActivity.class));
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(20);
    }

    @Override
    public void onClick(View popup, View v, MotionEvent e) {
        Log.d(TAG, "onClick");
        Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(View popup, View v, MotionEvent e) {
        Log.d(TAG, "onCancel");
        Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
    }
}
