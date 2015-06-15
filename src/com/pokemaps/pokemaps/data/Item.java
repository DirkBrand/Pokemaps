package com.pokemaps.pokemaps.data;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class Item implements Serializable {
	
	/**
	 * Model Class for Pokemon database table
	 */
	private static final long serialVersionUID = -8755734209725555655L;
	public final static String ITEM_ID_FIELD_NAME = "item_id";
	public final static String NAME_FIELD_NAME = "name";
	public final static String DESCRIPTION_FIELD_NAME = "description";
	public final static String IMAGE_ID_FIELD_NAME = "image_id";
	public final static String EXTRA_VALUE_FIELD_NAME = "extra_value";
	

	@DatabaseField(generatedId = true, columnName = ITEM_ID_FIELD_NAME)
	private long id;
	
	@DatabaseField(columnName = NAME_FIELD_NAME)
	private String name;	
	

	@DatabaseField(columnName = IMAGE_ID_FIELD_NAME)
	private int image_id;

	@DatabaseField(columnName = DESCRIPTION_FIELD_NAME)
	private String description;

	@DatabaseField(columnName = EXTRA_VALUE_FIELD_NAME)
	private int extra_value;
	
	public Item() {}
	
	public Item (String name, String description, int imageId) {
		this.name = name;
		this.description = description;
		this.image_id = imageId;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getImageId() {
		return image_id;
	}

	public void setImageId(int id) {
		this.image_id = id;
	}
	
	public int getExtraValue() {
		return extra_value;
	}

	public void setExtraValue(int val) {
		this.extra_value = val;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name;
	}
}
