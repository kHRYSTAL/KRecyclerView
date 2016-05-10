package me.khrystal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * usage: DividerDecoration is a VerticalDividerDecoration
 * author: kHRYSTAL
 * create time: 16/5/10
 * update time:
 * email: 723526676@qq.com
 */
public class DividerDecoration extends RecyclerView.ItemDecoration{

    private int mHeight;

    private int mLPadding;

    private int mRPadding;

    private Paint mPaint;

    private DividerDecoration(int height, int lPadding, int rPadding, int color){
        mHeight = height;
        mLPadding = lPadding;
        mRPadding = rPadding;
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            //child--divider--child--divider...
            int top = child.getBottom();
            int bottom = top + mHeight;
            int left = child.getLeft() + mLPadding;
            int right = child.getRight() - mRPadding;

            c.save();
            c.drawRect(left, top, right, bottom, mPaint);
            c.restore();
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0,0,0,mHeight);
    }

    /**
     * builder for divider decorations
     */
    public static class Builder {

        private Resources mResources;

        private int mHeight;

        private int mLPadding;

        private int mRPadding;

        private int mColor;

        public Builder(Context context) {
            mResources = context.getResources();
            //default config
            mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1f,
                    mResources.getDisplayMetrics());
            mLPadding = 0;
            mRPadding = 0;
            mColor = Color.BLACK;
        }

        public Builder setHeight(float pixels) {
            mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels,
                    mResources.getDisplayMetrics());

            return this;
        }

        public Builder setHeight(@DimenRes int resource){
            mHeight = mResources.getDimensionPixelSize(resource);

            return this;
        }

        public Builder setPadding(float pixels){
            setLeftPadding(pixels);
            setRightPadding(pixels);

            return this;
        }

        public Builder setPadding(@DimenRes int resource){
            setLeftPadding(resource);
            setRightPadding(resource);

            return this;
        }

        public Builder setLeftPadding(float pixels){
            mLPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels,
                    mResources.getDisplayMetrics());

            return this;
        }

        public Builder setRightPadding(float pixels){
            mRPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels,
                    mResources.getDisplayMetrics());

            return this;
        }

        public Builder setLeftPadding(@DimenRes int resource){
            mLPadding = mResources.getDimensionPixelSize(resource);

            return this;
        }

        public Builder setRightPadding(@DimenRes int resource){
            mRPadding = mResources.getDimensionPixelSize(resource);

            return this;
        }

        public Builder setColorResource(@ColorRes int resource){
            setColor(mResources.getColor(resource));

            return this;
        }

        public Builder setColor(@ColorInt int color){
            mColor = color;

            return this;
        }

        /**
         * user this to init DividerDecoration
         * @return
         */
        public DividerDecoration build(){
            return new DividerDecoration(mHeight,mLPadding,mRPadding,mColor);
        }
    }
}
