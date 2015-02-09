package com.lakshya;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FTUIntroFragment extends WorxTaskBaseFragment
{

	@Override
	public void onActivityBackPress()
	{}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	
		return inflater.inflate(R.layout.ftu_intro_layout, null);
	}
}
