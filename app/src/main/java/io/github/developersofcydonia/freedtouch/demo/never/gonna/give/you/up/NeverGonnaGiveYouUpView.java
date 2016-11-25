package io.github.developersofcydonia.freedtouch.demo.never.gonna.give.you.up;

import android.content.Context;
import android.util.AttributeSet;

import com.felipecsl.gifimageview.library.GifImageView;

import java.io.IOException;
import java.io.InputStream;

public class NeverGonnaGiveYouUpView extends GifImageView {

    public NeverGonnaGiveYouUpView(Context context) {
        super(context);
        neverGonnaGiveYouUp();
    }

    public NeverGonnaGiveYouUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        neverGonnaGiveYouUp();
    }

    private void neverGonnaGiveYouUp() {
        try {
            InputStream inputStream = getContext().getAssets().open("never_gonna_give_you_up.gif");
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            setBytes(bytes);
            startAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}