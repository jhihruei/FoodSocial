package com.tw.foodsocial;

import android.app.Application;

public class GlobalVariable extends Application{
	//private String Url = "http://104.131.147.212/api/";
	private String Url = "http://10.0.2.2:5000/api/";
	private int curUserID = 0;
	
	public String getUrl(){
		return this.Url;
	}
	
	public void setUserID(int inputID){
		this.curUserID = inputID;
	}
	
	public int getUserID(){
		return this.curUserID;
	}
}
