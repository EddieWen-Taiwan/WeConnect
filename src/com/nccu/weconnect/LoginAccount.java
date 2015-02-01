package com.nccu.weconnect;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class LoginAccount extends Activity {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private final static String TAG = "LaunchActivity";
	private String Sender_ID = "16930331867";
	private String reg_Id = null;
	private GoogleCloudMessaging gcm = null;
	private Context context = null;
	public static Activity finishme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_account);

		finishme = this;

		if( new checkNetwork().isNetworkAvailable( LoginAccount.this ) == true ) {

			context = getApplicationContext();
			if( checkGooglePlay() ){
				gcm = GoogleCloudMessaging.getInstance( context );
				reg_Id = getRegistrationId( this );

				if( reg_Id.isEmpty() ) {
					new getRegistrationTask().execute();
				}

			}

		// Set up the login form.
			mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
			mEmailView = (EditText) findViewById(R.id.email);
			mEmailView.setText(mEmail);

			Button btn = (Button) findViewById(R.id.gotoregister);
			btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent( LoginAccount.this, Register.class );
					startActivity(intent);
				}
				
			});
	
			mPasswordView = (EditText) findViewById(R.id.password);
			mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int id,
						KeyEvent keyEvent) {
					if ( id == R.id.login || id == EditorInfo.IME_NULL ) {
						attemptLogin();
						return true;
					}
					return false;
				}
			});

			mLoginFormView = findViewById(R.id.login_form);
			mLoginStatusView = findViewById(R.id.login_status);
			mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

			findViewById(R.id.sign_in_button).setOnClickListener( new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					attemptLogin();
				}
			});
			
			ScrollView screen = (ScrollView) findViewById(R.id.login_form);
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

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}	// attemptLogin

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}	// showProgress

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

		String strEntity;
		String[] temp = new String[4];
		SharedPreferences settings;
		SharedPreferences.Editor myInfo;
		@Override
		protected Integer doInBackground(Void... params) {

			settings = getSharedPreferences( "Account", 0 );
			String registrationId = settings.getString( "Regist_Id", "" );
			String ip = new getIP().get();

		// 0為錯誤  1為成功登入		2為密碼錯誤		3為建立新帳號  
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "checkAccount.php?w=3jrekfn7&account=" + mEmail + "&pass=" + mPassword );

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

			if ( strEntity.equals( "3" ) ) {
				return 3;
			} else  {

				if( strEntity.equals( "2" ) )
					return 2;	// 密碼錯誤
				else {
					temp = strEntity.split( "_" );

					myInfo = settings.edit();
					myInfo.putInt( "USERID", Integer.valueOf( temp[0] ) );
					myInfo.putString( "NAME", temp[2] );
						if( temp[3].equals( "no" ) )
							myInfo.putInt( "QUOTA", 0 );
						else
							myInfo.putInt( "QUOTA", Integer.valueOf( temp[3] ) );
					myInfo.commit();
					get = new HttpGet( ip + "checkAccount.php?w=sdivj9we&account=" + mEmail
													+ "&registrationId=" + registrationId );
					try {
						httpclient.execute( get );
						return 1;
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			// TODO: attempt authentication against a network service.
			// Account exists, return true if the password matches.
			// TODO: register the new account here.
			return null;

		}	// doInBackground

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final Integer success) {
			mAuthTask = null;
			showProgress(false);

			switch( success ) {
				case 1:	// Success
					int auth = new getAuthorization().get( LoginAccount.this );

					myInfo.putInt( "AUTH", auth );
					myInfo.commit();

					SharedPreferences settings = getSharedPreferences( "Account", 0 );
					int quota = settings.getInt( "QUOTA", 0 );

					Intent intent_out = new Intent( LoginAccount.this, MainActivity.class );
					if( quota >= 1 )
						intent_out.putExtra( "goTeam", 1 );

					startActivity( intent_out );
					finish();
					break;
				case 2:	// 密碼錯誤
					mPasswordView.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
					break;
				case 3:	// 建立新帳號
					mPasswordView.setError("create new account");

					AlertDialog.Builder alert = new AlertDialog.Builder( LoginAccount.this );
						alert.setIcon( R.drawable.ic_wcnt );
						alert.setTitle( "提醒" );
						alert.setMessage( "尚未成為WeConnect用戶，是否要註冊?" );
						alert.setNegativeButton( "確認", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent( LoginAccount.this, Register.class );
								startActivity( intent );
							}
						} );
						alert.setPositiveButton( "暫時不要", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								//	那就不要
							}
						} );
					alert.show();
					break;
				default:
					break;
			}	// switch

		}	// onPostExecute

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}	// AsyncTask UserLoginTask

// 以  下  註  冊  registration ID

// 檢查手機中是否有Google Play存在
	private boolean checkGooglePlay() {
		int existing = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if( existing != ConnectionResult.SUCCESS ){
			if ( GooglePlayServicesUtil.isUserRecoverableError(existing) ) {
				GooglePlayServicesUtil.getErrorDialog( existing, this, 9000 ).show();
			} else {
				finish();
			}
			return false;
		}
		return true;
	}

// 取得目前手機中所存的ID
	private String getRegistrationId(Context context) {

		SharedPreferences settings = getSharedPreferences( "Account", 0 );
		String registrationId = settings.getString( "Regist_Id", "" );
		if( registrationId.isEmpty() )
			return "";

		int registrationVersion = settings.getInt( "Regist_Version", 0);
		int currentVersion = getApplicationVersion( this );
		if( currentVersion != registrationVersion )
			return "";

		return registrationId;
	}
	
// 取得app當前版本
	private static int getApplicationVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo( context.getPackageName(), 0 );
			return packageInfo.versionCode;
		} catch( NameNotFoundException e ) {
			throw new RuntimeException( "Could not get package name: " + e );
		}
	}

// 註冊個ID
	private class getRegistrationTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String msg = "";
			try {
				if( gcm == null ) {
					gcm = GoogleCloudMessaging.getInstance( context );
				}
				reg_Id = gcm.register( Sender_ID );
				Log.d( TAG, "########################################" );
				Log.d( TAG, "Current Device's Registration ID is: " + msg );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}	// doInBackground

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			SharedPreferences settings = getSharedPreferences( "Account", 0 );
			SharedPreferences.Editor myInfo = settings.edit();
			myInfo.putString( "Regist_Id", reg_Id );
			myInfo.putInt( "Regist_Version", getApplicationVersion( context ) );
			myInfo.commit();
		}	// onPostExecute

	}	// registerTask

}
