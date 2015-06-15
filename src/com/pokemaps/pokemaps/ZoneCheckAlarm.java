package com.pokemaps.pokemaps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

public class ZoneCheckAlarm extends BroadcastReceiver {
	public static final int REQUEST_CODE = 12345;
	public static final String ACTION = "com.pokemaps.pokemaps.alarm";

	@Override
	public void onReceive(Context context, Intent intent) {

		ResultReceiver rec = intent.getParcelableExtra("receiver");
		Intent i = new Intent(context, ZoneCheckService.class);
		i.putExtra("receiver", rec);
		context.startService(i);
	}
}
