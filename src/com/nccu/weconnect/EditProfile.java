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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

public class EditProfile extends Activity {

	int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);

		if( new checkNetwork().isNetworkAvailable( EditProfile.this ) == true ) {

			SharedPreferences settings = getSharedPreferences( "Account", 0 );
			id = settings.getInt( "USERID", 0 );

			new getInfoTask().execute();

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

	}

// 取得user目前的資料 並填入edittext中
	private class getInfoTask extends AsyncTask<Integer, Void, String>{
		
		String strEntity;
		String[] info;

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			String ip = new getIP().get();
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "getPersonalInfo_1_1.php?userId=" + id );
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
			info = strEntity.split( "<br>" );

			return "SUCCESS";
		}	// doInBackground

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if( result.equals( "SUCCESS") ) {
				EditText name = (EditText) findViewById(R.id.editname);
					name.setText( info[0] );
				EditText major = (EditText) findViewById(R.id.editmajor);
					major.setText( info[1] );
				EditText club = (EditText) findViewById(R.id.editclub);
					club.setText( info[3] );
				EditText work = (EditText) findViewById(R.id.editwork);
					work.setText( info[4] );
				EditText story = (EditText) findViewById(R.id.editstory);
					story.setText( info[5] );
			}

		}	// onPostExecute
		
	}	// getInfoTask AsyncTask

// 按下確認 或是 返回上一頁都要執行這個 
	private void leaveThisPage() {
		// TODO Auto-generated method stub

		AlertDialog.Builder alert = new AlertDialog.Builder( EditProfile.this );
			alert.setIcon( R.drawable.ic_wcnt );
			alert.setTitle( "離開編輯頁面?" );
			alert.setMessage( "您將離開此頁面，是否要儲存對個人資料的變更?" );
			alert.setNegativeButton( "儲存", new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new editTask().execute();
					PersonalFile.finishme.finish();
	
					Intent intent = new Intent( EditProfile.this, PersonalFile.class );
					intent.putExtra( "comefrom", 1 );
					startActivity( intent );
	
					finish();
				}
	
			});
			alert.setPositiveButton( "放棄", new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					PersonalFile.finishme.finish();
	
					Intent intent = new Intent( EditProfile.this, PersonalFile.class );
					intent.putExtra( "comefrom", 1 );
					startActivity( intent );
	
					finish();
				}
	
			});
			alert.setNeutralButton( "繼續編輯", new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
						// do nothing
				}
	
			});
		alert.show();

	}	// leaveThisPage

// 確認，編入資料庫。
	private class editTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String ip = new getIP().get();

			EditText editname = (EditText) findViewById(R.id.editname);
			EditText editmajor = (EditText) findViewById(R.id.editmajor);
			EditText editclub = (EditText) findViewById(R.id.editclub);
			EditText editwork = (EditText) findViewById(R.id.editwork);
			EditText editstory = (EditText) findViewById(R.id.editstory);
			String name = null, major = null, club = null, work = null, story = null;
			try {
				name = URLEncoder.encode( editname.getText().toString(),"UTF-8" );
				major = URLEncoder.encode( editmajor.getText().toString(),"UTF-8" );
				club = URLEncoder.encode( editclub.getText().toString(),"UTF-8" );
				work = URLEncoder.encode( editwork.getText().toString(),"UTF-8" );
				story = URLEncoder.encode( editstory.getText().toString(),"UTF-8" );
			} catch (UnsupportedEncodingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String url = ip + "editInfo_1_1.php?userId=" + id + "&name_content=" + name +
					"&major_content=" + major + "&club_content=" + club + "&work_content=" + work + "&story_content=" + story;

			HttpClient httpclient = new DefaultHttpClient();
			try {
				HttpGet get = new HttpGet( url );
				httpclient.execute( get );
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complete, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		leaveThisPage();
//		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch( item.getItemId() ) {
			case R.id.action_complete:
			// 直接儲存
				new editTask().execute();
				PersonalFile.finishme.finish();

				Intent intent = new Intent( EditProfile.this, PersonalFile.class );
				intent.putExtra( "comefrom", 1 );
				startActivity( intent );

				finish();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
