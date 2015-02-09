package com.lakshya;

import com.lakshya.lib.InfiniteScrollListViewDemoActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;


public class LauncherActivity extends BaseActivity
{
	public static final String ACCOUNT_EXCHANGE = "com.citrix.exchange";


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_layout);

		boolean locked = true;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

		final Boolean yourLocked = prefs.getBoolean("ftuAlreadyShown", false);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run()
			{	if(yourLocked)
				{	
					Intent intent = new Intent(LauncherActivity.this,InfiniteScrollListViewDemoActivity.class);
					LauncherActivity.this.startActivity(intent);
					LauncherActivity.this.finish();
				}
				else
				{
					Intent intent = new Intent(LauncherActivity.this,FTUActivity.class);
					LauncherActivity.this.startActivity(intent);
					LauncherActivity.this.finish();
				}
			}
		}, 1000);

	}
}
