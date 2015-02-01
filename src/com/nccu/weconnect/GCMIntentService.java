package com.nccu.weconnect;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GCMIntentService extends IntentService {
	
	// 不知道在幹嘛
	public GCMIntentService() {
		super("GCMIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Bundle extra = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance( this );
		String messageType = gcm.getMessageType( intent );
		
		if( !extra.isEmpty() ){
			
			if( GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals( messageType ) ) {
				// It's an error.
				sendNotification( "Send error: " + extra.toString() );
			} else if ( GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals( messageType ) ) {
				// Deleted messages on the server.
				sendNotification( "Deleted messages on server: " + extra.toString() );
			} else if ( GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals( messageType ) ) {
				// It's a regular GCM message, do some work.
				
//				This loop represents the service doing some work.
//				for( int i = 0; i < 3; i++ ) {
//					try {
//						Thread.sleep( 500 );
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
				
				sendNotification( extra.getString( "message" ) );

			}

		}	// if _ !extra.isEmpty()

	}	// onHandleIntent

	private void sendNotification(String msg) {
		
		String message = null, title = null;
		Intent NotifyIntent = null;

		if( msg.equals( "SUCCESS" ) ) {
			title = "配對結果出爐";
			message = "恭喜配對成功";
			NotifyIntent = new Intent( this, TeamInfo.class );
		} else if( msg.contains( "FAIL" ) ) {
			title = "配對結果出爐"; 
			message = "很可惜....";
			NotifyIntent = new Intent( this, MatchFail.class );
			NotifyIntent.putExtra( "msg", msg );
		}
		
		PendingIntent pending = PendingIntent.getActivity( this, 0, NotifyIntent,   
																	PendingIntent.FLAG_UPDATE_CURRENT);  
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
						.setAutoCancel(true)
						.setTicker( "WeConnect快報" )
						.setContentTitle( title )
						.setSmallIcon( R.drawable.ic_wcnt )
						.setContentText( message )
						.setContentInfo( "WeConnect" )
						.setContentIntent( pending );

		Notification notification = builder.build();
		NotificationManager ngr = (NotificationManager) getSystemService( NOTIFICATION_SERVICE );
		ngr.notify( 1, notification );

	}

}
