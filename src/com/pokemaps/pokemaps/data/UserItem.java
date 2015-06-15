package com.pokemaps.pokemaps.data;

import com.j256.ormlite.field.DatabaseField;

public class UserItem {

	public final static String USER_ID_FIELD_NAME = "user_id";
	public final static String ITEM_ID_FIELD_NAME = "item_id";
	public final static String COUNT_FIELD_NAME = "count";
	
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreign = true, columnName = USER_ID_FIELD_NAME)
	private User user;
	@DatabaseField(foreign = true, columnName = ITEM_ID_FIELD_NAME)
	private Item item;
	
	
	public UserItem() {
		
	}
	
	public UserItem(User user, Item item) {
		this.user = user;
		this.item = item;
	}
	
/*
	public void incrementCount() {
		this.count++;
	}
	public int getCount() {
		return this.count;
	}
*/
}
