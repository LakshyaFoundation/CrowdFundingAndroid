package com.lakshya;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.PopupWindow;


public class BaseActivity extends ActionBarActivity 
{
	public static boolean isAppInForeground = false;
	public static Activity topActivity = null;
	public static PopupWindow fadePopup = null;
	public static Set<PopupWindow>  popupWindowSet = Collections.synchronizedSet(new LinkedHashSet<PopupWindow>());
	private AnimationType mAnimationType = AnimationType.DEFAULT;
	
	
	private enum AnimationType
	{
		DEFAULT,
		TOP_BOTTOM,
		RIGHT_LEFT;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		isAppInForeground = true;

	}

	@Override
	protected void onStart()
	{
		super.onStart();
		topActivity =  this;
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		isAppInForeground = false;
		clearPopups();
		
	}
	
	private void clearPopups()
	{
		PopupWindow popupWindow = null;
		Iterator<PopupWindow> iterator = popupWindowSet.iterator();
		while(iterator.hasNext())
		{
			popupWindow = iterator.next();
			popupWindow.dismiss();
			iterator.remove();
		}
		if(fadePopup!=null)
		{
			fadePopup.dismiss();
			fadePopup = null;
		}
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		clearPopups();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mAnimationType = AnimationType.DEFAULT;
		cancelNotification();
		
	}
	
	protected void onCreateWithTopBottomAnimation(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		cancelNotification();
		mAnimationType = AnimationType.TOP_BOTTOM;
		overridePendingTransition( R.anim.slide_in_up, R.anim.anim_pause );

	}
	
	protected void onCreateWithRightLeftAnimation(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		cancelNotification();
		mAnimationType = AnimationType.RIGHT_LEFT;
		overridePendingTransition( R.anim.slide_in_left,R.anim.anim_pause );
	}
	
	private void cancelNotification()
	{
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancelAll();
	}
	
	@Override
	public void finish()
	{
		super.finish();
		switch (mAnimationType)
		{
		case TOP_BOTTOM:
			 overridePendingTransition(R.anim.anim_pause, R.anim.slide_out_down);
			break;
			
		case RIGHT_LEFT:
			overridePendingTransition( R.anim.anim_pause,R.anim.slide_out_right );
			break;

		default:
			break;
		}
		
	}
	
	public void onNetworkEvent(boolean isConnected,boolean isFailover)
	{
		
	}
}

