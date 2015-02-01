package com.nccu.weconnect;
//先把手機連上網路！！！！！！！！！！！！！！！！！！！！！！！！！！！！
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseTime extends Activity {

	public static Activity finishme;

	String weekDay = "", weekDay2 = "", weekDay3 = "";
	int Day_2 = 0, Day_3 = 0, Month_2 = 0, Month_3 = 0, subject = 0;
	int[] forum_check;

	String[] everyForum = new String[13];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_time);

		finishme = this;

		if ( new checkNetwork().isNetworkAvailable( ChooseTime.this ) == true ) {

			ActionBar actionbar = getActionBar();	// 關於ActionBar 
			actionbar.setDisplayHomeAsUpEnabled(true);

			reminder();	// 說明文件，不再顯示

			Intent intent_in = getIntent();
			subject = intent_in.getIntExtra( "subject", 0 );

			new getTask().execute( subject );

			ImageView banner = (ImageView) findViewById(R.id.banner);
			switch( subject ){
				case 1:		// 旅遊
					banner.setImageResource( R.drawable.subject1_banner );
					break;
				case 2:		// 創業
					banner.setImageResource( R.drawable.subject2_banner );
					break;
				case 3:		// 校園議題
					banner.setImageResource( R.drawable.subject3_banner );
					break;
				case 4:		// 公共議題
					banner.setImageResource( R.drawable.subject4_banner );
					break;
				default:
					break;
			}

			ImageView d1t1 = (ImageView) findViewById(R.id.d1t1);
				d1t1.setOnClickListener( new clickAction() );
			ImageView d1t2 = (ImageView) findViewById(R.id.d1t2);
				d1t2.setOnClickListener( new clickAction() );
			ImageView d1t3 = (ImageView) findViewById(R.id.d1t3);
				d1t3.setOnClickListener( new clickAction() );
			ImageView d1t4 = (ImageView) findViewById(R.id.d1t4);
				d1t4.setOnClickListener( new clickAction() );
			ImageView d2t1 = (ImageView) findViewById(R.id.d2t1);
				d2t1.setOnClickListener( new clickAction() );
			ImageView d2t2 = (ImageView) findViewById(R.id.d2t2);
				d2t2.setOnClickListener( new clickAction() );
			ImageView d2t3 = (ImageView) findViewById(R.id.d2t3);
				d2t3.setOnClickListener( new clickAction() );
			ImageView d2t4 = (ImageView) findViewById(R.id.d2t4);
				d2t4.setOnClickListener( new clickAction() );
			ImageView d3t1 = (ImageView) findViewById(R.id.d3t1);
				d3t1.setOnClickListener( new clickAction() );
			ImageView d3t2 = (ImageView) findViewById(R.id.d3t2);
				d3t2.setOnClickListener( new clickAction() );
			ImageView d3t3 = (ImageView) findViewById(R.id.d3t3);
				d3t3.setOnClickListener( new clickAction() );
			ImageView d3t4 = (ImageView) findViewById(R.id.d3t4);
				d3t4.setOnClickListener( new clickAction() );

			Button btn = (Button) findViewById(R.id.sure);	//確認送出的button
			btn.setOnClickListener( new goNext() );

		}	// if network is work

	}	// OnCreate 到此

	private void reminder() {
		// TODO Auto-generated method stub
		final SharedPreferences settings = getSharedPreferences( "Account", 0 );
		int dontshow = settings.getInt( "dontshow_choose", 0 );

		if( dontshow == 0 ) {

			AlertDialog.Builder alert = new AlertDialog.Builder( ChooseTime.this );
				alert.setIcon( R.drawable.ic_wcnt );
				alert.setTitle( "說明" );
				alert.setMessage( "淺灰色代表該時段暫無人選擇\n黃色代表已有一人選擇\n綠色代表該時段已有兩人以上選擇\n深灰色則可能是您已選擇過該時段或是不再開放。" );
				final LayoutInflater inflater = (LayoutInflater)
													getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View view_dontshow = inflater.inflate( R.layout.dontshow, null );
				alert.setView( view_dontshow );
				alert.setNegativeButton( "確認" , new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						CheckBox check = (CheckBox) view_dontshow.findViewById(R.id.dontshow);
						if( check.isChecked() ) {
							SharedPreferences.Editor myInfo = settings.edit();
							myInfo.putInt( "dontshow_choose" , 1 );
							myInfo.commit();
						}

					}

				});
			alert.show();

		}	// if

	}	// reminder method

	public class getTask extends AsyncTask<Integer, Void, String> {

		String[] temp;
		String a;

		@Override
		protected String doInBackground(Integer... subject) {
			// TODO Auto-generated method stub
			SharedPreferences settings = getSharedPreferences( "Account", 0 );
			int userid = settings.getInt( "USERID", 0 );

			String ip = new getIP().get();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "getCurrent.php?subject=" + subject[0] + "&userId=" + userid );
			try {
				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				if ( entity != null ) {
					return EntityUtils.toString( entity, "utf-8" );
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
			TextView mon1 = (TextView) findViewById(R.id.month1);
			TextView date1 = (TextView) findViewById(R.id.date1);
			TextView weekday1 = (TextView) findViewById(R.id.weekday1);
			TextView mon2 = (TextView) findViewById(R.id.month2);
			TextView date2 = (TextView) findViewById(R.id.date2);
			TextView weekday2 = (TextView) findViewById(R.id.weekday2);
			TextView mon3 = (TextView) findViewById(R.id.month3);
			TextView date3 = (TextView) findViewById(R.id.date3);
			TextView weekday3 = (TextView) findViewById(R.id.weekday3);

			super.onPostExecute(result);
			if( !result.equals( "FAIL" ) ) {

				temp = result.split( "<br>", 2 );

				mon1.setText( temp[0].substring( 5, 7 ) );
				date1.setText( temp[0].substring( 8, 10 ) );
				weekday1.setText( temp[0].substring( 11, 14 ) );
				mon2.setText( temp[0].substring( 20, 22 ) );
				date2.setText( temp[0].substring( 23, 25 ) );
				weekday2.setText( temp[0].substring( 26, 29 ) );
				mon3.setText( temp[0].substring( 35, 37 ) );
				date3.setText( temp[0].substring( 38, 40 ) );
				weekday3.setText( temp[0].substring( 41, 44 ) );

				everyForum = temp[1].split( "_", 13 );
				for ( int i = 1; i < 13; i++ ) {

					if( everyForum[i].equals("ERROR") ) {
						setColor( i, -1 );
					} else {
						setColor( i, Integer.valueOf(everyForum[i]) );
					}

				}	// for

			}	// if

		}	// onPostExecute - END

	}	// AsyncTask getTask

// 按下"確認"之後動作，抓取user所選的值
	private class goNext implements OnClickListener{

		int forum_selected;
		@Override
		public void onClick(View v) {

			int[] forums = {
				R.id.d1t1, R.id.d1t2, R.id.d1t3, R.id.d1t4,
				R.id.d2t1, R.id.d2t2, R.id.d2t3, R.id.d2t4,
				R.id.d3t1, R.id.d3t2, R.id.d3t3, R.id.d3t4,
			};

			int count = 0;
			ImageView image = null;

			for( int i = 0; i <= 11; i++ ) {

				if( count == 0 ) {
					image = (ImageView) findViewById( forums[i] );
					if( image.getDrawable().getConstantState().equals( getResources().getDrawable(R.drawable.circle_w_selected).getConstantState() ) ||
							image.getDrawable().getConstantState().equals( getResources().getDrawable(R.drawable.circle_y_selected).getConstantState() ) ||
							image.getDrawable().getConstantState().equals( getResources().getDrawable(R.drawable.circle_g_selected).getConstantState() ) ) {
						forum_selected = i;
						count++;
					}
				}

			}
			if( count == 0 ) {
				Toast.makeText( ChooseTime.this, "請先選擇時段", Toast.LENGTH_LONG ).show();
			} else {
				Intent intent_out = new Intent( ChooseTime.this, WhatIsay.class );
					intent_out.putExtra( "subject", subject );
					intent_out.putExtra( "forum_selected", forum_selected );
				startActivity( intent_out );
			}

		}	// onClick

	}	// goNext OnClickListener

	private void setColor(int forum, int num) {
		// TODO Auto-generated method stub
		ImageView image = null;

		switch( forum ) {
			case 1:
				image = (ImageView) findViewById(R.id.d1t1);
				break;
			case 2:
				image = (ImageView) findViewById(R.id.d1t2);
				break;
			case 3:
				image = (ImageView) findViewById(R.id.d1t3);
				break;
			case 4:
				image = (ImageView) findViewById(R.id.d1t4);
				break;
			case 5:
				image = (ImageView) findViewById(R.id.d2t1);
				break;
			case 6:
				image = (ImageView) findViewById(R.id.d2t2);
				break;
			case 7:
				image = (ImageView) findViewById(R.id.d2t3);
				break;
			case 8:
				image = (ImageView) findViewById(R.id.d2t4);
				break;
			case 9:
				image = (ImageView) findViewById(R.id.d3t1);
				break;
			case 10:
				image = (ImageView) findViewById(R.id.d3t2);
				break;
			case 11:
				image = (ImageView) findViewById(R.id.d3t3);
				break;
			case 12:
				image = (ImageView) findViewById(R.id.d3t4);
				break;
			default:
				break;
		}

		if ( num == 0 )
			image.setImageDrawable( getResources().getDrawable(R.drawable.circle_w) );
		else if ( num == 1 )
			image.setImageDrawable( getResources().getDrawable(R.drawable.circle_y) );
		else if ( num >= 2 )
			image.setImageDrawable( getResources().getDrawable(R.drawable.circle_g) );
		else if ( num == -1 ) {
			image.setImageDrawable( getResources().getDrawable(R.drawable.circle_no) );
			image.setClickable( false );
		}

	}	// method setColor

	private class clickAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			ImageView image = (ImageView) v;
			int forum = 0;
			switch( v.getId() ) {
				case R.id.d1t1:
					forum = 1;
					break;
				case R.id.d1t2:
					forum = 2;
					break;
				case R.id.d1t3:
					forum = 3;
					break;
				case R.id.d1t4:
					forum = 4;
					break;
				case R.id.d2t1:
					forum = 5;
					break;
				case R.id.d2t2:
					forum = 6;
					break;
				case R.id.d2t3:
					forum = 7; 
					break;
				case R.id.d2t4:
					forum = 8;
					break;
				case R.id.d3t1:
					forum = 9;
					break;
				case R.id.d3t2:
					forum = 10;
					break;
				case R.id.d3t3:
					forum = 11;
					break;
				case R.id.d3t4:
					forum = 12;
					break;
				default:
					break;
			}

			if( image.getDrawable().getConstantState().equals( getResources().getDrawable(R.drawable.circle_w).getConstantState() ) ) {
				image.setImageDrawable( getResources().getDrawable(R.drawable.circle_w_selected) );
			}
			else if( image.getDrawable().getConstantState().equals( getResources().getDrawable(R.drawable.circle_y).getConstantState() ) ) {
				image.setImageDrawable( getResources().getDrawable(R.drawable.circle_y_selected) );
			}
			else if( image.getDrawable().getConstantState().equals( getResources().getDrawable(R.drawable.circle_g).getConstantState() ) ) {
				image.setImageDrawable( getResources().getDrawable(R.drawable.circle_g_selected) );
			}
			else if( image.getDrawable().getConstantState().equals( getResources().getDrawable(R.drawable.circle_w_selected).getConstantState() ) ||
						image.getDrawable().getConstantState().equals( getResources().getDrawable(R.drawable.circle_y_selected).getConstantState() ) ||
						image.getDrawable().getConstantState().equals( getResources().getDrawable(R.drawable.circle_g_selected).getConstantState() ) ) {
				setColor( forum, Integer.valueOf(everyForum[forum]) );
			}	// if
			
			// clean the other time
			for( int i = 1; i <= 12; i++ ) {

				if( i != forum && !everyForum[i].equals("ERROR") ) {
					setColor( i, Integer.valueOf(everyForum[i]) );
				}

			}	// for

		}	// onClick

	}	// OnClickListener clickAction
	
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
