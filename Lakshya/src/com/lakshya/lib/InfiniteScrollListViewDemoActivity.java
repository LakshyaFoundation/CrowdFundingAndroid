package com.lakshya.lib;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.imagedisplay.util.ImageFetcher;
import com.imagedisplay.util.Utils;
import com.lakshya.Constants;
import com.lakshya.FTUActivity;
import com.lakshya.NewsLetterActivity;
import com.lakshya.ProjectDetailActivity;
import com.lakshya.Projects;
import com.lakshya.R;
import com.lakshya.data.Project;
import com.lakshya.lib.DemoListAdapter.NewPageListener;
import com.lakshya.lib.InfiniteScrollListView.LoadingMode;
import com.lakshya.lib.InfiniteScrollListView.StopPosition;



/**
 * A demo for the listView with infinite scrolling capability
 * It shows how to make the loading happen when the list view reaches its top or bottom
 * It also shows how to display a customizable view as the loading indicator view either
 * at the top or bottom of the list view
 */
public class InfiniteScrollListViewDemoActivity extends ActionBarActivity {

	public static final String TAG = "InfiniteScrollListViewDemoActivity";

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;




	private ImageFetcher mImageFetcher; //Fetches the images

	TextView mDisplay;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;

	String regid;



	String SENDER_ID = "Your-Sender-ID";

	// A setting for how many items should be loaded at once from the server
	private static final int SEVER_SIDE_BATCH_SIZE = 10;

	private InfiniteScrollListView demoListView;

	private DemoListAdapter demoListAdapter;
	private BogusRemoteService bogusRemoteService;
	private Handler handler;
	private AsyncTask<Void, Void, List<Project>> fetchAsyncTask;

	private LoadingMode loadingMode = LoadingMode.SCROLL_TO_BOTTOM;
	private StopPosition stopPosition = StopPosition.REMAIN_UNCHANGED;

	public InfiniteScrollListViewDemoActivity () {
		super();
		bogusRemoteService = new BogusRemoteService();
		// Set up the image mapping for data points
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		handler = new Handler();

		mImageFetcher = Utils.getImageFetcher(this, 60, 60);

		demoListView = (InfiniteScrollListView) this.findViewById(R.id.infinite_listview_infinitescrolllistview);

		demoListView.setLoadingMode(loadingMode);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		demoListView.setLoadingView(layoutInflater.inflate(R.layout.loading_view_demo, null));
		demoListAdapter = new DemoListAdapter(new NewPageListener() {
			@Override
			public void onScrollNext() {
				fetchAsyncTask = new AsyncTask<Void, Void, List<Project>>() {
					@Override
					protected void onPreExecute() {
						// Loading lock to allow only one instance of loading
						demoListAdapter.lock();
					}
					@Override
					protected List<Project> doInBackground(Void ... params) {
						List<Project> result;
						// Mimic loading data from a remote service
						result = bogusRemoteService.getNextSushiBatch(Projects.mContext,SEVER_SIDE_BATCH_SIZE);
						return result;
					}
					@Override
					protected void onPostExecute(List<Project> result) {
						if (isCancelled() || result == null || result.isEmpty()) {
							demoListAdapter.notifyEndOfList();
						} else {
							// Add data to the placeholder

							demoListAdapter.addEntriesToBottom(result);

							// Add or remove the loading view depend on if there might be more to load
							if (result.size() < SEVER_SIDE_BATCH_SIZE) {
								demoListAdapter.notifyEndOfList();
							} else {
								demoListAdapter.notifyHasMore();
							}
							if (stopPosition != StopPosition.REMAIN_UNCHANGED) {
								demoListView.smoothScrollToPosition((result.size() < SEVER_SIDE_BATCH_SIZE ? demoListAdapter.getCount() : demoListAdapter.getCount() - 2));
							}
						}
					};
					@Override
					protected void onCancelled() {
						// Tell the adapter it is end of the list when task is cancelled
						demoListAdapter.notifyEndOfList();
					}
				}.execute();
			}


			@Override
			public View getInfiniteScrollListView(int position, View convertView, ViewGroup parent) {
				// Customize the row for list view
				if(convertView == null) {
					LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = layoutInflater.inflate(R.layout.row_demo, parent,false);
				}
				Project project = (Project) demoListAdapter.getItem(position);
				if (project != null) {

					TextView rowName = (TextView) convertView.findViewById(R.id.row_name);
					rowName.setText(project.getTitle());
					ImageView rowPhoto = (ImageView) convertView.findViewById(R.id.row_photo);
					loadProfileImage(project, rowPhoto);
					ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.fundingBar);
					progressBar.setProgress((int)Math.ceil(project.getPercentagePledged()));
					((TextView)convertView.findViewById(R.id.funded)).setText(String.valueOf(project.getPledgedAmount()));
					((TextView)convertView.findViewById(R.id.goal)).setText(String.valueOf(project.getGoal()));
					((TextView)convertView.findViewById(R.id.left)).setText(String.valueOf(project.getDaysRemaining()));

					int backers = project.getTotalBackers();
					if(backers>0)
					{
						if(backers == 1)
						{
							((TextView)convertView.findViewById(R.id.backers)).setText("This project has 1 backer.");
						}
						else
						{
							((TextView)convertView.findViewById(R.id.backers)).setText("This project has "+backers+" backers.");
						}

					}
					ViewHolder holder = new ViewHolder();
					holder.project = project;
					convertView.setTag(holder);
				}
				return convertView;
			}
		});

		demoListView.setAdapter(demoListAdapter);
		// Display a toast when a list item is clicked
		demoListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				ViewHolder holder = (ViewHolder)view.getTag();
				if(holder!=null && holder.project!=null)
				{
					Intent intent = new Intent(InfiniteScrollListViewDemoActivity.this,ProjectDetailActivity.class);
					intent.putExtra(Constants.PROJECT_ID, holder.project.getId());
					InfiniteScrollListViewDemoActivity.this.startActivity(intent);
				}
				//				handler.post(new Runnable() {
				//					@Override
				//					public void run() {
				//						Toast.makeText(InfiniteScrollListViewDemoActivity.this, demoListAdapter.getItem(position) + " " + getString(R.string.ordered), Toast.LENGTH_SHORT).show();
				//					}
				//				});
			}
		});
		//		loadingModeGroup.check(loadingMode == LoadingMode.SCROLL_TO_TOP ? R.id.infinite_listview_radio_scroll_to_top : R.id.infinite_listview_radio_scroll_to_bottom);
		//		loadingModeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//	        public void onCheckedChanged(RadioGroup group, int checkedId) {
		//	    		// Cancel a task if exists when loading mode changes
		//	        	if (fetchAsyncTask != null) {
		//	    			fetchAsyncTask.cancel(true);
		//	    		}
		//	        	// Reset fake remote service when loading mode changes
		//	        	bogusRemoteService.reset();
		//	        	// Remove loading view
		//	        	demoListAdapter.notifyEndOfList();
		//	        	// Clear all data points
		//	        	demoListAdapter.clearEntries();
		//	        	loadingMode = checkedId == R.id.infinite_listview_radio_scroll_to_top ? LoadingMode.SCROLL_TO_TOP : LoadingMode.SCROLL_TO_BOTTOM;
		//	        	// Overwrite new loading mode
		//	        	demoListAdapter.setLoadingMode(loadingMode);
		//	        	demoListView.setLoadingMode(loadingMode);
		//	        	// Disable row clicks when display messages
		//	        	demoListAdapter.setRowEnabled(checkedId == R.id.infinite_listview_radio_scroll_to_top ? false : true);
		//	        	// Manually loads the first page
		//	        	demoListAdapter.onScrollNext();
		//	        }
		//	    });
		//		stopPositionGroup.check(stopPosition == StopPosition.START_OF_LIST ? R.id.infinite_listview_radio_start_of_list : (stopPosition == StopPosition.END_OF_LIST ? R.id.infinite_listview_radio_end_of_list : R.id.infinite_listview_radio_remain_unchanged));
		//		stopPositionGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//	        public void onCheckedChanged(RadioGroup group, int checkedId) {
		//	    		// Cancel a task if exists when stop position setting changes
		//	        	if (fetchAsyncTask != null) {
		//	    			fetchAsyncTask.cancel(true);
		//	    		}
		//	        	// Reset fake remote service when stop position setting changes
		//	        	bogusRemoteService.reset();
		//	        	// Remove loading view
		//	        	demoListAdapter.notifyEndOfList();
		//	        	// Clear all data points
		//	        	demoListAdapter.clearEntries();
		//	        	stopPosition = checkedId == R.id.infinite_listview_radio_start_of_list ? StopPosition.START_OF_LIST : (checkedId == R.id.infinite_listview_radio_end_of_list ? StopPosition.END_OF_LIST : StopPosition.REMAIN_UNCHANGED);
		//	        	// Overwrite stop position setting
		//	        	demoListAdapter.setStopPosition(stopPosition);
		//	        	demoListView.setStopPosition(stopPosition);
		//	        	// Manually load the first page
		//	        	demoListAdapter.onScrollNext();
		//	        }
		//	    });



		// Check device for Play Services APK. If check succeeds, proceed with
		//  GCM registration.
		//        if (checkPlayServices()) {
		//            gcm = GoogleCloudMessaging.getInstance(this);
		//            regid = getRegistrationId(context);
		//
		//            if (regid.isEmpty()) {
		//                registerInBackground();
		//            }
		//        } else {
		//            Log.i(TAG, "No valid Google Play Services APK found.");
		//        }

	}

	public static class ViewHolder
	{
		Project project;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Load the first page to start demo
		demoListAdapter.onScrollNext();
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
			InfiniteScrollListViewDemoActivity.this.startActivity(intent);
			return true;
		case R.id.action_newsletters:
			intent.setClass(this.getApplicationContext(), NewsLetterActivity.class);
			InfiniteScrollListViewDemoActivity.this.startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private void registerInBackground() {
		new AsyncTask<Void,Void,String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your app.
					// The request to your server should be authenticated if your app
					// is using accounts.
					if(sendRegistrationIdToBackend())
					{

						// For this demo: we don't need to send it because the device
						// will send upstream messages to a server that echo back the
						// message using the 'from' address in the message.

						// Persist the regID - no need to register again.
						storeRegistrationId(context, regid);
					}
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				mDisplay.append(msg + "\n");
			}
		}.execute(null, null, null);
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private boolean sendRegistrationIdToBackend() {
		//TODO Your implementation here.
		return false;
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}


	private void loadProfileImage(Project project,ImageView imageView)
	{
		Log.d(TAG,"tweeter.profileimageurl="+project.getImageUrls().toString()+ "   imageView="+imageView.toString());

		if(project.getImageUrls()!=null && project.getImageUrls().size()>0)
		{
			mImageFetcher.loadImage(project.getImageUrls().get(0), imageView);
			//downloadBitmap(project.getImageUrls().get(0));
		}

	}
}