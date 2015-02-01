package com.nccu.weconnect;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

public class ReportUs extends DrawerActivity {

	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_us);

		if( new checkNetwork().isNetworkAvailable( ReportUs.this ) == true ) {

			initDrawer();

			btn = (Button) findViewById(R.id.sure);
			btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new reportTask().execute();
					btn.setClickable( false );
				}

			});

			ScrollView screen = (ScrollView) findViewById(R.id.fullscreen);
			screen.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
					imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
					return false;
				}

			});

		}	// if network is work

	}	// onCreate
	
	private class reportTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String ip = new getIP().get();
			String content = "";
			
			SharedPreferences settings = getSharedPreferences( "Account", 0) ;
			int userId = settings.getInt( "USERID", 0 );
			
			EditText edit = (EditText) findViewById(R.id.reportcontent);
			try {
				content = URLEncoder.encode( edit.getText().toString(), "UTF-8" );
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "reportUs.php?userId=" + userId + "&content=" + content );
			try {
				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				String strEntity = EntityUtils.toString( entity );
				if(  strEntity.equals( "SUCCESS" ) ) {
					return "SUCCESS";
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "FAIL";
		}	// doInBackground

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if ( result.equals( "SUCCESS" ) ) {
				AlertDialog.Builder alert = new AlertDialog.Builder( ReportUs.this );
					alert.setIcon(R.drawable.ic_wcnt);
					alert.setTitle( "成功" );
					alert.setMessage( "　　感謝您寶貴的意見，WeConnect會持續努力。" );
					alert.setNegativeButton( "返回主頁面", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent( ReportUs.this, MainActivity.class );
							startActivity( intent );

							finish();							
						}
					});
				alert.show();
			} else {
				AlertDialog.Builder alert = new AlertDialog.Builder( ReportUs.this );
					alert.setIcon(R.drawable.ic_wcnt);
					alert.setTitle( "錯誤" );
					alert.setMessage( "　　發生了點錯誤，請在幾分鐘後再試一次。" );
					alert.setNegativeButton( "確認", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub						
						}
					});
				alert.show();

				btn.setClickable( true );
			}	// if else

		}	// onPostExecute

	}	// reportTask AsyncTask

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent_main = new Intent( ReportUs.this, MainActivity.class );
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
