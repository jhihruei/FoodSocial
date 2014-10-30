package com.tw.foodsocial;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class postPage extends Activity implements OnClickListener{
	private Button postBtn_back,postBtn_post,postBtn_goIndex;
	private EditText postET_title,postET_address,postET_content;
	private Context mContext;
	private Handler mHandler;
	private Thread mThread;
	private GlobalVariable gv;
	private HttpClient client;
	private static JSONObject jObj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.postpage);
		init();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 1:
					gotoIndex();
					finish();
					break;
				}
				super.handleMessage(msg);
			}
			
		};
		
	}

	private void init(){
		mContext = this.getApplicationContext();
        gv = (GlobalVariable) mContext.getApplicationContext();
		postBtn_back = (Button) this.findViewById(R.id.BT_post_back);
		postBtn_post = (Button) this.findViewById(R.id.BT_post_post);
		postBtn_goIndex = (Button) this.findViewById(R.id.BT_post_index);
		postBtn_back.setOnClickListener(this);
		postBtn_post.setOnClickListener(this);
		postBtn_goIndex.setOnClickListener(this);
		postET_title = (EditText) this.findViewById(R.id.ET_post_title);
		postET_address = (EditText) this.findViewById(R.id.ET_post_address);
		postET_content = (EditText) this.findViewById(R.id.ET_post_content);
	}
	
	private void postToApi(){
		mThread = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
	    			int userID = gv.getUserID();
	    			client = new DefaultHttpClient();
	    			String url = gv.getUrl()+"post";
	    	        HttpPost post = new HttpPost(url);
	    	        JSONArray pJArray = new JSONArray();
	    	        pJArray.put("test.png");
	    	        JSONArray lJArray = new JSONArray();
	    	        lJArray.put(10);
	    	        lJArray.put(20);
	    	        JSONObject postData = new JSONObject();
	    	        postData.put("poster", userID);
	    	        postData.put("recommendBy", userID);
	    	        postData.put("title", postET_title.getText().toString());
	    	        postData.put("address", postET_address.getText().toString());
	    	        postData.put("latitude", lJArray);
	    	        postData.put("content", postET_content.getText().toString());
	    	        postData.put("picture", pJArray);
	    	        post.setEntity(new StringEntity(postData.toString()));
	    	        post.setHeader("Content-type", "application/json");
	    	        HttpResponse rp = client.execute(post);
					if(rp.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(rp.getEntity());
						jObj = new JSONObject(result);
						Log.d("Postresult", "stat:"+jObj.getString("stat"));
						//Log.d("postID",	"result:"+jObj.getJSONArray("result").getJSONObject(1).getInt("postID"));
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
    	mThread.start();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == postBtn_back || v == postBtn_goIndex){
			gotoIndex();
			finish();
		}
		else if(v == postBtn_post){
			postToApi();
			//finish();
		}
	}

	public void gotoIndex(){
		Intent i = new Intent();
		i.setClass(postPage.this, index.class);
		startActivity(i);
	}
}
