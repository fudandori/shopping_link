package com.fudandori.shoppinglink.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class StrikeTextView extends androidx.appcompat.widget.AppCompatTextView {

    private boolean strike;

    public StrikeTextView(Context context) {
        super(context);
    }

    public StrikeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StrikeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (strike) {
            final Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(3);
            canvas.drawLine(2, 27, getWidth(),17 , paint);
        }
    }

    public void strike(boolean value) {
        strike = value;
    }
}
