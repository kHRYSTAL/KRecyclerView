package me.khrystal.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import me.khrystal.listener.StickyHeaderAdapter;

/**
 * usage: StickyHeaderDecoration
 * author: kHRYSTAL
 * create time: 16/5/10
 * update time:
 * email: 723526676@qq.com
 */
public class StickyHeaderDecoration extends RecyclerView.ItemDecoration{

    private Map<Long, RecyclerView.ViewHolder> mHeaderCache;

    private StickyHeaderAdapter mAdapter;

    private boolean mRenderInline;

    public StickyHeaderDecoration(StickyHeaderAdapter adapter){
        this(adapter, false);
    }

    public StickyHeaderDecoration(StickyHeaderAdapter adapter, boolean renderInline){
        mAdapter = adapter;
        mHeaderCache = new HashMap<>();
        mRenderInline = renderInline;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);

        int headerHeight = 0;
        if(position != RecyclerView.NO_POSITION && hasHeader(position)){
            View header = getHeader(parent, position).itemView;
            headerHeight = getHeaderHeightForLayout(header);
        }

        outRect.set(0, headerHeight, 0, 0);
    }

    public void clearHeaderCache(){
        mHeaderCache.clear();
    }

    public View finderHeaderViewUnder(float x, float y){
        for(RecyclerView.ViewHolder holder : mHeaderCache.values()){
            View child = holder.itemView;
            float translationX = ViewCompat.getTranslationX(child);
            float translationY = ViewCompat.getTranslationY(child);

            if (x >= child.getLeft() + translationX &&
                    x <= child.getRight() + translationX &&
                    y >= child.getTop() + translationY &&
                    y <= child.getBottom() + translationY){
                return child;
            }
        }

        return null;
    }

    private boolean hasHeader(int position){
        if (position == 0){
            return true;
        }

        int previous = position - 1;
        return mAdapter.getHeaderId(position) != mAdapter.getHeaderId(previous);
    }

    private RecyclerView.ViewHolder getHeader(RecyclerView parent, int position){
        final long key = mAdapter.getHeaderId(position);

        if (mHeaderCache.containsKey(key)){
            return mHeaderCache.get(key);
        } else {
            final RecyclerView.ViewHolder holder = mAdapter.onCreateHeaderViewHolder(parent);
            final View header = holder.itemView;

            mAdapter.onBindHeaderViewHolder(holder, position);
            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(),
                    View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(),
                    View.MeasureSpec.UNSPECIFIED);

            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(),
                    header.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(),
                    header.getLayoutParams().height);
            header.measure(childWidth, childHeight);
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());

            mHeaderCache.put(key,holder);

            return holder;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final  int count = parent.getChildCount();

        for(int layoutPos = 0; layoutPos < count; layoutPos++){
            final View child = parent.getChildAt(layoutPos);

            final int adapterPos = parent.getChildAdapterPosition(child);

            if (adapterPos != RecyclerView.NO_POSITION &&
                    (layoutPos == 0 || hasHeader(adapterPos))){
                View header = getHeader(parent,adapterPos).itemView;
                c.save();
                final int left = child.getLeft();
                final int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);
                c.translate(left, top);
                ViewCompat.setTranslationX(header, left);
                ViewCompat.setTranslationY(header, top);
                header.draw(c);
                c.restore();
            }
        }
    }

    private int getHeaderTop(RecyclerView parent, View child, View header, int adapterPos, int layoutPos){

        int headerHeight = getHeaderHeightForLayout(header);
        int top = ((int)child.getY()) - headerHeight;
        if (layoutPos == 0){
            final int count = parent.getChildCount();
            final long currentId = mAdapter.getHeaderId(adapterPos);

            for (int i = 1; i < count; i++) {
                int adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i));
                if (adapterPosHere != RecyclerView.NO_POSITION){
                    long nextId = mAdapter.getHeaderId(adapterPosHere);
                    if (nextId != currentId){
                        final View next = parent.getChildAt(i);
                        final int offset = ((int)next.getY() -
                                (headerHeight + getHeader(parent,adapterPos).itemView.getHeight()));
                        if (offset < 0)
                            return offset;
                        else
                            break;
                    }
                }
            }
            top = Math.max(0,top);
        }

        return top;
    }

    private int getHeaderHeightForLayout(View header){
        return mRenderInline ? 0 : header.getHeight();
    }
}
