package io.github.developersofcydonia.freedtouch.demo;

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

    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButton = (Button) findViewById(R.id.button);

        FreeDTouch.add(mButton, this);

    }

    @Override
    public void onPeek(View v, MotionEvent e) {
        Log.d(TAG, "onPeek");
        Toast.makeText(MainActivity.this, "PEEK", Toast.LENGTH_SHORT).show();
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(20);
    }

    @Override
    public void onPop(View v, MotionEvent e) {
        Log.d(TAG, "onPop");
        Toast.makeText(MainActivity.this, "POP", Toast.LENGTH_SHORT).show();
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(20);

    }

    @Override
    public void onClick(View v, MotionEvent e) {
        Log.d(TAG, "onClick");
        Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(View v, MotionEvent e) {
        Log.d(TAG, "onCancel");
        Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
    }
}
