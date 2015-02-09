/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imagedisplay.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.joda.time.DateTime;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.TypedValue;

import com.google.common.base.CharMatcher;
import com.lakshya.Constants;
import com.lakshya.R;

/**
 * Class containing some static utility methods.
 */
public class Utils {
    private Utils() {};


//    @TargetApi(VERSION_CODES.HONEYCOMB)
//    public static void enableStrictMode() {
//        if (Utils.hasGingerbread()) {
//            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
//                    new StrictMode.ThreadPolicy.Builder()
//                            .detectAll()
//                            .penaltyLog();
//            StrictMode.VmPolicy.Builder vmPolicyBuilder =
//                    new StrictMode.VmPolicy.Builder()
//                            .detectAll()
//                            .penaltyLog();
//
//            if (Utils.hasHoneycomb()) {
//                threadPolicyBuilder.penaltyFlashScreen();
//                vmPolicyBuilder
//                        .setClassInstanceLimit(ImageGridActivity.class, 1)
//                        .setClassInstanceLimit(ImageDetailActivity.class, 1);
//            }
//            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
//            StrictMode.setVmPolicy(vmPolicyBuilder.build());
//        }
//    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
    }
    
    
    public static Bitmap getRoundedBitmap(Bitmap bitmap) 
    {
    	Bitmap output = null;
    	
    	if (bitmap != null) {
    		output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);
         
            final int color = Color.RED;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
    	}
        
        return output;
    }
    
    public static Bitmap getInitialsBitmap(Context context,String initials)
    {
    	 int width = (int)context.getResources().getDimension(R.dimen.quick_contact_width);
    	 int height = (int)context.getResources().getDimension(R.dimen.quick_contact_height);
    	 final Bitmap output = Bitmap.createBitmap((int)width,(int)height, Bitmap.Config.ARGB_8888);
         final Canvas canvas = new Canvas(output);
      
         final int color = Color.WHITE;
         final Paint paint = new Paint();
         paint.setAntiAlias(true);
         canvas.drawARGB(255, 204, 204, 204);
         paint.setColor(color);
         paint.setTextAlign(Align.CENTER);
         paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
         int textSize = (int)context.getResources().getDimension(R.dimen.initials_textSize);
         paint.setTextSize(textSize);
         paint.setTypeface(Typeface.DEFAULT);
         Rect bounds = new Rect();
         paint.getTextBounds(initials, 0, initials.length(), bounds);
         canvas.drawText(initials,width/2, (height+Math.abs(bounds.height()))/2, paint);
         return output;
    }
    
    
    public static boolean isValidUrl(String data)
    {
    	boolean returnValue = false;
        try {
			final URL url = new URL(data);
			returnValue = true;
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return returnValue;
    }
    
    public static boolean isValidInitials(String data)
    {
    	return true;
    }
    
	public static String getInitials(String name)
	{
		String initials = "";
		if ((name != null) && (name.length() > 0))
		{
			String firstName =  getFirstName(name);
			String secondname = getSecondName(name);
			CharMatcher az = CharMatcher.inRange('a', 'z');
			CharMatcher AZ = CharMatcher.inRange('A', 'Z');
			if(!TextUtils.isEmpty(firstName) && (az.matches(firstName.charAt(0)) || AZ.matches(firstName.charAt(0))))
			{
				if(!TextUtils.isEmpty(secondname))
				{
					initials = initials + firstName.charAt(0) + secondname.charAt(0);
				}
				else
				{
					initials = initials + firstName.charAt(0);
				}
				
			}		
			return initials.toUpperCase(Locale.getDefault());
		}
		else
		{
			return initials;
		}
	}

	private static String getSecondName(String name)
	{
		name = name.trim();
    	int index = name.indexOf(",");
    	if(index != -1)
    	{
    		return name.split(",")[0].trim();
    	}
    	else
    	{
    		String[] names = name.split(" ");
    		int len = names.length;
    		if(len > 1)
    		{
    			CharMatcher az = CharMatcher.inRange('a', 'z');
    			CharMatcher AZ = CharMatcher.inRange('A', 'Z');
    			for(int i = (len - 1); i > 0;i--)
    			{
    				if(!TextUtils.isEmpty(names[i]))
    				{
    					if(az.matches(names[i].charAt(0)) || AZ.matches(names[i].charAt(0)))
    					{
        					return names[i].trim();
        				}
    				}    				
    			}
    		}
    		return "";
    	}
	}
	
    public static String getFirstName(String name)
	{
    	name = name.trim();
    	int index = name.indexOf(",");
    	if(index != -1)
    	{
    		String[] commaSeparated =  name.split(",");
    		if(commaSeparated.length > 1)
    		{
    			String[] spaceSeparated = commaSeparated[1].trim().split(" ");
    			return spaceSeparated[0].trim();
    		}
    		else
    		{
    			return commaSeparated[0].trim();
    		}
    	}
    	else
    	{
    		return name.split(" ")[0].trim();
    	}
		
	}
    
    public static String getTweetHandle(String name)
	{
    	name = name.trim();
    	if(!TextUtils.isEmpty(name))
    	{
    		name = "@"+name;
    	}
    	return name;
	}
    
    public static String getTime(String dateTimeString)
    {
    	long currentSeconds = System.currentTimeMillis()/1000;
    	DateTime dt = new DateTime(dateTimeString);
    	long tweetSeconds = dt.getMillis()/1000;
    	long diff = currentSeconds - tweetSeconds;
    	
    	long days = diff/86400;
    	long hours = diff/3600;
    	long minutes = diff/60;
    	
    	if(days > 0)
    	{
    		return days+ " days ago";
    	}
    	else if(hours > 0)
    	{
    		return hours+ " hrs ago";
    	}
    	else if(minutes > 0)
    	{
    		return minutes+ " min ago";
    	}
    	else {
    		return diff+ " sec ago";
    	}

    }
    
    
    
    public static ImageFetcher getImageFetcher(FragmentActivity activity,float width, float height)
    {
		ImageCache.ImageCacheParams cacheParams =
				new ImageCache.ImageCacheParams(activity, Constants.IMAGE_CACHE_DIR);
		

		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

		// The ImageFetcher takes care of loading images into our ImageView children asynchronously
		Resources r = activity.getResources();
		int widthPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, r.getDisplayMetrics());
		int heightPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, r.getDisplayMetrics());
		
		ImageFetcher mImageFetcher = new ImageFetcher(activity, widthPx, heightPx);
		mImageFetcher.setLoadingImage(R.drawable.ic_launcher);
		mImageFetcher.addImageCache(activity.getSupportFragmentManager(), cacheParams);
		return mImageFetcher;
    }

    
}
