package me.khrystal.listener;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/5/10
 * update time:
 * email: 723526676@qq.com
 */
public interface StickyHeaderAdapter<T extends RecyclerView.ViewHolder> {

    long getHeaderId(int position);

    T onCreateHeaderViewHolder(ViewGroup parent);

    void onBindHeaderViewHolder(T viewHolder, int position);
}
