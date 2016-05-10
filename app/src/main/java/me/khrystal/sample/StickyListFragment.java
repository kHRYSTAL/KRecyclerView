package me.khrystal.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import me.khrystal.listener.StickyHeaderAdapter;
import me.khrystal.widget.DividerDecoration;
import me.khrystal.widget.KRecyclerView;
import me.khrystal.widget.StickyHeaderDecoration;


/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/5/10
 * update time:
 * email: 723526676@qq.com
 */
public class StickyListFragment extends Fragment implements KRecyclerView.LoadDataListener, RecyclerView.OnItemTouchListener {

    private KRecyclerView recyclerView;
    private List<String> dataList;
    private SimpleStickyHeaderAdapter adapter;
    int start,end;
    private StickyHeaderDecoration decor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataList = new ArrayList<>();
        recyclerView = (KRecyclerView) view.findViewById(R.id.recyclerview);


        final DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())
                .setHeight(5.0f)
                .setPadding(10.0f)
                .setColor(Color.BLACK)
                .build();
        recyclerView.addItemDecoration(divider);
        // TODO: 16/5/10
        adapter = new SimpleStickyHeaderAdapter(this,dataList);





        recyclerView.setAdapter(adapter, 1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLoadDataLintener(this);
        recyclerView.setItemCount(10);
        recyclerView.isUseByNetWork(false);
        decor = new StickyHeaderDecoration(adapter);


        recyclerView.addItemDecoration(decor,1);

        PtrClassicDefaultHeader defaultHeader = new PtrClassicDefaultHeader(getActivity());
        recyclerView.mPtrFrameLayout.setHeaderView(defaultHeader);
        recyclerView.mPtrFrameLayout.addPtrUIHandler(defaultHeader);
//TODO if stickyHeader need click must add this method
        recyclerView.addOnItemTouchListener(this);
        recyclerView.setHasFixedSize(true);


        recyclerView.mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.mPtrFrameLayout.autoRefresh(true);
            }
        }, 400);

    }

    @Override
    public void loadData(final int page) {
        if (page==1){
            adapter.clear();
            start=0;end=0;
        }

        end = start + 10;
        new AsyncTask<Void,Void,List<String>>(){
            @Override
            protected List<String> doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> list = new ArrayList();
                if (page<=6) {
                    for (int i=start ;i < end; i++) {
                        list.add(String.valueOf(i));
                    }
                    start = end;
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                if (strings!=null&&strings.size()>0){
                    recyclerView.hideEmptyView();
                    adapter.append(strings);
                    adapter.notifyDataSetChanged();
//                    TODO enableLoadMore
                    recyclerView.enableLoadMore();
                }else {
//                    TODO cantLoadMore
                    recyclerView.cantLoadMore();
                }
                recyclerView.endRefreshing();
            }
        }.execute();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View v = rv.findChildViewUnder(e.getX(), e.getY());
        Log.e("findChildViewUnder",""+(v==null));
        return v == null;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        if (e.getAction() != MotionEvent.ACTION_UP) {
            return;
        }
        // find the header that was clicked
        View view = decor.finderHeaderViewUnder(e.getX(), e.getY());
        Log.e("Sticky",""+view);
        //TODO do something when click
        if (view instanceof TextView) {
            Toast.makeText(this.getActivity(), ((TextView) view).getText() + " clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //nothing to do
    }

    public class SimpleStickyHeaderAdapter extends RecyclerView.Adapter<SimpleViewHolder>
            implements StickyHeaderAdapter<SimpleHeaderViewHolder>{


        List<String> dataList;
        Object parent;

        public SimpleStickyHeaderAdapter(Object parent, List<String> dataList) {
            this.dataList = dataList;
            this.parent = parent;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            SimpleViewHolder holder = new SimpleViewHolder(v, parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            String model = dataList.get(position);
            holder.bind(model);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
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
        public void onBindHeaderViewHolder(SimpleHeaderViewHolder viewHolder, int position) {
            viewHolder.textView.setText("Header " + getHeaderId(position));
        }

        public void append(List<String> items) {
            int pos = dataList.size();
            for (String item : items) {
                dataList.add(item);
            }
            notifyItemRangeInserted(pos, items.size());
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


    /**
     * StickyHeader
     */
    public class SimpleHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            addOnClickListener(textView);

        }

        public void addOnClickListener(View view) {
            if (view != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.header_text:
                    Toast.makeText(getActivity(),textView.getText(),Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        String str;
        public void bind(String str){
            if (!TextUtils.isEmpty(str))
                this.str = str;
            textView.setText(str);
        }
    }

    /**
     * NomalViewHolder
     */
    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected Activity mActivity;
        protected Fragment mFragment;
        public View mView;
        TextView textView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public SimpleViewHolder(View itemView, Object parent) {
            super(itemView);
            if (parent == null)
                return;
            if (parent instanceof Activity) {
                mActivity = (Activity) parent;
            } else if (parent instanceof Fragment) {
                mFragment = (Fragment) parent;
            }
            textView = (TextView)itemView.findViewById(R.id.item_text);
            addOnClickListener(textView);

        }

        public void addOnClickListener(View view) {
            if (view != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_text:
                    Toast.makeText(getActivity(),this.str,Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        String str;
        public void bind(String str){
            if (!TextUtils.isEmpty(str))
                this.str = str;
            textView.setText(str);
        }


    }
}
