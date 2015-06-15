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

public class PokemonAttackedDialog extends DialogFragment {

	ImageView image;
	Button btnCatch;
	Button btnRun;
	TextView encounterText;

	Pokemon pokemon;
	User currentUser;

	public static PokemonAttackedDialog newInstance(Pokemon p, User u) {
		PokemonAttackedDialog dialog = new PokemonAttackedDialog();
		dialog.setVariables(p, u);
		return dialog;
	}

	public void setVariables(Pokemon p, User u) {
		pokemon = p;
		currentUser = u;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pokemon_attacked_dialog,
				container, false);

		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		setCancelable(false);
		// set the custom dialog components - text, image and button
		image = (ImageView) rootView.findViewById(R.id.pokemonImage);
		btnCatch = (Button) rootView.findViewById(R.id.btnCatch);
		btnRun = (Button) rootView.findViewById(R.id.btnRun);
		encounterText = (TextView) rootView.findViewById(R.id.encounterText);

		encounterText.setText("A wild pokemon " + pokemon.getName()
				+ " attacked!");
		image.setImageResource(pokemon.getImageId());

		btnCatch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatabaseHelper databaseHelper = OpenHelperManager.getHelper(
						getActivity(), DatabaseHelper.class);
				try {
					// TODO: Check if already have?
					databaseHelper.getUserPokemonDao().create(
							new UserPokemon(currentUser, pokemon));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dismiss();
				
				
				List<Item> userItems = null;
				try {
					userItems = databaseHelper.lookupItemsForUser(currentUser);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Item pokeball = null;
				// TODO: other balls?
				for (Item i : userItems) {
					if (i.getName().equalsIgnoreCase("pokeball"))
						pokeball = i;
				}

				if (Utilities.calculate_catch(currentUser, pokemon, pokeball)) {
					Toast.makeText(getActivity().getApplicationContext(),
							"You caught " + pokemon.getName() + "!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							pokemon.getName() + " got away!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnRun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				Toast.makeText(getActivity().getApplicationContext(),
						"You ran away!", Toast.LENGTH_SHORT).show();
			}
		});

		return rootView;
	}

	public interface StarterPokemonDialogListener {
		void onFinishStarterDialog(String name);
	}

}
