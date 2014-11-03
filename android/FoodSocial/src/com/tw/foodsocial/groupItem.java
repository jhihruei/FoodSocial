package com.tw.foodsocial;

import java.util.ArrayList;

public class groupItem {
	private String groupName;
	private ArrayList<Integer> groupPostIDs;
	
	public groupItem(){
		this.groupName = "";
		this.groupPostIDs = new ArrayList<Integer>();
	}
	
	public groupItem(String iName,ArrayList<Integer> iIDs){
		this.groupName = iName;
		this.groupPostIDs = iIDs;
	}
	
	public void setGroupName(String iName){
		this.groupName = iName;
	}
	
	public void setID(int iID){
		this.groupPostIDs.add(iID);
	}
	
	public void setIDs(ArrayList<Integer> iIDs){
		this.groupPostIDs.addAll(iIDs);
	}
	
	public String getGroupName(){
		return this.groupName;
	}
	
	public ArrayList<Integer> getIDs(){
		return this.groupPostIDs;
	}
}
