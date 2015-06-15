package com.pokemaps.pokemaps.drawer;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.pokemaps.pokemaps.R;
import com.pokemaps.pokemaps.data.Item;

public class ItemListFragment extends Fragment {


	List<Item> userItems = null;
	public ItemListFragment(List<Item> itemList) {
		this.userItems = itemList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_collection,
				container, false);
		GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
		gridview.setAdapter(new ItemListAdapter(getActivity(), userItems));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(
						getActivity(),
						((TextView) v.findViewById(R.id.grid_item_label))
								.getText(), Toast.LENGTH_SHORT).show();
			}
		});

		return rootView;
	}

}
