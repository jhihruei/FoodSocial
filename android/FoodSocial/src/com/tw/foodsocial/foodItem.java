package com.tw.foodsocial;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.string;


public class foodItem {
	
	private String title;
	private String address;
	private String content;
	private int poster;
	private int recommendBy;
	private Date postTime;
	
	public foodItem(){
		this.title = "";
		this.address = "";
		this.content = "";
		this.poster = 0;
		this.recommendBy = 0;
		this.postTime = new Date();
	}
	
	public foodItem(String ititle,String iaddress,String icontent,int iposter,int irb,Date iPostTime){
		this.title = ititle;
		this.address = iaddress;
		this.content = icontent;
		this.poster = iposter;
		this.recommendBy = irb;
		this.postTime = iPostTime;
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

	public void setPostTime(long iTimeStamp){
		this.postTime = new Date(iTimeStamp);
	}
	
	public void setPostTime(Date iDate){
		this.postTime = iDate;
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
	
	public int getPoster(){
		return this.poster;
	}
	
	public int getRecommendBy(){
		return this.recommendBy;
	}
	
	public Date getPostTime(){
		return this.postTime;
	}

	
	public String getPostTimeString(){
		String resultTime;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		resultTime = sdf.format(this.postTime);
		return resultTime;
	}
}
