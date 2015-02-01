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
		
		// 告訴receiver要用哪個IntentService處理
		ComponentName comp = new ComponentName( context.getPackageName(), GCMIntentService.class.getName() );
		startWakefulService( context, ( intent.setComponent( comp ) ) );
		setResultCode( Activity.RESULT_OK );
		
	}

}
