package com.nccu.weconnect;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends Activity {

	Intent intent;
	TextView share, communicate, dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		if( new checkNetwork().isNetworkAvailable( SplashScreen.this ) == true ){

			ActionBar actionbar = getActionBar();
			actionbar.hide();

			SharedPreferences settings = getSharedPreferences( "Account", 0 );
			int userId = settings.getInt( "USERID", 0 );

			if( userId == 0 )
				intent = new Intent( SplashScreen.this, LoginAccount.class );
			else {
				int auth = new getAuthorization().get( SplashScreen.this );
				SharedPreferences.Editor myInfo = settings.edit();
				myInfo.putInt( "AUTH", auth );
				myInfo.commit();

				int quota = settings.getInt( "QUOTA", 0 );
				intent = new Intent( SplashScreen.this, MainActivity.class );
				if( quota >= 1 )
					intent.putExtra( "goTeam", 1 );
			}

			final TextView slogan = (TextView) findViewById(R.id.slogan);
			new Thread(new Runnable(){

				@Override
				public void run() {

					slogan.setAnimation( AnimationUtils.loadAnimation( SplashScreen.this, R.anim.push_up_in ) );
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//接下來轉跳到app的主畫面
					startActivity( intent );

					finish();
				}

			}).start();

		}	// if Network is work

	}

}
