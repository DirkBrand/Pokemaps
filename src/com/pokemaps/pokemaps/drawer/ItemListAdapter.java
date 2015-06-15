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
import com.pokemaps.pokemaps.data.Item;
import com.pokemaps.pokemaps.data.Pokemon;

public class ItemListAdapter extends BaseAdapter {
	// references to our images

	private Context context;
	private final List<Item> userItems;

	public ItemListAdapter(Context c, List<Item> userItems) {
		this.context = c;
		this.userItems = userItems;
	}

	public int getCount() {
		return userItems.size();
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
			gridView = inflater.inflate(R.layout.collection_image, parent,
					false);
		} else {
			gridView = (View) convertView;
		}

		Item curItem = userItems.get(position);

		// set value into textview
		TextView textView = (TextView) gridView
				.findViewById(R.id.grid_item_label);
		textView.setText(curItem.getName());

		// set image based on selected text
		ImageView imageView = (ImageView) gridView
				.findViewById(R.id.grid_item_image);

		imageView.setImageResource(curItem.getImageId());

		TextView itemMult = (TextView) gridView
				.findViewById(R.id.item_multiplier);
		int c = count(curItem);
		if (c > 1) {
			itemMult.setText("x" + c);
		} else {
			itemMult.setText("");
		}

		return gridView;
	}

	public int count(Item current) {
		int count = 0;
		for (Item i : userItems) {
			if (i.getId() == current.getId()) {
				count++;
			}
		}
		return count;
	}

}