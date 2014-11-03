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

import com.tw.foodsocial.RefreshLayout.OnLoadListener;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class index extends ActionBarActivity implements OnClickListener{
	private Handler mHandler;
	private Thread mThreadid,mThreadpost;
	private Context mContext,tContext;
	private GlobalVariable gv;
	private HttpClient client,clientpost;
	private static JSONObject jObj,jObjPost;
	private List<foodItem> foodArray;
	private foodItemAdapter fItemAdapter;
	private ListView LV_index;
	private Button postBtn;
	private Spinner SP_index_type;
	private String[] type = {"postWall","group"};
	private ArrayAdapter<String> typeList;
	private RefreshLayout RFL_index;
	private int refreshCount = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.index);
		init();
		fItemAdapter = new foodItemAdapter(tContext ,R.layout.fooditem, foodArray);
		LV_index.setAdapter(fItemAdapter);
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
        				if(!foodArray.isEmpty())
        					foodArray.clear();
        				if(jObjPost.isNull("stat"))
        					break;
        				try {
        					for(int i = 0 ; i < jObjPost.getJSONArray("result").length();i++){
        						foodItem fItem = new foodItem();
        						fItem.setPostID(jObjPost.getJSONArray("result").getJSONObject(i).getInt("postID"));
        						fItem.setTitle(jObjPost.getJSONArray("result").getJSONObject(i).getString("title"));
        						fItem.setAddress(jObjPost.getJSONArray("result").getJSONObject(i).getString("address"));
        						fItem.setContent(jObjPost.getJSONArray("result").getJSONObject(i).getString("content"));
        						fItem.setPoster(jObjPost.getJSONArray("result").getJSONObject(i).getInt("poster"));
        						fItem.setRecommendBy(jObjPost.getJSONArray("result").getJSONObject(i).getInt("recommendBy"));
        						fItem.setPostTime(jObjPost.getJSONArray("result").getJSONObject(i).getJSONObject("postTime").getLong("$date"));
        						fItem.setPosterName(jObjPost.getJSONArray("result").getJSONObject(i).getString("posterName"));
        						fItem.setRecommendByName(jObjPost.getJSONArray("result").getJSONObject(i).getString("recommendByName"));
        						foodArray.add(fItem);
        					}
        					fItemAdapter.refresh(foodArray);
        					Log.d("foodArray", foodArray.get(1).getContent());
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
        /*try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        fItemAdapter = new foodItemAdapter(tContext ,R.layout.fooditem, foodArray);
		Log.d("foodArray", "QQ");
		LV_index.setAdapter(fItemAdapter);*/
	}
	
	private void init(){
		mContext = this.getApplicationContext();
		tContext = this.getBaseContext();
        gv = (GlobalVariable) mContext.getApplicationContext();
        foodArray = new ArrayList<foodItem>();
        LV_index = (ListView) this.findViewById(R.id.LV_index);
        RFL_index = (RefreshLayout) this.findViewById(R.id.RFL_index);
        RFL_index_listener();
        listViewClick();
        postBtn = (Button) this.findViewById(R.id.BT_index_post);
        postBtn.setOnClickListener(this);
        SP_index_type = (Spinner) this.findViewById(R.id.SP_index_type);
        typeList = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_dropdown_item,type);
        SP_index_type.setAdapter(typeList);
	}
	
	private void RFL_index_listener() {
		// TODO Auto-generated method stub
		RFL_index.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				Toast.makeText(index.this, "refresh" , Toast.LENGTH_SHORT).show();
				
				RFL_index.postDelayed(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						reloadList(1);
						RFL_index.setRefreshing(false);
					}
					
				}, 1000);
			}
			
		});
		
		RFL_index.setOnLoadListener(new OnLoadListener(){

			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				Toast.makeText(index.this, "load" , Toast.LENGTH_SHORT).show();
				
				RFL_index.postDelayed(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						refreshCount++;
						reloadList(refreshCount);
						RFL_index.setLoading(false);
					}
					
				}, 1500);
			}
			
		});
	}

	private void reloadList(int inputCount){
		followWallid(inputCount);
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
						Log.d("postID",	"result:"+jObj.getJSONArray("result").getJSONObject(0).getInt("postID"));
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
					Log.d("fwE1", e.getMessage().toString());
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
					Log.d("fwE2", e.getMessage().toString());
				}
				finally{
					clientpost.getConnectionManager().shutdown();
				}
			}
    	});
    	mThreadpost.start();
    }
	
	public void listViewClick(){
		LV_index.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				foodItemAdapter tempAdapter = (foodItemAdapter) parent.getAdapter();
				Toast.makeText(index.this, "id:"+tempAdapter.getFoodItem((int) id).getPostID(), Toast.LENGTH_LONG).show();
				
				return false;
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==postBtn){
			Intent i = new Intent();
			i.setClass(index.this, postPage.class);
			startActivity(i);
			finish();
		}
	}

}
