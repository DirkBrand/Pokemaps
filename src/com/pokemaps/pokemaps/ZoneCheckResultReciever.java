package com.pokemaps.pokemaps;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.TextView;
import android.widget.Toast;

public class ZoneCheckResultReciever extends ResultReceiver {

	Receiver receiver;
	public ZoneCheckResultReciever(Handler handler) {
		super(handler);
	}
	
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
		
	}
	 // Defines our event interface for communication
	  public interface Receiver {
	      public void onReceiveResult(int resultCode, Bundle resultData);
	  }

	  // Delegate method which passes the result to the receiver if the receiver has been assigned
	  @Override
	  protected void onReceiveResult(int resultCode, Bundle resultData) {
	      if (receiver != null) {
	        receiver.onReceiveResult(resultCode, resultData);
	      }
	  }
}
