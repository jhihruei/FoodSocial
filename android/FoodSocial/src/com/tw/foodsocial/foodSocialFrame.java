package com.tw.foodsocial;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class foodSocialFrame extends FragmentActivity implements OnClickListener{
	private Fragment F_content;
	private FragmentTransaction FT;
	private FragmentManager fm;
	private Button BT_F_1,BT_F_2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.foodsocialframe);
		
		init();
	}
	
	private void init(){
		F_content = new fragment_postWall();
		FT = getSupportFragmentManager().beginTransaction();
		FT.add(R.id.LL_F_content, F_content, "test");
		FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		FT.commit();
		BT_F_1 = (Button) this.findViewById(R.id.BT_F_1);
		BT_F_1.setOnClickListener(this);
		BT_F_2 = (Button) this.findViewById(R.id.BT_F_2);
		BT_F_2.setOnClickListener(this);
	}

	public void changeFragment(int targetFragment){
		Fragment newFragment = new Fragment();
		switch(targetFragment){
		case 1:
			newFragment = new fragment_postWall();
			break;
		case 2:
			newFragment = new fragment_postPage();
			break;
		case 3:
			newFragment = new fragment_myFavs();
			break;
		}
		FT = this.getSupportFragmentManager().beginTransaction();
		FT.replace(R.id.LL_F_content, newFragment);
		FT.addToBackStack(null);
		FT.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == BT_F_1){
			changeFragment(2);
			Log.d("pg", "gotopg");
		}
		else if(v == BT_F_2){
			changeFragment(3);
			Log.d("mf","gotomf");
		}
	}
	
}
