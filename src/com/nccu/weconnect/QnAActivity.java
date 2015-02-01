package com.nccu.weconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

public class QnAActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.q_and_a);

		if( new checkNetwork().isNetworkAvailable( QnAActivity.this ) == true ) {

			initDrawer();

		}	// if network is work

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drawer, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent_main = new Intent( QnAActivity.this, MainActivity.class );
		startActivity( intent_main );

		finish();
//		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch( item.getItemId() ) {
			case R.id.action_drawer:
				DrawerLayout drw_layout = (DrawerLayout) findViewById(R.id.drw_layout);
				if( drw_layout.isDrawerOpen( GravityCompat.END ) )
					drw_layout.closeDrawers();
				else
					drw_layout.openDrawer( GravityCompat.END );
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
