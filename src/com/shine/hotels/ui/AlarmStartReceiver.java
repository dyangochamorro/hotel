package com.shine.hotels.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;


public class AlarmStartReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
//		showPoptext(subtitle) ;
		Message message = new Message();  
		message.what = 1 ;
		//HostActivity.this.myHandler.sendMessage(message); 
	}
	
}
