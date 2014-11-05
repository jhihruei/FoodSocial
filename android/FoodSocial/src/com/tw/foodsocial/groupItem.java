package com.tw.foodsocial;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class groupItem {
	private Context tContext;
	private String groupName;
	private ArrayList<Integer> groupPostIDs;
	private ArrayList<foodItem> groupFoodItems;
	private GlobalVariable gv;
	
	public groupItem(Context context){
		this.groupName = "";
		this.groupPostIDs = new ArrayList<Integer>();
		this.groupFoodItems = new ArrayList<foodItem>();
		this.tContext = context;
		this.gv = (GlobalVariable) tContext.getApplicationContext();
	}
	
	public groupItem(Context context,String iName,ArrayList<Integer> iIDs){
		this.groupName = iName;
		this.groupPostIDs = iIDs;
		this.groupFoodItems = new ArrayList<foodItem>();
		this.tContext = context;
		this.gv = (GlobalVariable) tContext.getApplicationContext();
	}
	
	public void setGroupName(String iName){
		this.groupName = iName;
	}
	
	public void setID(int iID){
		this.groupPostIDs.add(iID);
	}
	
	public void setIDs(ArrayList<Integer> iIDs){
		this.groupPostIDs.addAll(iIDs);
	}
	
	public String getGroupName(){
		return this.groupName;
	}
	
	public ArrayList<Integer> getIDs(){
		return this.groupPostIDs;
	}
	
	public void setFoodItems(){
		final JSONArray inputArray = new JSONArray(this.groupPostIDs);
		final HttpClient clientpost = new DefaultHttpClient();
		Thread mThreadpost = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
	    			String url = gv.getUrl()+"getPostByID";
	    	        HttpPost post = new HttpPost(url);
	    	        JSONObject postData = new JSONObject();
	    	        postData.put("postIDarray", inputArray);
	    	        post.setEntity(new StringEntity(postData.toString()));
	    	        post.setHeader("Content-type", "application/json");
	    	        HttpResponse rp = clientpost.execute(post);
					if(rp.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(rp.getEntity());
						JSONObject jObjPost = new JSONObject(result);
						Log.d("result", "stat:"+jObjPost.getString("stat"));
						//Log.d("postID",	"result:"+jObjPost.getJSONArray("result"));
						//Log.d("threadfw", "thread="+Thread.currentThread().getName());
						//Message msg_t = new Message();
						//msg_t.what = 2;
						//mHandler.handleMessage(msg_t);
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
    						groupFoodItems.add(fItem);
    					}
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
	
	public foodItem getFoodItem(int i){
		return this.groupFoodItems.get(i);
	}
	
	public Boolean checkFoodItemEmpty(){
		return this.groupFoodItems.isEmpty();
	}
}
