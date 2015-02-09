package com.lakshya;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;

import com.lakshya.data.Project;

public class Projects extends Application 
{
	
	// A placeholder for all the data points
	public static List<Project> entries = new ArrayList<Project>();
	
	public static Context mContext;
	
	private static void init(Context context)
	{
		mContext = context;
	}
	
	@Override
    public void onCreate() 
	{
        super.onCreate();
        init(this.getApplicationContext());
	}
}
