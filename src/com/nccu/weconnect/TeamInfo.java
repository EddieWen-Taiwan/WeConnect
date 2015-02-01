package com.nccu.weconnect;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

public class TeamInfo extends DrawerActivity {

	public String[] teammateName, teammateSay;
	public int[] teammateId;
	public static Activity finishme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_info);
		
		if( new checkNetwork().isNetworkAvailable( TeamInfo.this ) == true ) {
			new getInfoTask().execute();
			
			finishme = this;
			
			initDrawer();
		}

	}	// onCreate

	private class getInfoTask extends AsyncTask<Void, Void, String> {

		String strEntity;
		String[] temp, forum_subject, forum_date, forum_time, forum_place;
		int[] forum_id;

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SharedPreferences settings = getSharedPreferences( "Account", 0 );
			int userId = settings.getInt( "USERID", 0 );
			String ip = new getIP().get();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "getMyForums.php?userId=" + userId );

			try {
				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				strEntity = EntityUtils.toString( entity, "utf-8" );
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if( strEntity.isEmpty() )	
				return "FAIL";
			else {
				temp = strEntity.split( "<br>" );
				forum_id = new int[temp.length];
				forum_subject = new String[temp.length];
				forum_date = new String[temp.length];
				forum_time = new String[temp.length];
				forum_place = new String[temp.length];
				for( int i = 0; i < temp.length; i++ ) {
					String[] temp2 = temp[i].split( "_" );
					
					forum_id[i] = Integer.valueOf( temp2[0] );
					forum_subject[i] = temp2[1];
					forum_date[i] = temp2[2];
					forum_time[i] = temp2[3];
					forum_place[i] = temp2[4];
				}

				return "SUCCESS";
			}

		}	// doInBackground

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if( result.equals( "SUCCESS" ) ) {
				
				ExpandableListView forumhistory = (ExpandableListView) findViewById(R.id.forumhistory);
				ForumTitleAdapter adapter = new ForumTitleAdapter( TeamInfo.this,
											forum_id, forum_subject, forum_date, forum_time, forum_place );
				forumhistory.setAdapter( adapter );
				forumhistory.expandGroup( 0 );
				forumhistory.setGroupIndicator(null);
				
			} else if( result.equals( "FAIL" ) ) {

				AlertDialog.Builder alert = new AlertDialog.Builder( TeamInfo.this );
					alert.setIcon(R.drawable.ic_wcnt);
					alert.setTitle( "什麼?!" );
					alert.setMessage( getString( R.string.noforum ) );
					alert.setNegativeButton( "確認", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent( TeamInfo.this, MainActivity.class );
							startActivity( intent );

							MainActivity.finishme.finish();
							finish();
						}

					});
				alert.show();

			}

		}	// omPostExecute

	}	// getInfoTask AsyncTask

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent_main = new Intent( TeamInfo.this, MainActivity.class );
		startActivity( intent_main );

		finish();
//		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.drawer, menu );
		return true;
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
