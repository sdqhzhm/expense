package com.sky.expense.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * Created by sky on 14-2-18.
 */
public class ProgressCircle extends View{

    //Sizes (with defaults)
    private int circleWidth = 14;

    //Colors (with defaults)
    private int backgroundColor = 0x10000000;
    private int circleColor = 0xE04BBCF6;

    //Paints
    private Paint backgroundPaint = new Paint();
    private Paint circlePaint = new Paint();

    //Rectangles
    private RectF circleBounds = new RectF();

    private float progress = 0;
    private float angle = 0;

    public ProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupBounds();
        setupPaints();
        invalidate();
    }

    private void setupPaints() {
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(circleWidth);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleWidth);
    }

    private void setupBounds() {
        circleBounds = new RectF(circleWidth, circleWidth,
                this.getLayoutParams().width - circleWidth, this.getLayoutParams().height - circleWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画背景弧线
        canvas.drawArc(circleBounds, 0 - 90, 360, false, backgroundPaint);

        //画进度弧线
        canvas.drawArc(circleBounds, 0 - 90, angle, false, circlePaint);
    }

    public void refreshProgress(float progress){
        this.progress = progress;
        angle = (float) (progress*3.6);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
