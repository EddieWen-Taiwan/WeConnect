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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class getAuthorization {
	
	Context context;
	int auth;

	public int get(Context context) {
		// TODO Auto-generated method stub
		this.context = context;

		try {
			new getAuth().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			return auth;
	}

	private class getAuth extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			final SharedPreferences settings = context.getSharedPreferences( "Account", 0 );

			int userId = settings.getInt( "USERID", 0 );
			String ip = new getIP().get();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "getAuthorization.php?userId=" + userId );
			try {
				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				String strEntity = EntityUtils.toString( entity, "utf-8" );

				String[] temp = strEntity.split( "_" );
				auth = Integer.valueOf( temp[0] );

				SharedPreferences.Editor myInfo = settings.edit();
				if( !temp[1].equals( "no" ) ) {
					myInfo.putInt( "QUOTA", Integer.valueOf( temp[1] ) );
				} else {
					myInfo.putInt( "QUOTA", 0 );
				}
				myInfo.commit();

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

}
