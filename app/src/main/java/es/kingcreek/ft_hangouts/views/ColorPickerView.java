package es.kingcreek.ft_hangouts.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

        // Selector inicial en el centro
        selectorX = getWidth() / 2f;
        selectorY = getHeight() / 2f;

        // Color inicial
        selectorColor = getColorAtPoint(selectorX, selectorY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);

        // Dibujar el círculo de colores
        colorWheelPaint.setStyle(Paint.Style.STROKE);
        colorWheelPaint.setStrokeWidth(20);
        colorWheelPaint.setColor(Color.RED);
        canvas.drawCircle(centerX, centerY, radius, colorWheelPaint);

        // Dibujar el selector
        selectorPaint.setColor(selectorColor);
        canvas.drawCircle(selectorX, selectorY, 20, selectorPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Actualizar la posición del selector
                selectorX = event.getX();
                selectorY = event.getY();

                // Obtener el color en la posición actual del selector
                selectorColor = getColorAtPoint(selectorX, selectorY);

                // Notificar cambios de color
                if (onColorSelectedListener != null) {
                    onColorSelectedListener.onColorSelected(selectorColor);
                }

                // Redibujar la vista
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private int getColorAtPoint(float x, float y) {
        // Calcular la distancia desde el centro
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float distance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

        // Calcular el ángulo
        float angle = (float) Math.toDegrees(Math.atan2(y - centerY, x - centerX));
        if (angle < 0) {
            angle += 360;
        }

        // Calcular el valor de color basado en el ángulo y la distancia
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
