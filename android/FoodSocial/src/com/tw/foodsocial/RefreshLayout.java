package com.tw.foodsocial;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class RefreshLayout extends SwipeRefreshLayout implements OnScrollListener{
	private int mTouchSlop;
	private ListView mListView;
	private OnLoadListener mOnLoadListener;
	private View mListViewFooter;
	private int mYDown,mLastY;
	private boolean isLoading = false;
	public RefreshLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public RefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mListViewFooter = LayoutInflater.from(context).inflate(R.layout.fooditem, null,false);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if(mListView == null){
			getListView();
		}
	}
	
	private void getListView() {
		// TODO Auto-generated method stub
		int childs = getChildCount();
		if(childs > 0){
			View childView = getChildAt(0);
			if(childView instanceof ListView){
				mListView = (ListView) childView;
				mListView.setOnScrollListener(this);
				Log.d(VIEW_LOG_TAG, "找到ListView");
			}
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		final int action = ev.getAction();
		
		switch(action){
			case MotionEvent.ACTION_DOWN:
				mYDown = (int) ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				mLastY = (int) ev.getRawY();
				break;
			case MotionEvent.ACTION_UP:
				if(canLoad()){
					loadData();
				}
				break;
			default:
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private boolean canLoad() {
		// TODO Auto-generated method stub
		return isBottom() && !isLoading && isPullUp();
	}
	
	private boolean isBottom() {
		// TODO Auto-generated method stub
		if(mListView != null && mListView.getAdapter() != null){
			return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() -1);
		}
		return false;
	}

	private boolean isPullUp() {
		// TODO Auto-generated method stub
		return (mYDown - mLastY) >= mTouchSlop;
	}

	public void loadData() {
		// TODO Auto-generated method stub
		if(mOnLoadListener != null){
			setLoading(true);
			mOnLoadListener.onLoad();
		}
	}

	public void setLoading(boolean loading) {
		// TODO Auto-generated method stub
		isLoading = loading;
		if(isLoading){
			mListView.addFooterView(mListViewFooter);
		}else{
			//mListView.removeFooterView(mListViewFooter);
			mYDown = 0;
			mLastY = 0;
		}
	}
	
	public void setOnLoadListener(OnLoadListener loadListener){
		mOnLoadListener = loadListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(canLoad()){
			loadData();
		}
	}

	public static interface OnLoadListener{
		public void onLoad();
	}


	
}
