package com.hostxin.android.jsmethod;

import android.app.Activity;
import android.widget.Toast;



public class BaseActivityBrowser extends Activity {

	public void showToast(String text) {
		// TODO Auto-generated method stub
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

}
