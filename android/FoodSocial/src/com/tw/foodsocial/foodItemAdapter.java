package com.tw.foodsocial;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class foodItemAdapter extends ArrayAdapter<foodItem>{

	private int resource;
	private List<foodItem> fItems;
	
	public foodItemAdapter(Context context, int resource,
			List<foodItem> fItems) {
		super(context, resource, fItems);
		// TODO Auto-generated constructor stub
		this.resource = resource;
		this.fItems = fItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout foodItemView;
		final foodItem fItem = getFoodItem(position);
		
		if(convertView == null){
			foodItemView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, foodItemView, true);
		}
		else{
			foodItemView = (LinearLayout) convertView;
		}
		
		TextView TV_foodItem_title = (TextView) foodItemView.findViewById(R.id.TV_foodItem_title);
		TextView TV_foodItem_address = (TextView) foodItemView.findViewById(R.id.TV_foodItem_address);
		TextView TV_foodItem_content = (TextView) foodItemView.findViewById(R.id.TV_foodItem_content);
		TextView TV_foodItem_postTime = (TextView) foodItemView.findViewById(R.id.TV_foodItem_postTIme);
		TextView TV_foodItem_posterName = (TextView) foodItemView.findViewById(R.id.TV_foodItem_poster);
		TextView TV_foodItem_recommendByName = (TextView) foodItemView.findViewById(R.id.TV_foodItem_recommendBy);
		
		TV_foodItem_title.setText(fItem.getTitle());
		TV_foodItem_address.setText(fItem.getAddress());
		TV_foodItem_content.setText(fItem.getContent());
		TV_foodItem_postTime.setText(fItem.getPostTimeString());
		TV_foodItem_posterName.setText(fItem.getPosterName());
		TV_foodItem_recommendByName.setText(fItem.getRecommendByName());
		
		return foodItemView;
	}

	public foodItem getFoodItem(int index){
		return fItems.get(index);
	}
	
	public void setFoodItem(List<foodItem> inputfItems){
		fItems.addAll(inputfItems);
		this.notifyDataSetChanged();
	}
	
	public void refresh(List<foodItem> newfItems){
		this.fItems = newfItems;
		this.notifyDataSetChanged();
	}
}