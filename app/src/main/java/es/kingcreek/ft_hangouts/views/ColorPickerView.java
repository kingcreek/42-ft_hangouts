package es.kingcreek.ft_hangouts.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View {

    private Paint colorWheelPaint;
    private Paint selectorPaint;
    private OnColorSelectedListener onColorSelectedListener;

    private int selectorColor;
    private float selectorX, selectorY;

    public ColorPickerView(Context context) {
        super(context);
        init();
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        colorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setStyle(Paint.Style.STROKE);
        selectorPaint.setStrokeWidth(5);

        selectorX = getWidth() / 2f;
        selectorY = getHeight() / 2f;

        selectorColor = getColorAtPoint(selectorX, selectorY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);

        int[] colors = generateColorWheel();
        colorWheelPaint.setShader(new SweepGradient(centerX, centerY, colors, null));

        canvas.drawCircle(centerX, centerY, radius, colorWheelPaint);

        selectorPaint.setColor(selectorColor);
        canvas.drawCircle(selectorX, selectorY, 20, selectorPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Update selector position
                selectorX = event.getX();
                selectorY = event.getY();

                // Get color in position
                selectorColor = getColorAtPoint(selectorX, selectorY);

                // Notify changes to selector
                if (onColorSelectedListener != null) {
                    onColorSelectedListener.onColorSelected(selectorColor);
                }

                // Redraw
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private int[] generateColorWheel() {
        int[] colors = new int[360];
        for (int i = 0; i < 360; i++) {
            colors[i] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        return colors;
    }

    private int getColorAtPoint(float x, float y) {
        // Distance from center
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float distance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

        // Angle
        float angle = (float) Math.toDegrees(Math.atan2(y - centerY, x - centerX));
        if (angle < 0) {
            angle += 360;
        }

        // Color based on angle and distance
        float hue = angle;
        float saturation = Math.min(1, distance / (getWidth() / 2f));

        return Color.HSVToColor(new float[]{hue, saturation, 1});
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.onColorSelectedListener = listener;
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
}
