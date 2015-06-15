package com.pokemaps.pokemaps;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ZoneCheckService extends IntentService {

	ResultReceiver rec = null;

	public ZoneCheckService(String name) {
		super(name);
	}

	public ZoneCheckService() {
		this("ZoneCheckService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		// Extract the receiver passed into the service
		if (rec == null) {
			rec = intent.getParcelableExtra("receiver");
		}

		if (rec != null) {
			// To send a message to the Activity, create a pass a Bundle
			Bundle bundle = new Bundle();
			bundle.putString("resultValue", "TIME!");
			// Here we call send passing a resultCode and the bundle of extras
			rec.send(Activity.RESULT_OK, bundle);
		}

	}

}
