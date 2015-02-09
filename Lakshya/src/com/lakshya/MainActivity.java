package com.lakshya;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Intent intent = new Intent();
		switch(id)
		{
		case R.id.action_about:
			intent.setClass(this.getApplicationContext(), FTUActivity.class);
			MainActivity.this.startActivity(intent);
			return true;
		case R.id.action_newsletters:
			intent.setClass(this.getApplicationContext(), NewsLetterActivity.class);
			MainActivity.this.startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
