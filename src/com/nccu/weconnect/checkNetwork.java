package com.nccu.weconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class checkNetwork {

	public boolean isNetworkAvailable( Context context ){
		int state = 0;

		ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		if( cm == null ) {
			state = 0;
		} else {
			NetworkInfo[] infos = cm.getAllNetworkInfo();
			if( infos != null ) {

				for( NetworkInfo networkInfo : infos ){
					if( networkInfo.getState() == NetworkInfo.State.CONNECTED )
						state = 1;
				}

			}	// if

		}	// if else

		if( state == 1 )
			return true;
		else {
			AlertDialog.Builder alert = new AlertDialog.Builder( context );
			alert.setIcon( R.drawable.ic_wcnt );
			alert.setTitle("�������~");
			alert.setMessage( "�����s�u�X�F���D�A�Х��ˬd�O�_�s�W�����C" );
			alert.setNegativeButton("�T  �w", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			} );	// NefativeButton.OnClickListener()
			alert.show();

			return false;
		}

	}	// method isNetWorkAvailable

}
