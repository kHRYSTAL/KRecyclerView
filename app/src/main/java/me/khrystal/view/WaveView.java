package me.khrystal.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;

/**
 * WaveView
 *
 * @FileName: me.khrystal.widget.WaveView.java
 * @author: kHRYSTAL
 * @email: 723526676@qq.com
 * @date: 2016-02-22 19:19
 */
public class WaveView extends View {

    private PorterDuffXfermode mXfermode;

    private Paint mPaint;

    private Canvas mCanvas;

    private Bitmap mOriginalBitmap;

    private int mOriginalBitmapWidth;

    private int mOriginalBitmapHeight;

    private Bitmap mUltimateBitmap;

    private Path mBezierPath;

    private float mBezierControlX;

    private float mBezierControlY;

    private float mBezierControlOriginalY;

    private float mWaveY;

    private float mWaveOriginalY;

    private boolean mIsBezierControlXIncrease;

    private boolean mIsRefreshing = false;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void initCanvas() {
        mOriginalBitmapWidth = mOriginalBitmap.getWidth();
        mOriginalBitmapHeight = mOriginalBitmap.getHeight();

        mWaveOriginalY = mOriginalBitmapHeight;
        mWaveY = 1.2f * mWaveOriginalY;
        mBezierControlOriginalY = 1.25f * mWaveOriginalY;
        mBezierControlY = mBezierControlOriginalY;

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mBezierPath = new Path();

        mCanvas = new Canvas();
        mUltimateBitmap = Bitmap.createBitmap(mOriginalBitmapWidth, mOriginalBitmapHeight, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mUltimateBitmap);
    }

    /**
     *
     * @param resId
     */
    public void setUltimateColor(@ColorRes int resId) {
        mPaint.setColor(getResources().getColor(resId));
    }

    /**
     *
     * @param resId
     */
    public void setOriginalImage(@DrawableRes int resId) {
        mOriginalBitmap = BitmapFactory.decodeResource(getResources(), resId);
        initCanvas();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mUltimateBitmap == null) {
            return;
        }

        drawUltimateBitmap();
        canvas.drawBitmap(mUltimateBitmap, getPaddingLeft(), getPaddingTop(), null);
        if (mIsRefreshing) {
            invalidate();
        }
    }

    private void drawUltimateBitmap() {
        mBezierPath.reset();
        mUltimateBitmap.eraseColor(Color.parseColor("#00ffffff"));

        if (mBezierControlX >= mOriginalBitmapWidth + 1 / 2 * mOriginalBitmapWidth) {
            mIsBezierControlXIncrease = false;
        } else if (mBezierControlX <= -1 / 2 * mOriginalBitmapWidth) {
            mIsBezierControlXIncrease = true;
        }

        mBezierControlX = mIsBezierControlXIncrease ? mBezierControlX + 10 : mBezierControlX - 10;
        if (mBezierControlY >= 0) {
            mBezierControlY -= 2;
            mWaveY -= 2;
        } else {
            mWaveY = mWaveOriginalY;
            mBezierControlY = mBezierControlOriginalY;
        }

        mBezierPath.moveTo(0, mWaveY);
        mBezierPath.cubicTo(mBezierControlX / 2, mWaveY - (mBezierControlY - mWaveY), (mBezierControlX + mOriginalBitmapWidth) / 2, mBezierControlY, mOriginalBitmapWidth, mWaveY);
        mBezierPath.lineTo(mOriginalBitmapWidth, mOriginalBitmapHeight);
        mBezierPath.lineTo(0, mOriginalBitmapHeight);
        mBezierPath.close();

        mCanvas.drawBitmap(mOriginalBitmap, 0, 0, mPaint);
        mPaint.setXfermode(mXfermode);
        mCanvas.drawPath(mBezierPath, mPaint);
        mPaint.setXfermode(null);
    }

    public void startRefreshing() {
        mIsRefreshing = true;
        reset();
    }

    public void stopRefreshing() {
        mIsRefreshing = false;
        reset();
    }

    private void reset() {
        mWaveY = mWaveOriginalY;
        mBezierControlY = mBezierControlOriginalY;
        mBezierControlX = 0;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize + getPaddingLeft() + getPaddingRight();
        } else {
            width = mOriginalBitmapWidth + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize + getPaddingTop() + getPaddingBottom();
        } else {
            height = mOriginalBitmapHeight + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }

        }
        setMeasuredDimension(width, height);
    }

}
