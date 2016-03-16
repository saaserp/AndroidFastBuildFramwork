package com.wcgh.framwork;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.wcgh.gidance.GuidanceActivity;
import com.wcgh.login.LoginActivity;

public class MainActivity extends Activity {
	public final int LOGIN=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		isFirst();
		
	}
	private void startLogin(){
		this.startActivityForResult(new Intent(this,LoginActivity.class), LOGIN);
	}
	private boolean isFirst() {
		 
		SharedPreferences sp = getSharedPreferences("first", MODE_PRIVATE);
		boolean isFirst = sp.getBoolean("isFirst", true);
		if (isFirst) {
			 
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivityForResult(new Intent(this, GuidanceActivity.class),
					1);
		}
		return isFirst;
	}

}
