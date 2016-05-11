package me.khrystal.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.khrystal.deglgationlib.base.DelegateAdapter;
import me.khrystal.listener.StickyHeaderAdapter;
import me.khrystal.module.Color;
import me.khrystal.sample.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/5/10
 * update time:
 * email: 723526676@qq.com
 */
public  class StickyMutiAdapter extends DelegateAdapter<Color> implements StickyHeaderAdapter<StickyMutiAdapter.SimpleHeaderViewHolder>{

    private Context mContext;
    private List<Color> dataList;

    public StickyMutiAdapter(Context context, List<Color> datas) {
        super(context,datas,true);
        mContext = context;
        dataList = datas;
    }

    @Override
    public long getHeaderId(int position) {
        return (long)position / 7;
    }

    @Override
    public SimpleHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sticky_header, parent, false);
        SimpleHeaderViewHolder holder = new SimpleHeaderViewHolder(v, parent);
        return holder;
    }

    @Override
    public void onBindHeaderViewHolder(final StickyMutiAdapter.SimpleHeaderViewHolder viewHolder, int position) {
        viewHolder.textView.setText("Header " + getHeaderId(position));
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,viewHolder.textView.getText(),Toast.LENGTH_SHORT).show();
            }
        });
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

    public class SimpleHeaderViewHolder extends RecyclerView.ViewHolder {

        protected Activity mActivity;
        protected Fragment mFragment;
        public View mView;
        TextView textView;

        public SimpleHeaderViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public SimpleHeaderViewHolder(View itemView, Object parent) {
            super(itemView);
            if (parent == null)
                return;
            if (parent instanceof Activity) {
                mActivity = (Activity) parent;
            } else if (parent instanceof Fragment) {
                mFragment = (Fragment) parent;
            }
            textView = (TextView)itemView.findViewById(R.id.header_text);
        }

        String str;
        public void bind(String str){
            if (!TextUtils.isEmpty(str))
                this.str = str;
            textView.setText(str);
        }
    }



}
