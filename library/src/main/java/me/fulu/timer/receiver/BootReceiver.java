package me.fulu.timer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.fulu.timer.service.ResetIntentService;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		Log.e("BootReceiver", "BootReceiver ING " + intent.getAction());
		Intent serviceIntent = new Intent(arg0, ResetIntentService.class);
		arg0.startService(serviceIntent);
	}
}
