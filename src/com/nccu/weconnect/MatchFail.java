package com.nccu.weconnect;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.TextView;

public class MatchFail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_fail);

		if( new checkNetwork().isNetworkAvailable( MatchFail.this ) == true ) {
			
			ActionBar actionbar = getActionBar();	//		關於ACtionBar 
			actionbar.setDisplayHomeAsUpEnabled(true);

			Intent intent = getIntent();
			String msg = intent.getStringExtra( "msg" );
			String[] time = msg.split( "_" );
	
			TextView content = (TextView) findViewById(R.id.content);
			content.setText( getResources().getString(R.string.failcontent_1) + time[1] + getResources().getString(R.string.failcontent_2) );

		}	// if network is work

	}	// onCreate
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch( item.getItemId() ) {
			case android.R.id.home: //為up button的id 
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
