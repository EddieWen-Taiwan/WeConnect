package com.nccu.weconnect;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TeamListAdapter extends BaseAdapter {

	Context context;
	String[] teammateName, teammateMajor, teammateSay;
	int qquota, comment_quota, forumId;
	int[] teammateId;
//	View itemView;
	SharedPreferences settings;
	String content, ip;

	public TeamListAdapter( Context context, int[] teammateId, String[] teammateName,
						String[] teammateMajor, String[] teammateSay, int qquota, int forumId ) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.teammateId = teammateId;
		this.teammateName = teammateName;
		this.teammateMajor = teammateMajor;
		this.teammateSay = teammateSay;
		this.qquota = qquota;
		if( qquota != 0 )
			this.comment_quota = 1;
		else
			this.comment_quota = 0;
		this.forumId = forumId;
		this.ip = new getIP().get();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return teammateId.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return teammateId[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if( convertView == null ) {

			final LayoutInflater inflater = (LayoutInflater)
											context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if( comment_quota == 0 )
				convertView = inflater.inflate( R.layout.teamlist_sample_1, parent, false );
			else if( comment_quota == 1 )
				convertView = inflater.inflate( R.layout.teamlist_sample_2, parent, false );

			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView intro = (TextView) convertView.findViewById(R.id.introduction);
			TextView say = (TextView) convertView.findViewById(R.id.say);

			name.setText( teammateName[position] );
			intro.setText( teammateMajor[position] );
			say.setText( teammateSay[position] );

			if( comment_quota == 1 ) {

			// ���g
				final ImageView forgood = (ImageView) convertView.findViewById(R.id.good);
				forgood.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if( qquota == 1 ){
							AlertDialog.Builder alert = new AlertDialog.Builder( context );
								alert.setIcon( R.drawable.ic_wcnt );
								alert.setTitle( "���L�@���P" );
								alert.setMessage( "�A�n���o��P�Ǥ@���P�����y��?" );
								alert.setNegativeButton( "�n��", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										settings = context.getSharedPreferences( "Account", 0 );

										new plusStar().execute( teammateId[position] );
										qquota--;
										SharedPreferences.Editor myInfo = settings.edit();
										myInfo.putInt( "temp_quota", 0 );

										forgood.setImageDrawable( context.getResources().getDrawable( R.drawable.star ) );

										int quota = settings.getInt( "QUOTA", 0 );
										quota--;
										myInfo.putInt( "QUOTA", quota );
										myInfo.commit();
									}
								});
								alert.setPositiveButton( "�A�Q�Q��", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
									}
								});
							alert.show();

						} else
							Toast.makeText( context, "Oops�A�u����@���g��", Toast.LENGTH_SHORT ).show();
					}

				} );	// setOnClickListener ���g

			// ����
				final ImageView comment = (ImageView) convertView.findViewById(R.id.comment);
				comment.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if( comment.getDrawable().getConstantState().equals( context.getResources().getDrawable(R.drawable.comment_black).getConstantState() ) ) {
						
							final View criticism = inflater.inflate( R.layout.criticism_alert, null );
							AlertDialog.Builder alert = new AlertDialog.Builder( context );
								alert.setTitle( "���L�ӵ��y�a :)" );
								alert.setIcon( R.drawable.ic_wcnt );
								alert.setView( criticism );
								alert.setMessage( "��" + teammateName[position] + "���y�ܧa!" );
								alert.setNegativeButton( "�T�w", new DialogInterface.OnClickListener() {
	
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										EditText criticism_content = (EditText) criticism.findViewById(R.id.criticism);
										try {
											content = URLEncoder.encode( criticism_content.getText().toString(), "UTF-8" );
										} catch (UnsupportedEncodingException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
	
										comment.setImageDrawable( context.getResources().getDrawable(R.drawable.comment_blue) );
										comment.setClickable(false);
										new addCriticism().execute( teammateId[position] );
										Toast.makeText( context, "�H�g���N�����d", Toast.LENGTH_SHORT ).show();
	
									}
	
								});
								alert.setPositiveButton( "����", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
									}
								});
							alert.show();
							
						} else
							Toast.makeText( context, "�w�g�d�����L�L�o", Toast.LENGTH_SHORT ).show();

					}	// OnClick

				});	// OnClickListener

			// ���|
				final ImageView absence = (ImageView) convertView.findViewById(R.id.absence);
				absence.setOnClickListener( new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if( absence.getDrawable().getConstantState().equals( context.getResources().getDrawable(R.drawable.report_b).getConstantState() ) ) {

							AlertDialog.Builder alert = new AlertDialog.Builder( context );
								alert.setIcon( R.drawable.ic_wcnt );
								alert.setTitle( "���|" );
								alert.setMessage( "�L���ѵL�G�{�ɯʮu��?" );
								alert.setNegativeButton( "�O���A���|", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										absence.setImageDrawable( context.getResources().getDrawable(R.drawable.report_red) );
										absence.setClickable(false);
										new absenceTask().execute( teammateId[position] );
										Toast.makeText( context, "�w���|", Toast.LENGTH_SHORT ).show();
									}

								});
								alert.setPositiveButton( "�èS��", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
									}
								});
							alert.show();

						}

					}// OnClick

				});	// OnClickListener

			}	// if �٦�quota�n���g

		}	// if convertView == null

		return convertView;

	}	// getView

	private class plusStar extends AsyncTask<Integer, Void, String> {

		@Override
		protected String doInBackground(Integer... teammateId) {
			// TODO Auto-generated method stub
			int userId = settings.getInt( "USERID", 0 );

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "userPlus.php?T_userId=" + teammateId[0] + "&userId=" + userId );
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
			return "FAIL";
		}	// doInBackground

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if( result.equals( "SUCCESS" ) )
				Toast.makeText( context, "���\�e�X�P�P", Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText( context, "something wrong", Toast.LENGTH_SHORT ).show();
		}

	}	// plusStar AsyncTask

	private class addCriticism extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... beId) {
			// TODO Auto-generated method stub
			SharedPreferences settings = context.getSharedPreferences( "Account", 0 );
			int userId = settings.getInt( "USERID", 0 );

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "criticize.php?userId=" + userId + "&forumId=" + forumId
					+ "&beCriticized=" + beId[0] + "&content=" + content );
			try {
				httpclient.execute( get );
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}	// doInBackground

	}	// addCriticism AsyncTask

	private class absenceTask extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... userId) {
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet( ip + "reportAbsence.php?userId=" + userId[0] );
			try {
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

	}	// absenceTask AsyncTask

}
