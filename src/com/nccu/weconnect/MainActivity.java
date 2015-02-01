package com.nccu.weconnect;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends DrawerActivity {

	public static Activity finishme;
	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if( new checkNetwork().isNetworkAvailable( MainActivity.this ) == true ) {

			// 來了 快速通道
			if( getIntent().getIntExtra( "goTeam", 0 ) == 1 ) {
				startActivity( new Intent( MainActivity.this, TeamList.class ) );
			}	// 好快好快好快

			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			initDrawer();

			finishme = this;

			settings = getSharedPreferences( "Account", 0 );

			new getGossip().execute();

			TextView letsconnect = (TextView) findViewById(R.id.letsconnect);
			letsconnect.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					checkAuth();
				}

			});

		}	// if network is work

	}	// onCreate

	protected void checkAuth() {
		// TODO Auto-generated method stub
		int auth = settings.getInt( "AUTH", 0 );
		Intent intent = null;

		switch( auth ) {
			case 1:
				intent = new Intent( MainActivity.this, LetsConnect.class );
				startActivity( intent );
				break;
			case 4:
				AlertDialog.Builder alert = new AlertDialog.Builder( MainActivity.this );
					alert.setIcon( R.drawable.ic_wcnt );
					alert.setTitle( "警告" );
					alert.setMessage( "此帳號因被檢舉多次缺席，因此停權一周。若有任何問題可至\"意見回饋\"功能提出，WeConnect會盡快與您聯繫。" );
					alert.setNegativeButton( "確認", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
				alert.show();
				break;
			default:
				break;
		}	// switch

	}	// checkAuth method

	private class getGossip extends AsyncTask<Void,Void,String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String ip = new getIP().get();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "getGossip.php" );
			try {
				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString( entity, "utf-8" );
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}	// doInBackground

		@Override
		protected void onPostExecute(String strGossip) {
			// TODO Auto-generated method stub
			super.onPostExecute(strGossip);

			TextView gossip = (TextView) findViewById(R.id.gossip);
			gossip.setText( strGossip );
//			gossip.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					SharedPreferences settings = getSharedPreferences( "Account", 0 );
//					SharedPreferences.Editor myInfo = settings.edit();
//					myInfo.clear();
//					myInfo.commit();
//					Intent intent = new Intent( MainActivity.this, LoginAccount.class );
//					startActivity( intent );
//					finish();
//				}
//			});

		}	// onPostExecute

	}	// getGossip

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate( R.menu.drawer, menu );
		return super.onCreateOptionsMenu(menu);
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
