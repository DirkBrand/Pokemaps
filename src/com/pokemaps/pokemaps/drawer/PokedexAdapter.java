package com.pokemaps.pokemaps.drawer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pokemaps.pokemaps.R;
import com.pokemaps.pokemaps.R.id;
import com.pokemaps.pokemaps.R.layout;
import com.pokemaps.pokemaps.data.Pokemon;
import com.pokemaps.pokemaps.data.UserPokemon;

public class PokedexAdapter extends BaseAdapter {
	// references to our images

	private Context context;
	private final List<Pokemon> gridItems;
	private final List<Pokemon> userPokemon;

	public PokedexAdapter(Context c, List<Pokemon> gridItems, List<Pokemon> userPokemon) {
		this.context = c;
		this.gridItems = gridItems;
		this.userPokemon = userPokemon;
	}

	public int getCount() {
		return gridItems.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View gridView;
 
		if (convertView == null) {
 
			gridView = inflater.inflate(R.layout.collection_image, parent, false);        
 
		} else {
			gridView = (View) convertView;
		}
		

		Pokemon curPoke = gridItems.get(position);
		
		// set value into textview
		TextView textView = (TextView) gridView
				.findViewById(R.id.grid_item_label);
		textView.setText(curPoke.getName());

		// set image based on selected text
		ImageView imageView = (ImageView) gridView
				.findViewById(R.id.grid_item_image);

		
		if (contains(curPoke)) {
			imageView.setImageResource(curPoke.getImageId()); 
		} else {
			imageView.setImageResource(curPoke.getGreyImageId()); 
		}
		
		TextView itemMult = (TextView) gridView.findViewById(R.id.item_multiplier);
		int c = count(curPoke);
		if (c > 1) {
			itemMult.setText("x" + c);
		} else {
			itemMult.setText("");			
		}
		
 
		return gridView;
	}
	
	public boolean contains(Pokemon current) {
		for (Pokemon p : userPokemon) {
			if(p.getId() == current.getId()) {
				return true;
			}
		}
		return false;
	}
	
	public int count(Pokemon current) {
		int count = 0;
		for (Pokemon p : userPokemon) {
			if(p.getId() == current.getId()) {
				count++;
			}
		}
		return count;
	}
	

}