package com.pokemaps.pokemaps;

import java.sql.SQLException;
import java.util.List;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.pokemaps.pokemaps.data.DatabaseHelper;
import com.pokemaps.pokemaps.data.Item;
import com.pokemaps.pokemaps.data.Pokemon;
import com.pokemaps.pokemaps.data.User;
import com.pokemaps.pokemaps.data.UserPokemon;

public class PokemonInfoDialog extends DialogFragment {

	ImageView image;
	TextView name;
	TextView description;
	TextView type1;
	TextView type2;

	Pokemon pokemon;

	public static PokemonInfoDialog newInstance(Pokemon p) {
		PokemonInfoDialog dialog = new PokemonInfoDialog();
		dialog.setVariables(p);
		return dialog;
	}

	public void setVariables(Pokemon p) {
		pokemon = p;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pokemon_info_dialog,
				container, false);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		// set the custom dialog components - text, image and button
		image = (ImageView) rootView.findViewById(R.id.pokemonImage);
		name = (TextView) rootView.findViewById(R.id.pokemonName);
		description = (TextView) rootView.findViewById(R.id.pokemonDescription);
		type1 = (TextView) rootView.findViewById(R.id.pokemonType1);
		type2 = (TextView) rootView.findViewById(R.id.pokemonType2);

		name.setText(pokemon.getName());
		description.setText(pokemon.getDescription());
		image.setImageResource(pokemon.getImageId());

		if (pokemon.getType1() != null) {
			type1.setBackgroundResource(pokemon.getType1().getDrawable());
			type1.setText(pokemon.getType1().toString());
		} else {
			type1.setVisibility(View.GONE);
		}

		if (pokemon.getType2() != null) {
			type2.setText(pokemon.getType2().toString());
			type2.setBackgroundResource(pokemon.getType2().getDrawable());
		} else {
			type2.setVisibility(View.GONE);
		}

		return rootView;
	}

	public interface StarterPokemonDialogListener {
		void onFinishStarterDialog(String name);
	}

}
