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
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TeamList extends Activity {

	int forumid, qquota;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_list);

		if( new checkNetwork().isNetworkAvailable( TeamList.this ) == true ) {

			ActionBar actionbar = getActionBar();	//		關於ACtionBar 
			actionbar.setDisplayHomeAsUpEnabled(true);

			Intent intent_in = getIntent();
			forumid = intent_in.getIntExtra( "id", 0 );

			new getListTask().execute();

		}	// if network is work

	}	// onCreate

	private class getListTask extends AsyncTask<Void, Void, String> {
		
		String strEntity;
		String[] temp, teammateName, teammateMajor, teammateSay;
		int[] teammateId;
		int subject;

		@Override
		protected String doInBackground(Void... parms) {
			// TODO Auto-generated method stub
			String ip = new getIP().get();

			SharedPreferences settings = getSharedPreferences( "Account", 0 );
			int userId = settings.getInt( "USERID", 0 );
			int quota = settings.getInt( "QUOTA", 0 );

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = null;
			if( quota == 0 )
				get = new HttpGet( ip + "getForummate.php?code=2nc5q0c8&forumId=" + forumid + "&userId=" + userId );
			else if( quota >= 1 )
				get = new HttpGet( ip + "getForummate.php?code=ciw39n1e&forumId=" + 1234567 + "&userId=" + userId );

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

			temp = strEntity.split( "<br>" );
			subject = Integer.valueOf( temp[0] );
			teammateId = new int[temp.length-2];
			teammateName = new String[temp.length-2];
			teammateMajor = new String[temp.length-2];
			teammateSay = new String[temp.length-2];
			forumid = Integer.valueOf( temp[0] );
			for( int i = 1; i <= temp.length-1; i++ ) {

				if( i == temp.length-1 ) {
					if( !temp[i].equals( "noQuota" ) ) {
						qquota = 1;
						SharedPreferences.Editor myInfo = settings.edit();
						myInfo.putInt( "temp_quota", 1 );
						myInfo.commit();
					} else
						qquota = 0;
				} else {
					String[] temp2 = temp[i].split( "_" );

					int j = i -1;
					teammateId[j] = Integer.valueOf( temp2[0] );
					teammateName[j] = temp2[1];
					teammateMajor[j] = temp2[2];
					if( temp2[3].equals( "j656" ) )
						teammateSay[j] = "";
					else
						teammateSay[j] = temp2[3];
				}	// if else

			}	// for loop
 
			return "SUCCESS";
		}	// doInBackground

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if( result.equals( "SUCCESS" ) ) {
				
				ImageView banner = (ImageView) findViewById(R.id.banner);
				switch( subject ){
					case 1:
						banner.setImageDrawable( getResources().getDrawable(R.drawable.subject1_banner) );
						break;
					case 2:
						banner.setImageDrawable( getResources().getDrawable(R.drawable.subject2_banner) );
						break;
					case 3:
						banner.setImageDrawable( getResources().getDrawable(R.drawable.subject3_banner) );
						break;
					case 4:
						banner.setImageDrawable( getResources().getDrawable(R.drawable.subject4_banner) );
						break;
					default:
						break;
				}

				TeamListAdapter teamadapter = new TeamListAdapter( TeamList.this, teammateId,
												teammateName, teammateMajor, teammateSay, qquota, forumid );

				final ListView teammatelist = (ListView) findViewById(R.id.teammatelist);

				teammatelist.setAdapter( teamadapter );
				teammatelist.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
						// TODO Auto-generated method stub
						Intent intent_personal = new Intent( TeamList.this, PersonalFile.class );
						switch( position ) {
							case 0:
								intent_personal.putExtra( "userId", teammateId[0] );
								break;
							case 1:
								intent_personal.putExtra( "userId", teammateId[1] );
								break;
							case 2:
								intent_personal.putExtra( "userId", teammateId[2] );
								break;
							case 3:
								intent_personal.putExtra( "userId", teammateId[3] );
								break;
							case 4:
								intent_personal.putExtra( "userId", teammateId[4] );
								break;
							default:
								break;
						}
						intent_personal.putExtra( "comefrom", 2 );
						startActivity( intent_personal );
					}

				});	// OnItemCliclListener

			}	// if

		}	// onPostExecute

	}	// getListTask AsyncTask

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences( "Account", 0 );
		qquota = settings.getInt( "temp_quota", 0 );
		if( qquota == 1 )
			Toast.makeText( this, "請先將手中的星星送出喔", Toast.LENGTH_SHORT ).show();
		else {
			int quota = settings.getInt( "QUOTA", 0 );

			if( quota >= 1 ) {
				Toast.makeText( this, "請先完成評分唷", Toast.LENGTH_SHORT ).show();

				new getListTask().execute();
			}
			else {
				super.onBackPressed();
			}
		}	// if else

	}	// onBackPressed

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
			case android.R.id.home: //為up butoon的id 
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
