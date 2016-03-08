package me.khrystal.sample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import me.khrystal.view.WavePtrHeader;
import me.khrystal.widget.KRecyclerView;

/**
 * support swiprefreshLayout
 *
 * @FileName: me.khrystal.sample.SwipRefreshListFragment.java
 * @author: kHRYSTAL
 * @email: 723526676@qq.com
 * @date: 2016-02-22 23:14
 */
public class SwipRefreshListFragment extends Fragment implements KRecyclerView.LoadDataListener, SwipeRefreshLayout.OnRefreshListener {

    protected View rootView;
    private KRecyclerView recyclerView;
    private List<String> dataList;
    private SimpleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    int start, end;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swip_refresh, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        dataList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swip_refresh);
        recyclerView = (KRecyclerView) view.findViewById(R.id.recyclerview);
        adapter = new SimpleAdapter(this, dataList);
        recyclerView.setAdapter(adapter, 1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLoadDataLintener(this);
        recyclerView.setItemCount(10);
        recyclerView.isUseByNetWork(false);
        setCustomRefresh();
    }

    private void setCustomRefresh() {
        recyclerView.mPtrFrameLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        recyclerView.setPageAndRefresh(1);
        swipeRefreshLayout.setRefreshing(true);
    }


    @Override
    public void loadData(final int page) {
        if (page == 1) {
            adapter.clear();
            recyclerView.showEmptyView();
            start = 0;
            end = 0;
        }

        end = start + 10;
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> list = new ArrayList();
                if (page <= 6) {
                    for (int i = start; i < end; i++) {
                        list.add(String.valueOf(i));
                    }
                    start = end;
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                if (strings != null && strings.size() > 0) {
                    adapter.append(strings);
                    recyclerView.hideEmptyView();
                    adapter.notifyDataSetChanged();
//                    TODO enableLoadMore
                    recyclerView.enableLoadMore();
                } else {
//                    TODO cantLoadMore
                    recyclerView.cantLoadMore();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }



    /**
     * Adapter
     */
    public class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

        List<String> dataList;
        Object parent;

        public SimpleAdapter(Object parent, List<String> dataList) {
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

        public void append(List<String> items) {
            int pos = dataList.size();
            for (String item : items) {
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

    /**
     * ViewHolder
     */
    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            textView = (TextView) itemView.findViewById(R.id.item_text);
            addOnClickListener(textView);

        }

        public void addOnClickListener(View view) {
            if (view != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_text:
                    Toast.makeText(getActivity(), this.str, Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        String str;

        public void bind(String str) {
            if (!TextUtils.isEmpty(str))
                this.str = str;
            textView.setText(str);
        }


    }
}