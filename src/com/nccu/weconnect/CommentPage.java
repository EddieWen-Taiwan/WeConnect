package com.nccu.weconnect;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CommentPage extends Activity {
	
	String[] comment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_page);
		
		if( new checkNetwork().isNetworkAvailable( CommentPage.this ) == true )	{
			new getComment().execute();

			ActionBar actionbar = getActionBar();	//		關於ACtionBar 
			actionbar.setDisplayHomeAsUpEnabled(true);
		}

	}

	private class getComment extends AsyncTask<Void,Void,String> {

		String strEntity;

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String ip = new getIP().get();
			SharedPreferences settings = getSharedPreferences( "Account", 0 );
			int id = settings.getInt( "USERID", 0 );

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "getComment.php?userId=" + id );
			try {

				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				strEntity = EntityUtils.toString( entity, "utf-8" );
				if( !strEntity.equals( "null" ) ) {
					comment = strEntity.split( "_" );
					return "SUCCESS";
				} else if( strEntity.equals( "null" ) ) {
					comment = new String[]{"目前還沒有人對你留下評語喔"};
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
			if( result.equals( "SUCCESS" ) ) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>
						( CommentPage.this, android.R.layout.simple_list_item_1, comment );
				ListView list = (ListView) findViewById(R.id.list_comment);
				list.setAdapter( adapter );
			}

		}

	}	// getComment Extends AsyncTask

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
