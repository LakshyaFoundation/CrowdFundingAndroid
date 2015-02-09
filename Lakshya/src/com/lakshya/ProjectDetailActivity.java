package com.lakshya;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imagedisplay.util.ImageFetcher;
import com.imagedisplay.util.Utils;
import com.lakshya.data.Project;

public class ProjectDetailActivity extends BaseActivity
{
	
	public static final String TAG = "ProjectDetailActivity";
	private ImageFetcher mImageFetcher; //Fetches the images

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_detail_layout);
		
		mImageFetcher = Utils.getImageFetcher(this, 60, 60);

		int projectId = getIntent().getIntExtra(Constants.PROJECT_ID, -1);
		Project project = null;

		for(Project pj :Projects.entries)
		{
			if(pj.getId() == projectId)
			{
				project = pj;
				break;
			}
		}
		
		if(project!=null)
		{

			TextView rowName = (TextView) findViewById(R.id.row_name);
			rowName.setText(project.getTitle());
			ImageView rowPhoto = (ImageView) findViewById(R.id.row_photo);
			loadProfileImage(project, rowPhoto);
			ProgressBar progressBar = (ProgressBar)findViewById(R.id.fundingBar);
			progressBar.setProgress((int)Math.ceil(project.getPercentagePledged()));
			((TextView)findViewById(R.id.funded)).setText(String.valueOf(project.getPledgedAmount()));
			((TextView)findViewById(R.id.goal)).setText(String.valueOf(project.getGoal()));
			((TextView)findViewById(R.id.left)).setText(String.valueOf(project.getDaysRemaining()));
			((TextView)findViewById(R.id.rules_and_challengers_text)).setText(String.valueOf(project.getRisksAndChallenges()));
			((TextView)findViewById(R.id.description_text)).setText(String.valueOf(project.getDescription()));

			int backers = project.getTotalBackers();
			if(backers>0)
			{
				if(backers == 1)
				{
					((TextView)findViewById(R.id.backers)).setText("This project has 1 backer.");
				}
				else
				{
					((TextView)findViewById(R.id.backers)).setText("This project has "+backers+" backers.");
				}

			}

		}
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
