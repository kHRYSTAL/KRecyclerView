package me.khrystal.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import me.khrystal.adapter.StickyMutiAdapter;
import me.khrystal.deglgationlib.base.DelegateViewHolder;
import me.khrystal.deglgationlib.base.Delegation;
import me.khrystal.module.Color;
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
public class MutiTypeStickFragment extends Fragment implements KRecyclerView.LoadDataListener {

    private KRecyclerView recyclerView;
    private List<Color> dataList;
    private StickyMutiAdapter adapter;
    int start,end;

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
                .setColor(android.graphics.Color.BLACK)
                .build();
        recyclerView.addItemDecoration(divider);
        // TODO: 16/5/10
        adapter = new StickyMutiAdapter(getActivity(),dataList);
        adapter.addDelegation(new Delegation<Color>(R.layout.red_item,1) {
            @Override
            protected void handle(DelegateViewHolder delegateViewHolder, Color color,final int i) {
                delegateViewHolder.getTextView(R.id.item_text).setText("Red"+i);
                delegateViewHolder.getTextView(R.id.item_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"Red"+i,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        adapter.addDelegation(new Delegation<Color>(R.layout.blue_item,2) {
            @Override
            protected void handle(DelegateViewHolder delegateViewHolder, Color color,final int i) {
                delegateViewHolder.getTextView(R.id.item_text).setText("Blue"+i);
                delegateViewHolder.getTextView(R.id.item_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"Blue"+i,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });






        recyclerView.setAdapter(adapter, 1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLoadDataLintener(this);
        recyclerView.setItemCount(10);
        recyclerView.isUseByNetWork(false);
        StickyHeaderDecoration decor = new StickyHeaderDecoration(adapter);


        recyclerView.addItemDecoration(decor,1);

        PtrClassicDefaultHeader defaultHeader = new PtrClassicDefaultHeader(getActivity());
        recyclerView.mPtrFrameLayout.setHeaderView(defaultHeader);
        recyclerView.mPtrFrameLayout.addPtrUIHandler(defaultHeader);


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
        new AsyncTask<Void,Void,List<Color>>(){
            @Override
            protected List<Color> doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Color> list = new ArrayList();
                if (page<=6) {
                    for (int i=start ;i < end; i++) {
                        if (i % 2 == 0) list.add(new Color(1));
                        else list.add(new Color(2));
                    }
                    start = end;
                }
                Collections.shuffle(list);
                return list;
            }

            @Override
            protected void onPostExecute(List<Color> colors) {
                if (colors!=null&&colors.size()>0){
                    recyclerView.hideEmptyView();
                    adapter.append(colors);
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
}
