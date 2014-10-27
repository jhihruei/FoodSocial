package com.tw.foodsocial;

import android.R.string;

public class foodItem {
	
	private String title;
	private String address;
	private String content;
	private int poster;
	private int recommendBy;
	
	public foodItem(){
		this.title = "";
		this.address = "";
		this.content = "";
		this.poster = 0;
		this.recommendBy = 0;
	}
	
	public foodItem(String ititle,String iaddress,String icontent,int iposter,int irb){
		this.title = ititle;
		this.address = iaddress;
		this.content = icontent;
		this.poster = iposter;
		this.recommendBy = irb;
	}
	
	

}
