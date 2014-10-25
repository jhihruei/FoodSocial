package com.tw.foodsocial;

import android.app.Application;

public class GlobalVariable extends Application{
	private String Url = "http://104.131.147.212/api/";
	
	public String getUrl(){
		return this.Url;
	}
}
