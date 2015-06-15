package com.pokemaps.pokemaps;

import com.pokemaps.pokemaps.NewGameDialog.NewGameDialogListener;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class StarterPokemonDialog extends DialogFragment {

	RadioButton starterpokemon1;
	RadioButton starterpokemon2;
	RadioButton starterpokemon3;

	TextView text;

	RadioGroup radioGroup;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.starter_pokemon_dialog, container,
				false);

		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setCancelable(false);
		// set the custom dialog components - text, image and button
		text = (TextView) rootView.findViewById(R.id.starterText);
		text.setText("Hi, pick a starter pokemon!");

		radioGroup = (RadioGroup) rootView.findViewById(R.id.pokemonContainer);

		starterpokemon1 = (RadioButton) rootView
				.findViewById(R.id.starterpokemon1);
		starterpokemon2 = (RadioButton) rootView
				.findViewById(R.id.starterpokemon2);
		starterpokemon3 = (RadioButton) rootView
				.findViewById(R.id.starterpokemon3);

		Button dialogButton = (Button) rootView
				.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String name = "";
				int image_id = 0;
				switch(radioGroup.getCheckedRadioButtonId()) {
				case R.id.starterpokemon1:
					name = "Bulbasaur";
					break;
				case R.id.starterpokemon2:
					name = "Charmander";
					break;
				case R.id.starterpokemon3:
					name = "Squirtle";
							
					break;
				}
				StarterPokemonDialogListener activity = (StarterPokemonDialogListener) getActivity();
				activity.onFinishStarterDialog(name);
				dismiss();
			}
		});

		return rootView;
	}
	
	public interface StarterPokemonDialogListener {
	    void onFinishStarterDialog(String name);
	}

}
