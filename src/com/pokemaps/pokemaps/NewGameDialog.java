package com.pokemaps.pokemaps;

import com.pokemaps.pokemaps.data.User;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class NewGameDialog extends DialogFragment {

	private RadioGroup radioGroup;
	private RadioButton radioSexButton;

	EditText mUsername;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.new_game_dialog, container,
				false);

		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setCancelable(false);
		// set the custom dialog components - text, image and button
		mUsername = (EditText) rootView.findViewById(R.id.username);

		radioGroup = (RadioGroup) rootView.findViewById(R.id.radioSex);


		Button dialogButton = (Button) rootView
				.findViewById(R.id.register_done_button);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {		
		        radioSexButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());		
		        String username = mUsername.getText().toString();
		        int sex = User.MALE;
		        if (radioSexButton.getText().toString().equalsIgnoreCase("female")) {
		        	sex = User.FEMALE;
		        }
		        
				NewGameDialogListener activity = (NewGameDialogListener) getActivity();
				activity.onFinishNewGameDialog(username, sex);
				dismiss();
			}
		});

		return rootView;
	}
	
	public interface NewGameDialogListener {
	    void onFinishNewGameDialog(String username, int sex);
	}

}
