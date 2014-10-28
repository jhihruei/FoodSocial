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
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class index extends ActionBarActivity{
	private Handler mHandler;
	private Thread mThreadid,mThreadpost;
	private Context mContext,tContext;
	private GlobalVariable gv;
	private HttpClient client,clientpost;
	private static JSONObject jObj,jObjPost;
	private List<foodItem> foodArray;
	private foodItemAdapter fItemAdapter;
	private ListView LV_index;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		init();
		mHandler = new Handler(){
        	public void handleMessage(Message msg){
        		switch(msg.what){
        			case 1://取完postID存入JSONArray
        				JSONArray jArray = new JSONArray();
        				if(jObj.isNull("stat"))
        					break;
        				try {
        						for(int i = 0; i < jObj.getJSONArray("result").length();i++){
        							jArray.put(jObj.getJSONArray("result").getJSONObject(i).getInt("postID"));
        						}
        						Log.d("jArray", "jArray:"+jArray.toString());
        						followWallpost(jArray);
        						break;
        				} catch (JSONException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        				break;
        			case 2://取完post存入foodItem array
        				if(jObjPost.isNull("stat"))
        					break;
        				try {
        					for(int i = 0 ; i < jObjPost.getJSONArray("result").length();i++){
        						foodItem fItem = new foodItem();
        						fItem.setTitle(jObjPost.getJSONArray("result").getJSONObject(i).getString("title"));
        						fItem.setAddress(jObjPost.getJSONArray("result").getJSONObject(i).getString("address"));
        						fItem.setContent(jObjPost.getJSONArray("result").getJSONObject(i).getString("content"));
        						fItem.setPoster(jObjPost.getJSONArray("result").getJSONObject(i).getInt("poster"));
        						fItem.setRecommendBy(jObjPost.getJSONArray("result").getJSONObject(i).getInt("recommendBy"));
        						foodArray.add(fItem);
        					}
        					Log.d("foodArray", foodArray.get(1).getContent());
        					//fItemAdapter = new foodItemAdapter(tContext ,R.layout.fooditem, foodArray);
        					//Log.d("foodArray", "QQ");
        					//LV_index.setAdapter(fItemAdapter);
        					break;
        				} catch (JSONException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        				break;
        		}		
        		super.handleMessage(msg);
        	}
        };
        followWallid(1);
        fItemAdapter = new foodItemAdapter(tContext ,R.layout.fooditem, foodArray);
		Log.d("foodArray", "QQ");
		LV_index.setAdapter(fItemAdapter);
	}
	
	private void init(){
		mContext = this.getApplicationContext();
		tContext = this.getBaseContext();
        gv = (GlobalVariable) mContext.getApplicationContext();
        foodArray = new ArrayList<foodItem>();
        LV_index = (ListView) this.findViewById(R.id.LV_index);
	}

	private void followWallid(final int inputCount){//
    	mThreadid = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
	    			int userID = gv.getUserID();
	    			client = new DefaultHttpClient();
	    			String url = gv.getUrl()+"getPostID";
	    	        HttpPost post = new HttpPost(url);
	    	        JSONObject postData = new JSONObject();
	    	        postData.put("fun", "userID");
	    	        postData.put("userID", userID);
	    	        postData.put("count", inputCount);
	    	        post.setEntity(new StringEntity(postData.toString()));
	    	        post.setHeader("Content-type", "application/json");
	    	        HttpResponse rp = client.execute(post);
					if(rp.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(rp.getEntity());
						jObj = new JSONObject(result);
						Log.d("result", "stat:"+jObj.getString("stat"));
						Log.d("postID",	"result:"+jObj.getJSONArray("result").getJSONObject(1).getInt("postID"));
						Message msg_t = new Message();
						msg_t.what = 1;
						mHandler.handleMessage(msg_t);
					}
					else{
						Log.d("result", "Not 200");
					}
				} catch (ClientProtocolException e) {
					Log.d("cPE", e.getMessage().toString());
				} catch (IOException e) {
					Log.d("IOE", e.getMessage().toString());
				} catch (Exception e){
					Log.d("E", e.getMessage().toString());
				}
				finally{
					client.getConnectionManager().shutdown();
				}
			}
    	});
    	mThreadid.start();
    }
	
	private void followWallpost(final JSONArray inputArray){//
    	mThreadpost = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
	    			clientpost = new DefaultHttpClient();
	    			String url = gv.getUrl()+"getPostByID";
	    	        HttpPost post = new HttpPost(url);
	    	        JSONObject postData = new JSONObject();
	    	        postData.put("postIDarray", inputArray);
	    	        post.setEntity(new StringEntity(postData.toString()));
	    	        post.setHeader("Content-type", "application/json");
	    	        HttpResponse rp = clientpost.execute(post);
					if(rp.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(rp.getEntity());
						jObjPost = new JSONObject(result);
						Log.d("result", "stat:"+jObjPost.getString("stat"));
						Log.d("postID",	"result:"+jObjPost.getJSONArray("result"));
						Message msg_t = new Message();
						msg_t.what = 2;
						mHandler.handleMessage(msg_t);
					}
					else{
						Log.d("result", "Not 200");
					}
				} catch (ClientProtocolException e) {
					Log.d("cPE", e.getMessage().toString());
				} catch (IOException e) {
					Log.d("IOE", e.getMessage().toString());
				} catch (Exception e){
					Log.d("E", e.getMessage().toString());
				}
				finally{
					clientpost.getConnectionManager().shutdown();
				}
			}
    	});
    	mThreadpost.start();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//new MenuInflater(this).inflate(R.menu.option, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		//new MenuInflater(this).inflate(R.menu.context, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}
	
}
