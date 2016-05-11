# FreeDTouch
A 3D Touch API for Android

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FreeDTouch-green.svg?style=true)](https://android-arsenal.com/details/1/3527)

# How do I use it?
build.gradle

```
compile 'io.github.developersofcydonia:freedtouch:1.0.0'
```


MainActivity.java

```java
FreeDTouch.OnForceTouchListener onForceTouchListener = new FreeDTouch.OnForceTouchListener() {
    @Override
    public void onPeek(View popup, View v, MotionEvent e) {
        Log.d(TAG, "onPeek");
        Toast.makeText(MainActivity.this, "PEEK", Toast.LENGTH_SHORT).show();
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(20);
        
        // To find views inside your popup, use popup.findViewById(...).
        // E.g. -> TextView textView = (TextView) popup.findViewById(R.id.textview);
    }
    
    @Override
    public void onPop(View popup, View v, MotionEvent e) {
        Log.d(TAG, "onPop");
        Toast.makeText(MainActivity.this, "POP", Toast.LENGTH_SHORT).show();
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
};

Button button = (Button) findViewById(R.id.button);
View popupContainer = findViewById(R.id.popup_container);

FreeDTouch.setup(button, onForceTouchListner)
        .addPopup(popupContainer, R.layout.popup_example)
        .start();
```

activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Wrap your layout in a RelativeLayout -->
    <RelativeLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">

        <Button
            android:id="@+id/button"
            android:layout_gravity="center"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="PushMe" />

    </RelativeLayout>

    <!-- Create a popup container -->
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/popup_container">
    </FrameLayout>
</RelativeLayout>
```

popup_example.xml

``` xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_margin="72dp"
    android:background="#eee">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:textColor="#212121"
        android:textSize="82sp"
        android:gravity="center"
        android:text="Hey!"/>

</RelativeLayout>
```


