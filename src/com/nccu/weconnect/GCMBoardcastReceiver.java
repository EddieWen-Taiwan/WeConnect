package com.nccu.weconnect;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GCMBoardcastReceiver extends WakefulBroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		// �i�Dreceiver�n�έ���IntentService�B�z
		ComponentName comp = new ComponentName( context.getPackageName(), GCMIntentService.class.getName() );
		startWakefulService( context, ( intent.setComponent( comp ) ) );
		setResultCode( Activity.RESULT_OK );
		
	}

}
