package com.tw.foodsocial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class foodItemAdapter extends ArrayAdapter<foodItem>{

	private int resource;
	private List<foodItem> fItems;
	private JSONObject jObj;
	private HttpClient client;
	private GlobalVariable gv;
	
	public foodItemAdapter(Context context, int resource,
			List<foodItem> fItems) {
		super(context, resource, fItems);
		// TODO Auto-generated constructor stub
		this.resource = resource;
		this.fItems = fItems;
		gv = (GlobalVariable) context.getApplicationContext();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout foodItemView;
		final foodItem fItem = getFoodItem(position);
		
		if(convertView == null){
			foodItemView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, foodItemView, true);
		}
		else{
			foodItemView = (LinearLayout) convertView;
		}
		
		TextView TV_foodItem_title = (TextView) foodItemView.findViewById(R.id.TV_foodItem_title);
		TextView TV_foodItem_address = (TextView) foodItemView.findViewById(R.id.TV_foodItem_address);
		TextView TV_foodItem_content = (TextView) foodItemView.findViewById(R.id.TV_foodItem_content);
		TextView TV_foodItem_postTime = (TextView) foodItemView.findViewById(R.id.TV_foodItem_postTIme);
		TextView TV_foodItem_posterName = (TextView) foodItemView.findViewById(R.id.TV_foodItem_poster);
		TextView TV_foodItem_recommendByName = (TextView) foodItemView.findViewById(R.id.TV_foodItem_recommendBy);
		Button BT_addGroup = (Button) foodItemView.findViewById(R.id.BT_foosItem_addGroup);
		
		TV_foodItem_title.setText(fItem.getTitle());
		TV_foodItem_address.setText(fItem.getAddress());
		TV_foodItem_content.setText(fItem.getContent());
		TV_foodItem_postTime.setText(fItem.getPostTimeString());
		TV_foodItem_posterName.setText(fItem.getPosterName());
		TV_foodItem_recommendByName.setText(fItem.getRecommendByName());
		
		BT_addGroup.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog addGroup = new Dialog(v.getContext()); 
	    		addGroup.setTitle(fItem.getTitle());
	    		addGroup.setContentView(R.layout.group_dialog);
	    		
	    		Button btnEnter = (Button) addGroup.findViewById(R.id.BT_groupdialog_enter);
	    		Button btnCancel = (Button) addGroup.findViewById(R.id.BT_groupdialog_cancel);
	    		final EditText txtNewGroup = (EditText) addGroup.findViewById(R.id.ET_groupdialog_add);
	    		ListView LV_groupdialog = (ListView) addGroup.findViewById(R.id.LV_groupdialog);
	    		
	    		ArrayList<String> strs = new ArrayList<String>();
	    		for(int i = 0 ; i < gv.getGroups().size() ;i++){
	    			strs.add(gv.getGroups().get(i).getGroupName());
	    		}
	    		ArrayAdapter<String> adapter_groupdialog = new ArrayAdapter<String>(v.getContext(),android.R.layout.simple_list_item_1,strs);
	    		LV_groupdialog.setAdapter(adapter_groupdialog);
	    		
	    		LV_groupdialog.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						ArrayAdapter<String> tempAdapter = (ArrayAdapter<String>) parent.getAdapter();
						addFav(tempAdapter.getItem((int) id),fItem.getPostID());
						Toast.makeText(view.getContext(), "id:"+tempAdapter.getItem((int) id), Toast.LENGTH_LONG).show();
						addGroup.dismiss();
					}
	    			
	    		});
	    		
	    		btnEnter.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String newGroupName = txtNewGroup.getText().toString();
						Log.d("groupName", newGroupName);
						if(!(newGroupName.length()==0)){
							addFav(newGroupName,fItem.getPostID());
							addGroup.dismiss();
						}
						else{
							Toast.makeText(v.getContext(), "請輸入新群組名稱", Toast.LENGTH_LONG).show();
						}
					}
	    			
	    		});
	    		
	    		btnCancel.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						addGroup.dismiss();
					}
	    		});
	    		
	    		addGroup.show();
			}
		});
		
		return foodItemView;
	}

	public foodItem getFoodItem(int index){
		return fItems.get(index);
	}
	
	public void setFoodItem(List<foodItem> inputfItems){
		fItems.addAll(inputfItems);
		this.notifyDataSetChanged();
	}
	
	public void refresh(List<foodItem> newfItems){
		try{
			this.fItems = newfItems;
			this.notifyDataSetChanged();
		}catch(Exception e){
			Log.d("e", e.toString());
		}
	}
	
	public void addFav(final String addTargetGroupName,final int postID){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(postID);
		gv.setGroup(addTargetGroupName, temp);
		Thread mThreadid = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					GlobalVariable gv = (GlobalVariable) getContext().getApplicationContext();
	    			int userID = gv.getUserID();
	    			client = new DefaultHttpClient();
	    			String url = gv.getUrl()+"Favorites/add";
	    	        HttpPost post = new HttpPost(url);
	    	        JSONObject postData = new JSONObject();
	    	        JSONArray postIDarray = new JSONArray();
	    	        postIDarray.put(postID);
	    	        postData.put("userID", userID);
	    	        postData.put("groupName", addTargetGroupName);
	    	        postData.put("postIDarray", postIDarray);
	    	        post.setEntity(new StringEntity(postData.toString(),"UTF-8"));
	    	        post.setHeader("Content-type", "application/json");
	    	        HttpResponse rp = client.execute(post);
					if(rp.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(rp.getEntity());
						jObj = new JSONObject(result);
						Log.d("result", "stat:"+jObj.getString("stat"));
						//Log.d("postID",	"result:"+jObj.getJSONArray("result").getJSONObject(0).getInt("postID"));
						//Message msg_t = new Message();
						//msg_t.what = 1;
						//mHandler.handleMessage(msg_t);
					}
					else{
						Log.d("result", "Not 200");
					}
				} catch (ClientProtocolException e) {
					Log.d("cPE", e.getMessage().toString());
				} catch (IOException e) {
					Log.d("IOE", e.getMessage().toString());
				} catch (Exception e){
					Log.d("fwE1", e.getMessage().toString());
				}
				finally{
					client.getConnectionManager().shutdown();
				}
			}
    	});
    	mThreadid.start();
	}

	@Override
	public foodItem getItem(int position) {
		// TODO Auto-generated method stub
		return fItems.get(position);
	}
}
