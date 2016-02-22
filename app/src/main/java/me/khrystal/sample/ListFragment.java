package me.khrystal.sample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import me.khrystal.widget.KRecyclerView;
import me.khrystal.view.WavePtrHeader;

/**
 *
 * @FileName: me.khrystal.sample.ListFragment.java
 * @author: kHRYSTAL
 * @email: 723526676@qq.com
 * @date: 2016-02-22 18:20
 */
public class ListFragment extends Fragment implements KRecyclerView.LoadDataListener {

    protected View rootView;
    private KRecyclerView recyclerView;
    private List<String> dataList;
    private SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        dataList = new ArrayList<>();
        int type = getArguments().getInt("ListType",0);
        int column = 0;
        switch (type){
            case 0:
            case 1:
                column=1;
                break;
            case 2:
            case 3:
                column=3;
                break;
        }
        recyclerView = (KRecyclerView) view.findViewById(R.id.recyclerview);
        adapter = new SimpleAdapter(this,dataList);
        recyclerView.setAdapter(adapter,column, LinearLayoutManager.VERTICAL);
        recyclerView.setLoadDataLintener(this);
        recyclerView.setItemCount(10);
        recyclerView.isUseByNetWork(false);
        if (type==1||type==3){
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_view,recyclerView,false);
            recyclerView.addHeaderView(headerView);
        }
        setCustomRefresh();
//        autorefresh once
        recyclerView.mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.mPtrFrameLayout.autoRefresh(true);
            }
        },400);
    }

    private void setCustomRefresh() {
        WavePtrHeader wavePtrHeader = new WavePtrHeader(getActivity());
        recyclerView.mPtrFrameLayout.setHeaderView(wavePtrHeader);
        recyclerView.mPtrFrameLayout.addPtrUIHandler(wavePtrHeader);
    }

    int start,end;
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
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> list = new ArrayList();
                if (page<=6) {
                    for (int i=start ;i < end; i++) {
                        list.add("第" + i + "个");
                    }
                    start = end;
                }
                Log.e("ListFragment","listSize:"+list.size()+",dataListSize:"+dataList.size());
                return list;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                if (strings!=null&&strings.size()>0){
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
