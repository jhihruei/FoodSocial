package com.tw.foodsocial;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.string;


public class foodItem {
	
	private int postID;
	private String title;
	private String address;
	private String content;
	private int poster;
	private int recommendBy;
	private String posterName;
	private String recommendByName;
	private Date postTime;
	
	public foodItem(){
		this.postID = 0;
		this.title = "";
		this.address = "";
		this.content = "";
		this.poster = 0;
		this.recommendBy = 0;
		posterName = "";
		recommendByName = "";
		this.postTime = new Date();
	}
	
	public foodItem(int ipostID,String ititle,String iaddress,String icontent,int iposter,int irb,Date iPostTime,String iposterName,String irecommendByName){
		this.postID = ipostID;
		this.title = ititle;
		this.address = iaddress;
		this.content = icontent;
		this.poster = iposter;
		this.recommendBy = irb;
		this.postTime = iPostTime;
		this.posterName = iposterName;
		this.recommendByName = irecommendByName;
	}
	
	public void setPostID(int inputPostid){
		this.postID = inputPostid;
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
	
	public void setPosterName(String iName){
		this.posterName = iName;
	}
	
	public void setRecommendByName(String iName){
		this.recommendByName = iName;
	}
	
	public int getPostID(){
		return this.postID;
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
	
	public String getPosterName(){
		return this.posterName;
	}
	
	public String getRecommendByName(){
		return this.recommendByName;
	}
}
