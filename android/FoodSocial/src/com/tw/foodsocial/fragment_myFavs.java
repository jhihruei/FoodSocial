package com.tw.foodsocial;

import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class fragment_myFavs extends Fragment {
	private View mView;
	private ExpandableListView ELV_Fav;
	private GlobalVariable gv;
	private groupParentAdapter gPA;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
		gPA = new groupParentAdapter(this.getActivity().getBaseContext(),ELV_Fav,gv.getGroups());
			
		ELV_Fav.setAdapter(gPA);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.fragment_myfavs, container, false);
		Log.d("oCV", "oCV");
		return mView;
	}
	
	void init(){
		ELV_Fav = (ExpandableListView) mView.findViewById(R.id.ELV_Fav_content);
		gv = (GlobalVariable) this.getActivity().getApplicationContext();
	}

	public class groupParentAdapter extends BaseExpandableListAdapter{

		private Context tContext;
		private ArrayList<groupItem> gItems;
		private LayoutInflater inflater;
		private groupExpListView gELV[];
		public groupParentAdapter(Context context, ExpandableListView ELV, ArrayList<groupItem> igItems){
			this.tContext = context;
			this.gItems = igItems;
			this.inflater = LayoutInflater.from(context);
			gELV = new groupExpListView[igItems.size()];
		}
		
		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return gItems.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return gItems.get(groupPosition).getIDs().size();
		}

		@Override
		public groupItem getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return gItems.get(groupPosition);
		}

		@Override
		public Integer getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return gItems.get(groupPosition).getIDs().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = null;
			if(convertView != null)
				v = convertView;
			else
				v = inflater.inflate(R.layout.elv_row_1, parent, false);
			if(gItems.size()!=0){
				String groupName = gItems.get(groupPosition).getGroupName();
				if(gItems.get(groupPosition).checkFoodItemEmpty()){
					gItems.get(groupPosition).setFoodItems();
				}
				TextView TV_groupName = (TextView) v.findViewById(R.id.TV_Row1_1);
				Button BT_rand = (Button) v.findViewById(R.id.BT_Row1_rand);
				
				BT_rand.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						foodItem tempFItem = gItems.get(groupPosition).randGetFood();
						Log.d("randID", tempFItem.getTitle());
						final Dialog signUp=new Dialog(v.getContext()); 
			    		signUp.setTitle("今天吃什麼?");
			    		signUp.setContentView(R.layout.fooditem);
			    		
			    		TextView TV_foodItem_title = (TextView) signUp.findViewById(R.id.TV_foodItem_title);
			    		TextView TV_foodItem_address = (TextView) signUp.findViewById(R.id.TV_foodItem_address);
			    		TextView TV_foodItem_content = (TextView) signUp.findViewById(R.id.TV_foodItem_content);
			    		TextView TV_foodItem_postTime = (TextView) signUp.findViewById(R.id.TV_foodItem_postTIme);
			    		TextView TV_foodItem_posterName = (TextView) signUp.findViewById(R.id.TV_foodItem_poster);
			    		TextView TV_foodItem_recommendByName = (TextView) signUp.findViewById(R.id.TV_foodItem_recommendBy);
			    		Button BT_addGroup = (Button) signUp.findViewById(R.id.BT_foosItem_addGroup);
			    		
			    		TV_foodItem_title.setText(tempFItem.getTitle());
			    		TV_foodItem_address.setText(tempFItem.getAddress());
			    		TV_foodItem_content.setText(tempFItem.getContent());
			    		TV_foodItem_postTime.setText(tempFItem.getPostTimeString());
			    		TV_foodItem_posterName.setText(tempFItem.getPosterName());
			    		TV_foodItem_recommendByName.setText(tempFItem.getRecommendByName());
			    		signUp.show();
						//Toast.makeText(, tempFItem.getPostID(), Toast.LENGTH_LONG);
					}
				});
				
				if(groupName != null)
					TV_groupName.setText(groupName);
				return v;
			}
			return null;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = null;
			if(gELV[groupPosition] != null)
				v = gELV[groupPosition];
			else{
				v = inflater.inflate(R.layout.fooditem, parent,false);

				final foodItem fItem = gItems.get(groupPosition).getFoodItem(childPosition);
				
				TextView TV_foodItem_title = (TextView) v.findViewById(R.id.TV_foodItem_title);
				TextView TV_foodItem_address = (TextView) v.findViewById(R.id.TV_foodItem_address);
				TextView TV_foodItem_content = (TextView) v.findViewById(R.id.TV_foodItem_content);
				TextView TV_foodItem_postTime = (TextView) v.findViewById(R.id.TV_foodItem_postTIme);
				TextView TV_foodItem_posterName = (TextView) v.findViewById(R.id.TV_foodItem_poster);
				TextView TV_foodItem_recommendByName = (TextView) v.findViewById(R.id.TV_foodItem_recommendBy);
				Button BT_addGroup = (Button) v.findViewById(R.id.BT_foosItem_addGroup);
				
				TV_foodItem_title.setText(fItem.getTitle());
				TV_foodItem_address.setText(fItem.getAddress());
				TV_foodItem_content.setText(fItem.getContent());
				TV_foodItem_postTime.setText(fItem.getPostTimeString());
				TV_foodItem_posterName.setText(fItem.getPosterName());
				TV_foodItem_recommendByName.setText(fItem.getRecommendByName());
				//groupExpListView newgELV = new groupExpListView(tContext);
				//newgELV.setRows(gItems.get(groupPosition).getIDs().size());
				//SimpleExpandableListAdapter tempAdapter = new SimpleExpandableListAdapter(tContext,);
				//gELV[groupPosition] = newgELV;
				//v = newgELV;
			}
			return v;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	
	public class groupExpListView extends ExpandableListView{
		private int rows;
		public groupExpListView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public void setRows(int inputRows){
			this.rows = inputRows;
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// TODO Auto-generated method stub
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		
	}
	
}
