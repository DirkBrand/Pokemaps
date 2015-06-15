package com.pokemaps.pokemaps.drawer;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pokemaps.pokemaps.PokemonInfoDialog;
import com.pokemaps.pokemaps.R;
import com.pokemaps.pokemaps.Utilities;
import com.pokemaps.pokemaps.R.id;
import com.pokemaps.pokemaps.R.layout;
import com.pokemaps.pokemaps.data.DatabaseHelper;
import com.pokemaps.pokemaps.data.Pokemon;
import com.pokemaps.pokemaps.data.User;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PokedexFragment extends Fragment {

	List<Pokemon> pokemonCollection = null;

	List<Pokemon> userPokemon = null;
	private Dao<Pokemon, Integer> pokemonDao;

	public PokedexFragment(List<Pokemon> pokemonList) {
		this.userPokemon = pokemonList;

		DatabaseHelper databaseHelper = OpenHelperManager.getHelper(
				getActivity(), DatabaseHelper.class);
		try {
			pokemonDao = databaseHelper.getPokemonDao();
			pokemonCollection = pokemonDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_collection,
				container, false);
		GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
		gridview.setAdapter(new PokedexAdapter(getActivity(),
				pokemonCollection, userPokemon));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				try {
					Pokemon p = pokemonDao.queryForEq(
							Pokemon.NUMBER_FIELD_NAME, position + 1).get(0);
					Log.i("pokemaps", p.getName() + " clicked");
					if (contains(p)) {
						PokemonInfoDialog dialog = PokemonInfoDialog
								.newInstance(p);
						dialog.show(getFragmentManager(), "dialog");
					} else {
						PokemonInfoDialog dialog = PokemonInfoDialog
								.newInstance(new Pokemon(0,"?","???",null,-1,p.getGreyImageId(),p.getGreyImageId()));
						dialog.show(getFragmentManager(), "dialog");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		return rootView;
	}

	public boolean contains(Pokemon current) {
		for (Pokemon p : userPokemon) {
			if (p.getId() == current.getId()) {
				return true;
			}
		}
		return false;
	}
}
