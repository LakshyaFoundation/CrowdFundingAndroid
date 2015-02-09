/**
 *
 * Copyright 2013 Wei Xiao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.lakshya.lib;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

import com.lakshya.Constants;
import com.lakshya.lib.InfiniteScrollListView.LoadingMode;
import com.lakshya.lib.InfiniteScrollListView.StopPosition;

/**
 * A customized list adapter to work with infinite scroll list view
 */
public abstract class InfiniteScrollListAdapter extends BaseAdapter implements OnScrollListener {

	public static final String TAG = InfiniteScrollListAdapter.class.getSimpleName();
	
	public static final int PROJECT_LOAD_BUFFER = 3; //When the user is slowly scrolling throught he tweets, if the backing adapter has

	// A lock to prevent another scrolling event to be triggered if one is already in session
	protected boolean canScroll = false;
	// A flag to enable/disable row clicks
	protected boolean rowEnabled = true;
	protected LoadingMode loadingMode;
	protected StopPosition stopPosition;
	protected InfiniteScrollListPageListener infiniteListPageListener;
	private long lastDataSetChangedTime = System.currentTimeMillis();

	protected abstract void onScrollNext();
	public abstract View getInfiniteScrollListView(int position, View convertView, ViewGroup parent);
	
	public void setInfiniteListPageListener(InfiniteScrollListPageListener infiniteListPageListener) {
		this.infiniteListPageListener = infiniteListPageListener;
	}

	public void lock() {
		canScroll = false;
	}	
	public void unlock() {
		canScroll = true;
	}
	
	public boolean canScroll()
	{
		long currentTime = System.currentTimeMillis();
		if((currentTime- lastDataSetChangedTime) > Constants.THROTTLE_TIME_BETWEEN_DOWN_SCROLL)
		{
			canScroll = true;
		}
		return canScroll;
	}

    @Override
    public boolean isEnabled(int position) {
    	return rowEnabled; 
    }
	public void setRowEnabled(boolean rowEabled) {
		this.rowEnabled = rowEabled;
	}
	
	public void setLoadingMode(LoadingMode loadingMode) {
		this.loadingMode = loadingMode;
	}
	
	public StopPosition getStopPosition() {
		return stopPosition;
	}
	public void setStopPosition(StopPosition stopPosition) {
		this.stopPosition = stopPosition;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (view instanceof InfiniteScrollListView) {
			// In scroll-to-bottom-to-load mode, when the sum of first visible position and visible count equals the total number
			// of items in the adapter it reaches the bottom
			int bufferItemsToShow = getCount() -(firstVisibleItem + visibleItemCount);
//			if (loadingMode == LoadingMode.SCROLL_TO_BOTTOM && firstVisibleItem + visibleItemCount - 1 == getCount() && canScroll) {
//				onScrollNext();
//			}
			
			Log.d(TAG, "There are getCount()="+ getCount()+" firstVisibleItem="+firstVisibleItem+ " visibleItemCount="+visibleItemCount);
			if((bufferItemsToShow < PROJECT_LOAD_BUFFER  && canScroll()))
			{
				onScrollNext();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		return getInfiniteScrollListView(position, convertView, parent);
	}

	public void notifyEndOfList() {
		// When there is no more to load use the lock to prevent loading from happening
		lock();
		// More actions when there is no more to load
		if (infiniteListPageListener != null) {
			infiniteListPageListener.endOfList();
		}
	}

	public void notifyHasMore() {
		// Release the lock when there might be more to load
		unlock();
		// More actions when it might have more to load
		if (infiniteListPageListener != null) {
			infiniteListPageListener.hasMore();
		}
	}
	
	@Override
	public void notifyDataSetChanged() 
	{
		Log.v(TAG, "notifyDataSetChanged()");
		super.notifyDataSetChanged();
		lastDataSetChangedTime =  System.currentTimeMillis();
	}
	@Override
	public void notifyDataSetInvalidated() 
	{
		Log.v(TAG, "notifyDataSetInvalidated()");
		super.notifyDataSetInvalidated();
		lastDataSetChangedTime =  System.currentTimeMillis();
	}

}