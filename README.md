# KRecyclerView

------

KRecyclerView is a RecyclerView,
dependent on [Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh) and [HeaderAndFooterRecyclerView](https://github.com/cundong/HeaderAndFooterRecyclerView)
,it's support：

> * pull to refresh
> * load more
> * showEmptyView
> * addHeaderView

it's support Grid and List(Refresh and LoadMore orientation is Vertical)

##screenshot
![ss-1](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot1.png)
![ss-2](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot2.png)
![ss-3](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot3.png)


##Setup with Android Studio
```
    allprojects {
    		repositories {
    			...
    			maven { url "https://jitpack.io" }
    		}
    	}
```
```
    	dependencies {
    	        compile 'com.github.kHRYSTAL:KRecyclerView:v0.1.0'
    	}
```

the library minSdkVersion is 13 and targetSdkVersion is 23

------

###Attribute

```xml
    <me.khrystal.widget.KRecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:recyclerviewClipToPadding="true|false"
        app:recyclerviewPadding="10dp"
        app:recyclerviewPaddingBottom="10dp"
        app:recyclerviewPaddingLeft="10dp"
        app:recyclerviewPaddingRight="10dp"
        app:recyclerviewPaddingTop="10dp"
        app:recyclerviewEmptyView="@layout/empty_view">
```

### Usage

####init
```java
krecyclerView = (KRecyclerView) view.findViewById(R.id.recyclerview);
//set your adapter like RecyclerView.Adapter<>
adapter = new SimpleAdapter(this,dataList);
//setAdapter
//Notice when column <= 1,KRecyclerView will use LinearLayoutManger
krecyclerView.setAdapter(adapter, column,LinearLayoutManager.VERTICAL);
krecyclerView.setLoadDataLintener(new KRecyclerView.LoadDataListener() {
    @Override
    public void loadData(int page) {
      //load data method it's used by refresh and load more
    }
});
//make KRecyclerView know how many items in a page
krecyclerView.setItemCount(10);
//make KRecyclerView know loaddata need to judge network can available
//Notice when isUseByNetWork(true) and network can't available
//the loading footer can click to load more again
krecyclerView.isUseByNetWork(true);
```

### setRefreshHeader

you can watch [Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh) to know how to use

sample has a custom refresh header

```java
//set classic refresh header
PtrClassicDefaultHeader defaultHeader =
    new PtrClassicDefaultHeader(getActivity());
krecyclerView.mPtrFrameLayout.setHeaderView(defaultHeader);
krecyclerView.mPtrFrameLayout.addPtrUIHandler(defaultHeader);
//auto refresh once
krecyclerView.mPtrFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    krecyclerView.mPtrFrameLayout.autoRefresh(true);
                }
            }, 400);
```

if you want to use **SwipRefreshLayout** you can use like
```java
 private void setCustomRefresh() {
        krecyclerView.mPtrFrameLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        //this method will set page=1 and called loadData(int page)
        krecyclerView.setPageAndRefresh(1);
        swipeRefreshLayout.setRefreshing(true);
    }

```
###show|hide EmptyView
```java
krecyclerView.showEmptyView();
krecyclerView.hideEmptyView();
```

###set KRecycerView reenable load more or cant load more
```java
krecyclerView.enableLoadMore();
krecyclerView.cantLoadMore();
```
###addheaderView
```java
headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_view,recyclerView,false);
recyclerView.addHeaderView(headerView);
```


###more usage can watch sample

#Thanks
[@liaohuqiu][1]
[@cundong][2]



[1]:https://github.com/liaohuqiu
[2]:https://github.com/cundong

