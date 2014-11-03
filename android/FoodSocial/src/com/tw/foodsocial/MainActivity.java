package com.tw.foodsocial;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Session;
import com.facebook.SessionState;

import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements OnClickListener{
	private Button buttonAccount,buttonFB,buttonSignUp;
	private Handler mHandler;
	private Thread mThread,mGThread;
	private Context mContext;
	private GlobalVariable gv;
	private HttpClient client,Gclient;
	private static JSONObject jObj,GjObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
        mHandler = new Handler(){
        	public void handleMessage(Message msg){
        		switch(msg.what){
        			case 1:
        				//gotoIndex();
        				setGroups();
        				break;
        			case 2: 
        				onClick(buttonAccount);
        				break;
        			case 3:
					try {
						if(GjObj.getInt("stat") == 1){
        					try {
								for(int i = 0 ; i < GjObj.getJSONArray("result").length() ; i++){
									ArrayList<Integer> intArray = new ArrayList<Integer>();
									for(int j = 0 ; j < GjObj.getJSONArray("result").getJSONObject(i).getJSONArray("groups").length();j++){
										intArray.add(GjObj.getJSONArray("result").getJSONObject(i).getJSONArray("groups").getInt(j));
									}
									gv.setGroup(GjObj.getJSONArray("result").getJSONObject(i).getString("groupName"), intArray);
									Log.d("groupArray", gv.getGroups().get(0).getIDs().toString());
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
        				}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        				gotoIndex();
        				break;
        		}		
        		super.handleMessage(msg);
        	}
        };
        if(Session.openActiveSessionFromCache(mContext)!=null)//開啟app後抓FB cache如果有就直接進入index
        	gotoIndex();
    }
    
    private void init(){
    	buttonAccount = (Button)this.findViewById(R.id.buttonAccount);
        buttonFB = (Button)this.findViewById(R.id.buttonFB);
        buttonSignUp = (Button)this.findViewById(R.id.buttonSignUp);
        buttonAccount.setOnClickListener(this);
        buttonFB.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
        mContext = this.getApplicationContext();
        gv = (GlobalVariable) mContext.getApplicationContext();
    }

    private void accountLogin(final String inputUser,final String inputPassword){//
    	mThread = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
	    			String str_user = inputUser;
	    			String str_passwd = inputPassword;
	    			client = new DefaultHttpClient();
	    			String url = gv.getUrl()+"login";
	    	        HttpPost post = new HttpPost(url);
	    	        JSONObject postData = new JSONObject();
	    	        postData.put("type", "account");
	    	        postData.put("account", str_user);
	    	        postData.put("password", str_user);
	    	        postData.put("password", str_passwd);
	    	        postData.put("fbID", "");
	    	        post.setEntity(new StringEntity(postData.toString()));
	    	        post.setHeader("Content-type", "application/json");
	    	        HttpResponse rp = client.execute(post);
					if(rp.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(rp.getEntity());
						jObj = new JSONObject(result);
						Log.d("loginresult", "stat:"+jObj.getString("stat"));
						gv.setUserID(jObj.getInt("userID"));
						Log.d("loginresult", "userID:"+jObj.getInt("userID"));
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
    
    private void accountSignUp(final String inputAccount,final String inputPassword,final String inputAccountName){//
    	mThread = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
	    			String str_account = inputAccount;
	    			String str_passwd = inputPassword;
	    			String str_accountName = inputAccountName;
	    			client = new DefaultHttpClient();
	    	        HttpPost post = new HttpPost("http://10.0.2.2:5000/api/addUser");
	    	        JSONObject postData = new JSONObject();
	    	        postData.put("account", str_account);
	    	        postData.put("accountName", str_accountName);
	    	        postData.put("password", str_passwd);
	    	        postData.put("fbID", "");
	    	        postData.put("loginDevice", "");
	    	        post.setEntity(new StringEntity(postData.toString()));
	    	        post.setHeader("Content-type", "application/json");
	    	        HttpResponse rp = client.execute(post);
					if(rp.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(rp.getEntity());
						jObj = new JSONObject(result);
						Log.d("result", "stat:"+jObj.getString("stat"));
						Message msg_t = new Message();
						msg_t.what = 2;
						//msg_t = mHandler.obtainMessage(2, obj)
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
    
    private void setGroups(){//
    	mGThread = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
	    			Gclient = new DefaultHttpClient();
	    			String url = gv.getUrl()+"Favorites/get";
	    	        HttpPost post = new HttpPost(url);
	    	        JSONObject postData = new JSONObject();
	    	        postData.put("userID", gv.getUserID());
	    	        post.setEntity(new StringEntity(postData.toString()));
	    	        post.setHeader("Content-type", "application/json");
	    	        HttpResponse rp = Gclient.execute(post);
					if(rp.getStatusLine().getStatusCode() == 200){
						String result = EntityUtils.toString(rp.getEntity());
						GjObj = new JSONObject(result);
						Log.d("GroupGet", "stat:"+GjObj.getString("stat"));
						//Log.d("loginresult", "userID:"+GjObj.getInt("userID"));
						Message msg_t = new Message();
						msg_t.what = 3;
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
					Gclient.getConnectionManager().shutdown();
				}
			}
    	});
    	mGThread.start();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
    	if(v == buttonAccount){
    		final Dialog login=new Dialog(this); 
    		login.setTitle("一般帳號登入");
    		login.setContentView(R.layout.login_dialog);
    		
    		Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
    		Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
    		final EditText txtUsername = (EditText) login.findViewById(R.id.txtUsername);
    		final EditText txtPassword = (EditText) login.findViewById(R.id.txtPassword);
    		btnLogin.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					accountLogin(txtUsername.getText().toString(),txtPassword.getText().toString());
					login.dismiss();
				}
    		});
    		
    		btnCancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					login.dismiss();
				}
    		});

    		login.show();
    	}
    	else if(v == buttonFB){
    		Session.openActiveSession(this, true, new Session.StatusCallback() {
    			
    			@Override
    			public void call(Session session, SessionState state, Exception exception) {
    				// TODO Auto-generated method stub
    				if(session.isOpened()){
    					Log.e("jack", session.getAccessToken());
    					Log.e("jack_1",session.getState().toString());
    					gotoIndex();
    				}
    			}

    		});
    	}
    	else if(v == buttonSignUp){
    		final Dialog signUp=new Dialog(this); 
    		signUp.setTitle("一般帳號註冊");
    		signUp.setContentView(R.layout.signup_dialog);
    		
    		Button btnSignUp = (Button) signUp.findViewById(R.id.btnSignUp);
    		Button btnSignCancel = (Button) signUp.findViewById(R.id.btnSignCancel);
    		final EditText txtAccount = (EditText) signUp.findViewById(R.id.txtAccount);
    		final EditText txtPassword = (EditText) signUp.findViewById(R.id.txtPassword);
    		final EditText txtAccountName = (EditText) signUp.findViewById(R.id.txtAccountName);
    		btnSignUp.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					accountSignUp(txtAccount.getText().toString(),txtPassword.getText().toString(),txtAccountName.getText().toString());
					signUp.dismiss();
				}
    		});
    		
    		btnSignCancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					signUp.dismiss();
				}
    		});

    		signUp.show();
    	}
    }
    

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	private void gotoIndex(){
		Intent i = new Intent();
		i.setClass(MainActivity.this, index.class);
		startActivity(i);
	}
}
