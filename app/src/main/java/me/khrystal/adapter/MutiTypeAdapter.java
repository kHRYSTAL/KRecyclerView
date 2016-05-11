package me.khrystal.adapter;

import android.content.Context;

import java.util.List;

import me.khrystal.deglgationlib.base.DelegateAdapter;
import me.khrystal.deglgationlib.base.DelegateViewHolder;
import me.khrystal.module.Color;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/5/11
 * update time:
 * email: 723526676@qq.com
 */
public class MutiTypeAdapter extends DelegateAdapter<Color>{

    List<Color> dataList;

    public MutiTypeAdapter(Context context, List<Color> datas) {
        super(context, datas,true);
        dataList = datas;
    }

    @Override
    public void onBindViewHolder(DelegateViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
    }

    public void append(List<Color> items) {
        int pos = dataList.size();
        for (Color item : items) {
            dataList.add(item);
        }
        notifyItemRangeInserted(pos, items.size());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void remove(int position) {
        if (dataList.size() > 0) {
            dataList.remove(position);
            this.notifyItemRemoved(position);
        }
    }

    public void clear() {
        int size = dataList.size();
        dataList.clear();
        this.notifyItemRangeRemoved(0, size);
    }
}
