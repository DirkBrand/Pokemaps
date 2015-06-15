package com.pokemaps.pokemaps;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";

	Button continueGameButton;
	Button startGameButton;

	MediaPlayer player;
	boolean keepMusicGoing;

	private boolean mIsBound = false;
	private BackgroundSoundService mServ;
	private ServiceConnection Scon = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder binder) {
			mServ = ((BackgroundSoundService.ServiceBinder) binder)
					.getService();
		}

		public void onServiceDisconnected(ComponentName name) {
			mServ = null;
		}
	};

	void doBindService() {
		bindService(new Intent(this, BackgroundSoundService.class), Scon,
				Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	void doUnbindService() {
		if (mIsBound) {
			unbindService(Scon);
			mIsBound = false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Start music service
		Intent music = new Intent();
		music.setClass(this, BackgroundSoundService.class);
		startService(music);

		doBindService();

		startGameButton = (Button) findViewById(R.id.startGameButton);
		startGameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						MainMapActivity.class);
				intent.putExtra("new_game", true);
				startActivity(intent);

			}
		});

		continueGameButton = (Button) findViewById(R.id.continueGameButton);
		continueGameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						MainMapActivity.class);
				startActivity(intent);

			}
		});

	}

	@Override
	protected void onStop() {
		super.onPause();
		if (mServ != null)
			mServ.pauseMusic();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mServ != null)
			mServ.resumeMusic();
	}

	@Override
	protected void onDestroy() {
		super.onPause();
		if (mServ != null)
			mServ.stopMusic();
		
		doUnbindService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (player != null)
			player.start();

		SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
		int user_id = sharedPref.getInt("user_id", -1);
		Log.i("pokemaps", user_id + "");
		if (user_id == -1) {
			continueGameButton.setEnabled(false);
		} else {
			continueGameButton.setEnabled(true);
			;
		}

	}

}
