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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends Activity {
	
	private EditText et_account;
	private String addAccount;
//	String[] edu_email = { "NCCU", "NTU" };
//	int[] edu_logo = { R.drawable.nccu_logo, R.drawable.ntu_logo };
	String[] edu_email = { "NCCU" };
	int[] edu_logo = { R.drawable.nccu_logo };
	String email = "";
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_new);

		if( new checkNetwork().isNetworkAvailable( Register.this ) == true ) {

			ActionBar actionbar = getActionBar();	//		關於ACtionBar 
			actionbar.setDisplayHomeAsUpEnabled(true);

			Spinner spinner_email = (Spinner) findViewById(R.id.email);
			DrawerAdapter adapter = new DrawerAdapter( Register.this, edu_email, edu_logo, 1 );

			spinner_email.setAdapter( adapter );
			spinner_email.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> arg0, View v, int position, long arg3) {
					// TODO Auto-generated method stub
					switch( position ) {
						case 0:
							email = "@nccu.edu.tw";
							break;
//						case 1:
//							email = "@ntu.edu.tw";
//							break;
						default:
							break;
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
				}

			});

			et_account = (EditText) findViewById(R.id.enter_account);
			btn = (Button) findViewById(R.id.register);
			btn.setOnClickListener( new RegisterAction() );

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

	}	// OnCreate

	private class RegisterAction implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub	
			et_account.setError(null);
			addAccount = et_account.getText().toString();

			if( TextUtils.isEmpty( addAccount ) ) {
				et_account.setError( "請輸入學號" );
				et_account.requestFocus();
			} else {
				if( !email.equals( "@nccu.edu.tw" ) )
					Toast.makeText( Register.this, "目前只開放政大學生喔~", Toast.LENGTH_LONG).show();
				else {
					btn.setClickable( false );
					new RegisterTask().execute();
					Toast.makeText( Register.this, "請稍候片刻", Toast.LENGTH_LONG).show();
				}
			}

		}	// onClick

	}	// OnClickListener RegisterAction

	private class RegisterTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String ip = new getIP().get();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "registerMail/register.php?account=" + addAccount + email );
			try {
				HttpResponse response = httpclient.execute( get );
				HttpEntity entity = response.getEntity();
				String answer = EntityUtils.toString( entity, "utf-8" );

				if ( answer.contains( "Message sent!" ) )
					return 1;

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return 0;
		}	// doInBackground

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			AlertDialog.Builder alert = new AlertDialog.Builder(Register.this);
				if( result == 1 ) {
					alert.setIcon( R.drawable.ic_wcnt );
					alert.setTitle( "Congratulation" );
					alert.setMessage("請至學生信箱等待官方認證信件並完成註冊。歡迎使用WeConnect^___^" );
					alert.setNegativeButton( "確  認", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							LoginAccount.finishme.finish();
							Intent intent = new Intent( Register.this, LoginAccount.class );
							startActivity( intent );
	
							finish();
						}
					});
				} else {
					alert.setIcon( R.drawable.ic_wcnt );
					alert.setTitle( "Error" );
					alert.setMessage( "RRRRRRR" );
					alert.setNegativeButton( "重  新  註  冊", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
				}
			alert.show();
			btn.setClickable( true );
		}	// onPostExecute

	}	// AsyncTask RegisterTask
	
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
