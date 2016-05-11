# KRecyclerView

------

KRecyclerView is a RecyclerView,
dependent on [Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh) and [HeaderAndFooterRecyclerView](https://github.com/cundong/HeaderAndFooterRecyclerView)
,it's support：

> * pull to refresh
> * load more
> * showEmptyView
> * addHeaderView
> * support sticky header(need fix,couldn't support click)
> * support multi-type when use sticky header

later:
> * can modify load more UI  


it's support Grid and List(Refresh and LoadMore orientation is Vertical)
if you want use multi-type display easily, you can use [RecyclerViewAdapterDelegate][3]


##screenshot
![ss-1](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot1.gif)
![ss-2](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot2.gif)
![ss-3](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot3.gif)
![ss-4](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot4.gif)
![ss-5](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot5.gif)
![ss-6](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot6.gif)
![ss-7](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot7.gif)
![ss-8](https://github.com/kHRYSTAL/KRecyclerView/blob/master/screenshot/screenshot8.gif)


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
    	        compile 'com.github.kHRYSTAL:KRecyclerView:v0.2.0'
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
kRecyclerView = (KRecyclerView) view.findViewById(R.id.recyclerview);
//set your adapter like RecyclerView.Adapter<>
adapter = new SimpleAdapter(this,dataList);
//setAdapter
//Notice when column <= 1,KRecyclerView will use LinearLayoutManger
kRecyclerView.setAdapter(adapter, column,LinearLayoutManager.VERTICAL);
kRecyclerView.setLoadDataListener(new KRecyclerView.LoadDataListener() {
    @Override
    public void loadData(int page) {
      //load data method it's used by refresh and load more
    }
});
//make KRecyclerView know how many items in a page
kRecyclerView.setItemCount(10);
//make KRecyclerView know loadData need to judge network can available
//Notice when isUseByNetWork(true) and network can't available
//the loading footer can click to load more again
kRecyclerView.isUseByNetWork(true);
```

### setRefreshHeader

you can watch [Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh) to know how to use

sample has a custom refresh header

```java
//set classic refresh header
PtrClassicDefaultHeader defaultHeader =
    new PtrClassicDefaultHeader(getActivity());
kRecyclerView.mPtrFrameLayout.setHeaderView(defaultHeader);
kRecyclerView.mPtrFrameLayout.addPtrUIHandler(defaultHeader);
//auto refresh once
kRecyclerView.mPtrFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    kRecyclerView.mPtrFrameLayout.autoRefresh(true);
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
        kRecyclerView.setPageAndRefresh(1);
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
kRecyclerView.enableLoadMore();
kRecyclerView.cantLoadMore();
```
###addheaderView
```java
headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_view,kRecyclerView,false);
kRecyclerView.addHeaderView(headerView);
```


###more usage can watch sample

#Thanks
[@liaohuqiu][1]
[@cundong][2]



[1]:https://github.com/liaohuqiu
[2]:https://github.com/cundong
[3]:https://github.com/kHRYSTAL/RecyclerViewAdapterDelegate

