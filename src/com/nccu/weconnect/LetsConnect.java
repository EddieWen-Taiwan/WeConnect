package com.nccu.weconnect;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LetsConnect extends Activity {

	public static Activity finishme;
	public static String[] subject_note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lets_connect);

		finishme = this;

		if( new checkNetwork().isNetworkAvailable( LetsConnect.this ) == true ) {

			ActionBar actionbar = getActionBar();	//		關於ACtionBar 
			actionbar.setDisplayHomeAsUpEnabled(true);

			try {
				new getNote().execute().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String[] subject_title = new String[]{ "校園議題", "公共議題", "創業", "旅行" };

			int[] subject = new int[]{ R.drawable.subject1, R.drawable.subject2, R.drawable.subject3, R.drawable.subject4 };

			subjectAdapter subjectadapter = new subjectAdapter( LetsConnect.this, subject_title, subject_note, subject );
			ListView subjectlist = (ListView) findViewById(R.id.subject_list);

			subjectlist.setAdapter( subjectadapter );
			subjectlist.setOnItemClickListener( new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
					// TODO Auto-generated method stub
					Intent intent_go = new Intent( LetsConnect.this, ChooseTime.class );
					switch( position ) {
						case 0:		// 校園議題
							intent_go.putExtra( "subject", 1 );
							break;
						case 1:		// 公共議題
							intent_go.putExtra( "subject", 2 );
							break;
						case 2:		// 創業
							intent_go.putExtra( "subject", 3 );
							break;
						case 3:		// 旅行
							intent_go.putExtra( "subject", 4 );
							break;
						default:
							break;
					}
					startActivity( intent_go );
				}

			});

		}	// if network is work

	}	// onCreate
	
	public class getNote extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String ip = new getIP().get();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "getNote.php" );
			try {
				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				String strEntity = EntityUtils.toString( entity, "utf-8" );
				subject_note = strEntity.split( "<br>" );
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}	// doInBackground

	}	// getNote AsyncTask

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		MainActivity.finishme.finish();
		Intent intent_main = new Intent( LetsConnect.this, MainActivity.class );
		startActivity( intent_main );

		finish();
//		super.onBackPressed();
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
