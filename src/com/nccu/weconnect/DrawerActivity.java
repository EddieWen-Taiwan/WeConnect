package com.nccu.weconnect;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class DrawerActivity extends Activity {

	protected void initDrawer() {

		String[] menu_item = new String[]{ getString( R.string.title_activity_lets_connect ), getString( R.string.title_activity_personal_file ),
				getString ( R.string.title_activity_forum_history ), getString( R.string.title_activity_report ), getString( R.string.title_activity_qa ) };
		int[] menu_icon = new int[]{ R.drawable.list_letsconnect, R.drawable.list_profile, R.drawable.list_history, R.drawable.list_report, R.drawable.list_qa };

		DrawerLayout drawerlayout = (DrawerLayout) findViewById(R.id.drw_layout);
		drawerlayout.setDrawerShadow( R.drawable.drawer_shadow, GravityCompat.END );

		DrawerAdapter drawer = new DrawerAdapter( getBaseContext(), menu_item, menu_icon, 0 );

		ListView drawerlist = (ListView) findViewById(R.id.drawer_list);
		drawerlist.setAdapter( drawer );

		drawerlist.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = null;
				switch( position ) {
					case 0:
						checkAuth();
						break;
					case 1:
						intent = new Intent( getBaseContext(), PersonalFile.class );
						intent.putExtra( "comefrom", 1 );
						startActivity( intent );
						finish();
						break;
					case 2:
						intent = new Intent( getBaseContext(), TeamInfo.class );
						startActivity( intent );
						finish();
						break;
					case 3:
						intent = new Intent( getBaseContext(), ReportUs.class );
						startActivity( intent );
						finish();
						break;
					case 4:
						intent = new Intent( getBaseContext(), QnAActivity.class );
						startActivity( intent );
						finish();
						break;
					default:
						break;
				}

			}	// switch

		});

	}	// initDrawer

	protected void checkAuth() {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences( "Account", 0 );
		int auth = settings.getInt( "AUTH", 0 );
		Intent intent = null;

		switch( auth ) {
			case 1:
				intent = new Intent( getBaseContext(), LetsConnect.class );
				startActivity( intent );
				finish();
				break;
			case 4:
				intent = new Intent( getBaseContext(), MainActivity.class );
				startActivity( intent );
				finish();
				break;
			default:
				break;
		}

	}	// checkAuth

}
