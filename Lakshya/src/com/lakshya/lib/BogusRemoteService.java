/**
 *
 * Copyright 2013 Wei Xiao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.lakshya.lib;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lakshya.NetworkHelper;
import com.lakshya.data.Project;
import com.lakshya.data.ProjectListResponse;

/**
 * Mimic the server side logic to return results with some delay
 */
public class BogusRemoteService {

	private static final String TAG = BogusRemoteService.class.getSimpleName();

	// Dummy data
	private String[] sushiEntries = new String[] {"Akaki", "Ama Ebi", "Anago", "Kuruma Ebi", "Hamachi", "Hirame", 
			"Hokki", "Hotate", "Ika", "Ikura", "Inari", "Kaibashira", "Kaki", "Maguro", "Hon Maguro", 
			"Maguro Toro", "Masago", "Mirugai", "Saba", "Sake", "Tai", "Tako", "Tamago", "Tobiko", 
			"Torigai", "Unagi", "Uni"};
	private String[] messageEntries = new String[] {"Hello sir", "Hello officer", "Your driver's license and registration please", "Here you are sir", "Is the address on your license current", "No", "Where do you live?", "With my parents", "Where does your parents live?", "With me", 
			"Where do you all live", "Together", "Where is your house?", "Next to my neighours' house", "Where is your neigbours' house?", 
			"If I tell you, you won't believe me", "Tell me", "Next to my house"};

	private int sushiIdx = 0;
	private int messageIdx = 0;

	public List<Project> getNextSushiBatch(Context context,int batchSize) {
		
		
		String json = NetworkHelper.GET("http://crowdfund.thelakshyafoundation.org/_project/list?start_index=0&num_projects=5");
		
//		String json = null;
//	    try {
//
//	        InputStream is = context.getAssets().open("projectDetails.json");
//
//	        int size = is.available();
//
//	        byte[] buffer = new byte[size];
//
//	        is.read(buffer);
//
//	        is.close();
//
//	        json = new String(buffer, "UTF-8");
//
//
//	    } catch (IOException ex) {
//	        ex.printStackTrace();
//	    }
		
		//The received data contains an inner join of tweets and tweet users. 
		//Read them both.
		Gson gson = new Gson();

//		Type collectionType = new TypeToken<ArrayList<Project>>(){}.getType();
//		ArrayList<Project> results = gson.fromJson(json, collectionType);
		
		Type projectListType = new TypeToken<ProjectListResponse>(){}.getType();
		ProjectListResponse result = gson.fromJson(json, projectListType);
		
		if(result!=null)
		{
			return result.getProjects();
		}

		return null;
	}

	
	public List<String> getNextMessageBatch(int batchSize) {
		List<String> results = new ArrayList<String>();
    	try {
			for (int i = messageIdx; i < (messageIdx + batchSize < messageEntries.length ? messageIdx + batchSize : messageEntries.length); i++) {
		    	results.add(messageEntries[i]);
				if (messageIdx != 0) {
					// Delay when retrieving each item
					Thread.sleep(400L);
				}
		    }
			// Keep track where the pointer is
	    	messageIdx = messageIdx + batchSize;
		} catch (InterruptedException e) {
			// Reset pointers when user stops the current call
			messageIdx = 0;
			results.clear();
			Log.i(TAG, "Message service interrupted");
		}
		return results;
	}
	
	public void reset() {
		// Reset both pointers
		sushiIdx = 0;
		messageIdx = 0;
	}

}