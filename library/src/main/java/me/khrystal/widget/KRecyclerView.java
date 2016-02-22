package me.khrystal.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.khrystal.listener.EndlessRecyclerOnScrollListener;
import me.khrystal.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import me.khrystal.recyclerview.HeaderSpanSizeLookup;
import me.khrystal.recyclerview.NetworkUtils;
import me.khrystal.recyclerview.RecyclerViewStateUtils;
import me.khrystal.recyclerview.RecyclerViewUtils;

/**
 * RecyclerView,support loadmore and refresh
 *
 * user by Ptr and RecyclerUtil
 *
 * @FileName: me.khrystal.widget.KRecyclerView.java
 * @author: kHRYSTAL
 * @email: 723526676@qq.com
 * @date: 2016-02-22 16:20
 */
public class KRecyclerView extends RelativeLayout{

    public RecyclerView mRecyclerView;
    public PtrFrameLayout mPtrFrameLayout;

    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected boolean mClipToPadding;

    protected ViewStub mEmpty;
    protected View mEmptyView;
    protected int mEmptyId;
    private View rootView;

    private Context mContext;

    private int mItemCount=10;
    private boolean hasMore = true;
    private boolean useNet;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private LoadDataListener mListener;
    private int maxRefreshTime=2000;
    private int mPage=1;

    public KRecyclerView(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    public KRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initAttrs(attrs);
        initViews();
    }



    public KRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(attrs);
        initViews();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.KRecyclerView);

        try {
            mPadding = (int) typedArray.getDimension(R.styleable.KRecyclerView_recyclerviewPadding, -1.1f);
            mPaddingTop = (int) typedArray.getDimension(R.styleable.KRecyclerView_recyclerviewPaddingTop, 0.0f);
            mPaddingBottom = (int) typedArray.getDimension(R.styleable.KRecyclerView_recyclerviewPaddingBottom, 0.0f);
            mPaddingLeft = (int) typedArray.getDimension(R.styleable.KRecyclerView_recyclerviewPaddingLeft, 0.0f);
            mPaddingRight = (int) typedArray.getDimension(R.styleable.KRecyclerView_recyclerviewPaddingRight, 0.0f);
            mClipToPadding = typedArray.getBoolean(R.styleable.KRecyclerView_recyclerviewClipToPadding, false);
            mEmptyId = typedArray.getResourceId(R.styleable.KRecyclerView_recyclerviewEmptyView, 0);
        } finally {
            typedArray.recycle();
        }
    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.krecycler_view_layout, this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mPtrFrameLayout = (PtrFrameLayout) rootView.findViewById(R.id.refresh_layout);
        if (mRecyclerView != null) {
            mRecyclerView.setClipToPadding(mClipToPadding);
            if (mPadding != -1.1f) {
                mRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
        }

        setCustomSwipeToRefresh();

        mEmpty = (ViewStub) rootView.findViewById(R.id.emptyview);
        mEmpty.setLayoutResource(mEmptyId);
        if (mEmptyId != 0)
            mEmptyView = mEmpty.inflate();
        mEmpty.setVisibility(View.GONE);
    }

    public void setCustomSwipeToRefresh() {
        mPtrFrameLayout.setResistance(1.7f);
        mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrameLayout.setDurationToClose(200);
        mPtrFrameLayout.setDurationToCloseHeader(1000);
        mPtrFrameLayout.setPullToRefresh(false);
        mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
                boolean canbePullDown = PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view1);
                return canbePullDown;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                refresh();
            }
        });
    }

    public void setRecyclerViewBackground(@ColorInt int color){
        mRecyclerView.setBackgroundColor(color);
    }


    public void setEmptyView(@LayoutRes int emptyResourceId) {
        mEmptyId = emptyResourceId;

        mEmpty.setLayoutResource(mEmptyId);
        if (mEmptyId != 0)
            mEmptyView = mEmpty.inflate();
        mEmpty.setVisibility(View.GONE);
    }

    /**
     * Show the custom or default empty view.
     */
    public void showEmptyView() {
        if (mEmptyId != 0)
            mEmpty.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the custom or default empty view.
     */
    public void hideEmptyView() {
        if (mEmptyId != 0)
            mEmpty.setVisibility(View.GONE);
    }

    /**
     * Set a listener that will be notified of any changes in scroll state or position.
     *
     * @param customOnScrollListener to set or null to clear
     * @deprecated Use {@link #addOnScrollListener(RecyclerView.OnScrollListener)} and
     * {@link #removeOnScrollListener(RecyclerView.OnScrollListener)}
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener customOnScrollListener) {
        mRecyclerView.setOnScrollListener(customOnScrollListener);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener customOnScrollListener) {
        mRecyclerView.addOnScrollListener(customOnScrollListener);
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener customOnScrollListener) {
        mRecyclerView.removeOnScrollListener(customOnScrollListener);
    }


    /**
     * Add an {@link RecyclerView.ItemDecoration} to this RecyclerView. Item decorations can affect both measurement and drawing of individual item views. Item decorations are ordered. Decorations placed earlier in the list will be run/queried/drawn first for their effects on item views. Padding added to views will be nested; a padding added by an earlier decoration will mean further item decorations in the list will be asked to draw/pad within the previous decoration's given area.
     *
     * @param itemDecoration Decoration to add
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * Add an {@link RecyclerView.ItemDecoration} to this RecyclerView. Item decorations can affect both measurement and drawing of individual item views.
     * <p>Item decorations are ordered. Decorations placed earlier in the list will be run/queried/drawn first for their effects on item views. Padding added to views will be nested; a padding added by an earlier decoration will mean further item decorations in the list will be asked to draw/pad within the previous decoration's given area.</p>
     *
     * @param itemDecoration Decoration to add
     * @param index          Position in the decoration chain to insert this decoration at. If this value is negative the decoration will be added at the end.
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecyclerView.addItemDecoration(itemDecoration, index);
    }

    /**
     * Sets the {@link RecyclerView.ItemAnimator} that will handle animations involving changes
     * to the items in this RecyclerView. By default, RecyclerView instantiates and
     * uses an instance of {@link android.support.v7.widget.DefaultItemAnimator}. Whether item animations are enabled for the RecyclerView depends on the ItemAnimator and whether
     * the LayoutManager {@link android.support.v7.widget.RecyclerView.LayoutManager#supportsPredictiveItemAnimations()
     * supports item animations}.
     *
     * @param animator The ItemAnimator being set. If null, no animations will occur
     *                 when changes occur to the items in this RecyclerView.
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    /**
     * Gets the current ItemAnimator for this RecyclerView. A null return value
     * indicates that there is no animator and that item changes will happen without
     * any animations. By default, RecyclerView instantiates and
     * uses an instance of {@link android.support.v7.widget.DefaultItemAnimator}.
     *
     * @return ItemAnimator The current ItemAnimator. If null, no animations will occur
     * when changes occur to the items in this RecyclerView.
     */
    public RecyclerView.ItemAnimator getItemAnimator() {
        return mRecyclerView.getItemAnimator();
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRecyclerView.removeItemDecoration(decoration);
    }

    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.removeOnItemTouchListener(listener);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }


    public void setAdapter(RecyclerView.Adapter adapter,int column,int orientation){
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        if (mRecyclerView!=null) {
            mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
            if (column>1) {
                GridLayoutManager manager = new GridLayoutManager(mContext, column);
                manager.setOrientation(orientation);
                manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
                mRecyclerView.setLayoutManager(manager);
            }else {
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(orientation);
                mRecyclerView.setLayoutManager(manager);
            }
        }
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if(state == LoadingFooter.State.Loading) {
                return;
            }

            if (hasMore) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState((Activity)mContext, mRecyclerView, mItemCount, LoadingFooter.State.Loading, null);
                load(mPage);
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState((Activity)mContext, mRecyclerView, mItemCount, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    public void setItemCount(int defaultItemCount){
        mItemCount = defaultItemCount;
    }

    private void load(int page) {
        if (useNet && !NetworkUtils.isNetAvailable(mContext)) {
            RecyclerViewStateUtils.setFooterViewState((Activity) mContext, mRecyclerView, mItemCount, LoadingFooter.State.NetWorkError, this.mFooterClick);
        } else {
            if (page!=1) RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Loading);
            else RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
            mListener.loadData(page);
        }
    }

    public void isUseByNetWork(boolean useNet){
        this.useNet = useNet;
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState((Activity) mContext, mRecyclerView, mItemCount, LoadingFooter.State.Loading, null);
            load(mPage);
        }
    };


    public void setLoadDataLintener(LoadDataListener lintener){
        mListener = lintener;
    }

    public interface LoadDataListener{
        public void loadData(int page);
    }


    public void setMaxRefreshTime(int millistime){
        maxRefreshTime = millistime;
    }

    public void cantLoadMore(){
        hasMore = false;
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.TheEnd);
    }

    public void enableLoadMore(){
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
        hasMore = true;
        mPage++;
    }

    public void addHeaderView(View headerView){
        if (mRecyclerView!=null)
            RecyclerViewUtils.setHeaderView(mRecyclerView, headerView);
    }

    public void addFooterView(View footerView){
        if (mRecyclerView!=null)
            RecyclerViewUtils.setFooterView(mRecyclerView,footerView);
    }

    public void refresh(){
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPtrFrameLayout != null && mPtrFrameLayout.isRefreshing()) {
                    mPtrFrameLayout.refreshComplete();
                }
            }
        }, maxRefreshTime);
        mPage = 1;
        load(mPage);
    }

    public void setPageAndRefresh(int page){
        mPage = page;
        load(mPage);
    }

    public void endRefreshing() {
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mPtrFrameLayout != null) {
                        mPtrFrameLayout.refreshComplete();
                    }
                }
            }, 500);
        }
    }
}
