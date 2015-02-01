package com.nccu.weconnect;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonalFile extends DrawerActivity {

	public static Activity finishme;
	Intent intent_in;
	int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_file);

		if( new checkNetwork().isNetworkAvailable( PersonalFile.this ) == true ) {

			intent_in = getIntent();
			finishme = this;

			initDrawer();

			checkIdentity();

		}	// if network is work

	}	// onCreate

	private void checkIdentity() {

		if ( intent_in.getIntExtra( "comefrom", 0 ) == 1 ){

			SharedPreferences settings = getSharedPreferences( "Account", 0 );
			id = settings.getInt( "USERID", 0 );
			new getInfoTask().execute( id );

			LinearLayout comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
			comment_layout.setVisibility( 0 );
			comment_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent( PersonalFile.this, CommentPage.class);
					startActivity( intent );
				}

			});

		} else if ( intent_in.getIntExtra( "comefrom", 0 ) == 2 ) {

			ActionBar actionbar = getActionBar();	//		關於ACtionBar 
			actionbar.setDisplayHomeAsUpEnabled(true);

			String username = intent_in.getStringExtra( "userName" );
			id = intent_in.getIntExtra( "userId", 0 );
			new getInfoTask().execute( id );

			TextView name = (TextView) findViewById(R.id.name);
			name.setText( username );

		}

	}

	private class getInfoTask extends AsyncTask<Integer, Void, String>{

		String strEntity;
		String[] info;
		Bitmap myBitmap;

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			String ip = new getIP().get();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "getpersonalinfo_1_1.php?userId=" + id );
			try {
				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				strEntity = EntityUtils.toString( entity, "utf-8" );

				info = strEntity.split( "<br>" );
		//	load image through url
				String strurl = info[6];
				URL url = new URL( strurl );
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        myBitmap = BitmapFactory.decodeStream(input);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "SUCCESS";
		}	// doInBackground

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if( result.equals( "SUCCESS") ) {
				TextView name = (TextView) findViewById(R.id.name);
					name.setText( info[0] );
				TextView depart = (TextView) findViewById(R.id.department);
					depart.setText( "政治大學\n" + info[1] );
				TextView star = (TextView) findViewById(R.id.starGrade);
					star.setText( info[2] );
				TextView club = (TextView) findViewById(R.id.clubcontent);
					club.setText( info[3] );
				TextView work = (TextView) findViewById(R.id.workcontent);
					work.setText( info[4] );
				TextView story = (TextView) findViewById(R.id.storycontent);
					story.setText( info[5] );

				if( !info[6].equals( "default" ) ) {
					ImageView photo = (ImageView) findViewById(R.id.photo);
					photo.setImageBitmap( myBitmap );
				}

			}	// if success

		}	// onPostExecute

	}	// getInfoTask AsyncTask

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if( intent_in.getIntExtra( "comefrom", 0 ) == 1 ) {
			Intent Intent_main = new Intent( PersonalFile.this, MainActivity.class );
			startActivity( Intent_main );

			finish();
		} else
			super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if( intent_in.getIntExtra( "comefrom", 0 ) == 1 ) {
			getMenuInflater().inflate(R.menu.edit, menu);
			getMenuInflater().inflate( R.menu.drawer, menu );
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch( item.getItemId() ) {
			case R.id.action_edit:
				Intent intent_edit = new Intent( PersonalFile.this, EditProfile.class );
				startActivity( intent_edit );
				break;
			case android.R.id.home:
				onBackPressed();
				return true;
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
