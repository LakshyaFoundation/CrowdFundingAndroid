package com.lakshya;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lakshya.lib.InfiniteScrollListViewDemoActivity;

public class FTUActivity extends BaseActivity implements OnClickListener
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ftu_layout);
		initUi();
	}
	
	private void initUi()
	{
		setFragment();
		
		Button startButton = (Button) findViewById(R.id.ftuStartButton);
		
		startButton.setOnClickListener(this);
		
		
		
	}
	
	private void setFragment()
	{
		FTUIntroFragment fragment = new FTUIntroFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.ftu_content_layout,fragment);
		ft.commit();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.ftuStartButton:
			Intent intent = new Intent ();
			intent.setClass(FTUActivity.this, InfiniteScrollListViewDemoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			break;

		default:
			break;
		}	
	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()); 
		prefs.edit().putBoolean("ftuAlreadyShown", true).commit();
	}

}
