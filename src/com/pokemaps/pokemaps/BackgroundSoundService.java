package com.pokemaps.pokemaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BackgroundSoundService extends Service {
	private static final String TAG = null;
	MediaPlayer player;
	int songIds[] = new int[] { R.raw.ceruleantheme, R.raw.gtm_analytics,
			R.raw.oakresearchlab, R.raw.palettetowntheme,
			R.raw.pewtercitystheme, R.raw.roadtocerulean, R.raw.vermilliontheme };
	int position = 0;
	Random rand = new Random();
	
    private int length = 0;
	private final IBinder mBinder = new ServiceBinder();

	public class ServiceBinder extends Binder {
		public BackgroundSoundService getService() {
			return BackgroundSoundService.this;
		}
	}

	public IBinder onBind(Intent arg0) {

		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		position = rand.nextInt(songIds.length);
		player = MediaPlayer.create(this, songIds[position]);
		if (player != null) {
			player.setVolume(100, 100);
			player.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					position = (position + 1) % songIds.length;
					AssetFileDescriptor afd = getApplicationContext()
							.getResources()
							.openRawResourceFd(songIds[position]);

					try {
						mp.reset();
						mp.setDataSource(afd.getFileDescriptor(),
								afd.getStartOffset(), afd.getDeclaredLength());
						mp.prepare();
						length = 0;
						mp.start();
						afd.close();
					} catch (IllegalArgumentException e) {
						Log.e(TAG,
								"Unable to play audio queue do to exception: "
										+ e.getMessage(), e);
					} catch (IllegalStateException e) {
						Log.e(TAG,
								"Unable to play audio queue do to exception: "
										+ e.getMessage(), e);
					} catch (IOException e) {
						Log.e(TAG,
								"Unable to play audio queue do to exception: "
										+ e.getMessage(), e);
					}
				}
			});
		}

	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		player.start();
		return START_STICKY;
	}




	public void pauseMusic()
	{
		if(player.isPlaying())
		{
			player.pause();
			length=player.getCurrentPosition();

		}
	}

	public void resumeMusic()
	{
		if(player.isPlaying()==false)
		{
			player.seekTo(length);
			player.start();
		}
	}

	public void stopMusic()
	{
		player.stop();
		player.release();
		player = null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (player != null) {
			player.stop();
			player.release();
		}
	}

	@Override
	public void onLowMemory() {

	}
}
