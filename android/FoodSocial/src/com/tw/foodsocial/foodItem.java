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
	
	public void setTitle(String inputTitle){
		this.title = inputTitle;
	}
	
	public void setAddress(String inputAddress){
		this.address = inputAddress;
	}
	
	public void setContent(String inputContent){
		this.content = inputContent;
	}
	
	public void setPoster(int inputPoster){
		this.poster = inputPoster;
	}
	
	public void setRecommendBy(int inputRb){
		this.recommendBy = inputRb;
	}

	public String getTitle(){
		return this.title;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public String getContent(){
		return this.content;
	}
}
