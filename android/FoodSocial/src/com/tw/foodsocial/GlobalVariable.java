package com.tw.foodsocial;

import java.util.ArrayList;

import android.app.Application;
import android.util.Log;

public class GlobalVariable extends Application{
	//private String Url = "http://104.131.147.212/api/";
	private String Url = "http://10.0.2.2:5000/api/";
	private int curUserID = 0;
	private ArrayList<groupItem> groups = new ArrayList<groupItem>();

	
	
	
	public String getUrl(){
		return this.Url;
	}
	
	public void setUserID(int inputID){
		this.curUserID = inputID;
	}
	
	public int getUserID(){
		return this.curUserID;
	}
	
	public void groupReset(){
		this.groups = new ArrayList<groupItem>();
	}
	
	public void setGroup(String newGroupName, ArrayList<Integer> inputIDs){
		Boolean flag = true;
		for(int i = 0 ; i < groups.size() ; i++){
			if(this.groups.get(i).getGroupName() == newGroupName){
				this.groups.get(i).setIDs(inputIDs);
				this.groups.get(i).setFoodItems();
				flag = false;
				break;
			}
		}
		if(flag){
			groupItem newGroup = new groupItem(this,newGroupName,inputIDs);
			this.groups.add(newGroup);
		}
	}
	
	public ArrayList<groupItem> getGroups(){
		return this.groups;
	}
	

}
