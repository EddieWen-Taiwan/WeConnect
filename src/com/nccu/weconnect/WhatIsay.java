package com.nccu.weconnect;
//先把手機連上網路！！！！！！！！！！！！！！！！！！！！！！！！！！！！
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

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

public class WhatIsay extends Activity {

	int forum = 0, subject = 0;
	Button sure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.what_isay);

		if( new checkNetwork().isNetworkAvailable( WhatIsay.this ) == true ) {

			ActionBar actionbar = getActionBar();	//		關於ACtionBar 
			actionbar.setDisplayHomeAsUpEnabled(true);

			Intent intent_in = getIntent();
			forum = intent_in.getIntExtra( "forum_selected", 0 );
			subject = intent_in.getIntExtra( "subject", 0 );

			ImageView banner = (ImageView) findViewById(R.id.banner);
			switch( subject ) {
				case 1:		// 旅遊
					banner.setImageDrawable( getResources().getDrawable( R.drawable.subject1_banner ) );
					break;
				case 2:		// 創業
					banner.setImageDrawable( getResources().getDrawable( R.drawable.subject2_banner ) );
					break;
				case 3:		// 校園議題
					banner.setImageDrawable( getResources().getDrawable( R.drawable.subject3_banner ) );
					break;
				case 4:		// 公共議題
					banner.setImageDrawable( getResources().getDrawable( R.drawable.subject4_banner ) );
					break;
				default:
					break;
			}
	
			sure = (Button) findViewById(R.id.sure);
			sure.setOnClickListener(new OnClickListener(){
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sure.setClickable( false );
					new askForCheck().execute();
				}

			});	// OnClickListener
			
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

	private class askForCheck extends AsyncTask< Void, Void, String> {

		String say = "";
		int checked = 2, userId = 0;	// 以上，沒用
		String strEntity, date, matchTime, location;
		String[] temp = new String[1];
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String ip = new getIP().get();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "attend_1_2.php?about=" + say + "&w=19930721" + "&userId=" + userId
					+ "&intent=" + checked + "&subjectId=" + subject + "&forum=" + forum );
			try {
				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				strEntity = EntityUtils.toString( entity, "utf-8" );
				temp = strEntity.split( "_" );

					date = temp[0];
					matchTime = temp[1];
					location = temp[2];

				return "SUCCESS";
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

			if( result.equals( "SUCCESS" ) ){

				String msg = "您選擇的時段是:\n" + date + "   " + matchTime + "場次\n" + "並" + location + "\n\n請確認本時段能夠出席。";

				AlertDialog.Builder doubleCheck = new AlertDialog.Builder( WhatIsay.this );
				doubleCheck.setIcon( R.drawable.ic_wcnt );
				doubleCheck.setTitle("確定嗎?");
				doubleCheck.setMessage( msg );
				doubleCheck.setNegativeButton("確  定 ", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new addTask().execute();
					}
				} );	// NefativeButton.OnClickListener()
				doubleCheck.setPositiveButton("取  消", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sure.setClickable( true );
					}
				});
				doubleCheck.show();
			}

		}

	}	// askForCheck AsyncTask

	private class addTask extends AsyncTask< Void, Void, String> {

		EditText isay = (EditText) findViewById(R.id.i_say);
		String say = "";
//		CheckBox checkbox = (CheckBox) findViewById(R.id.host_intent);
//		int checked = 2;

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				say = URLEncoder.encode( isay.getText().toString(), "UTF-8" );
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

//			if ( checkbox.isChecked() == true )
//				checked = 1;
//			else
//				checked = 0;

			SharedPreferences settings = getSharedPreferences( "Account", 0) ;
			int userId = settings.getInt( "USERID", 0 );
			String ip = new getIP().get();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "attend_1_2.php?about=" + say + "&w=20140117" + "&userId=" + userId
					+ "&intent=" + 0 + "&subjectId=" + subject + "&forum=" + forum );
			try {
				httpclient.execute( get );
				return "SUCCESS";
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if ( result.equals( "SUCCESS" ) ) {

				AlertDialog.Builder alert = new AlertDialog.Builder( WhatIsay.this );
				alert.setIcon( R.drawable.ic_wcnt );
				alert.setTitle( "報名成功" );
				alert.setMessage( "　　恭喜已完成報名手續，我們將會在每個場次前三十分鐘通知您是否報名成功。" );
				alert.setNegativeButton( "返回主頁面", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						MainActivity.finishme.finish();
						LetsConnect.finishme.finish();
						ChooseTime.finishme.finish();
						Intent intent_goback = new Intent( WhatIsay.this, MainActivity.class );
						startActivity( intent_goback );
						finish();
					}
				});
				alert.show();

			} else
				sure.setClickable( true );

		}	// onPostExecute

	}	// AsyncTask addTask

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
			case android.R.id.home:	//為up button的id 
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}