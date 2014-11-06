package com.tw.foodsocial;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class SessionManager {
	SharedPreferences pref;
	Editor editor;
	Context mContext;
	private static final String PREF_NAME = "AndroidFoodSocial";
	private static final String IS_LOGIN = "IsLogined";
	public static final String KEY_ACCOUNT = "account";
	public static final String KEY_PASSWD = "passwd";
	int PRIVATE_MODE = 0;
	
	
	public SessionManager(Context context){
		Log.d("ini", "ini");
		this.mContext = context;
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
		editor.putBoolean(IS_LOGIN, false);
		editor.commit();
	}
	
	public void LoginSucess(String account, String passwd){
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_ACCOUNT, account);
		editor.putString(KEY_PASSWD, passwd);
		editor.commit();
		//Toast.makeText(mContext, account, Toast.LENGTH_LONG);
		Log.d("LoginSuccess","success");
	}
	
	public HashMap<String,String> getUser(){
		HashMap<String,String> user = new HashMap<String,String>();
		
		user.put(KEY_ACCOUNT, pref.getString(KEY_ACCOUNT, null));
		user.put(KEY_PASSWD, pref.getString(KEY_PASSWD, null));
		Log.d("getUser", "getUser");
		return user;
	}
	
	public Boolean isLogined(){
		//Log.d("loginStat", pref.getString(IS_LOGIN, null));
		return pref.getBoolean(IS_LOGIN, false);
		//return false;
	}
}
