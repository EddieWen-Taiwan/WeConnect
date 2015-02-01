package com.nccu.weconnect;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class IdentifyPic extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.identify_pic);
		
		if ( new checkNetwork().isNetworkAvailable( IdentifyPic.this ) == true ) {
			
			ActionBar actionbar = getActionBar();	// 關於ActionBar 
			actionbar.setDisplayHomeAsUpEnabled(true);

			Intent intent_in = getIntent();
			String subject = intent_in.getStringExtra( "subject" );
			String date = intent_in.getStringExtra( "date" );
			String time = intent_in.getStringExtra( "time" );
			
			TextView text = (TextView) findViewById(R.id.aaa);
			text.setText( subject + "\n" + date + "\n" + time + "\n" );

		}

	}	// onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.identify_pic, menu);
		return true;
	}

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
