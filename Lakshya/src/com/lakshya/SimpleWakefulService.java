package com.lakshya;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class SimpleWakefulService extends IntentService {
	
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	
	
    public SimpleWakefulService() {
        super("SimpleWakefulService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // At this point SimpleWakefulReceiver is still holding a wake lock
        // for us.  We can do whatever we need to here and then tell it that
        // it can release the wakelock.  This sample just does some slow work,
        // but more complicated implementations could take their own wake
        // lock here before releasing the receiver's.
        //
        // Note that when using this approach you should be aware that if your
        // service gets killed and restarted while in the middle of such work
        // (so the Intent gets re-delivered to perform the work again), it will
        // at that point no longer be holding a wake lock since we are depending
        // on SimpleWakefulReceiver to that for us.  If this is a concern, you can
        // acquire a separate wake lock here.
        for (int i=0; i<5; i++) {
            Log.i("SimpleWakefulReceiver", "Running service " + (i+1)
                    + "/5 @ " + SystemClock.elapsedRealtime());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
        Log.i("SimpleWakefulReceiver", "Completed service @ " + SystemClock.elapsedRealtime());
        
		mNotificationManager = (NotificationManager)
				Projects.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent launchIntent = new Intent(Projects.mContext, ProjectDetailActivity.class);
		intent.putExtra(Constants.LAUNCHED_FROM_NOTIFICATIONS, true);
		intent.putExtra(Constants.PROJECT_ID, 2);
		PendingIntent contentIntent = PendingIntent.getActivity(Projects.mContext, 0,
				launchIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(Projects.mContext)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("push notification")
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText("push received"))
		.setContentText("push content");

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		
		
        SimpleWakefulReceiver.completeWakefulIntent(intent);
    }
}