package com.pokemaps.pokemaps.data;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.pokemaps.pokemaps.R;

public class Zone implements Serializable {
	
	/**
	 * Model Class for Pokemon database table
	 */
	private static final long serialVersionUID = -8755734209725555655L;
	public final static String ZONE_ID_FIELD_NAME = "zone_id";
	public final static String NAME_FIELD_NAME = "zone_name";
	public final static String TYPE_FIELD_NAME = "zone_type";
	public final static String LAT_FIELD_NAME = "latitude";
	public final static String LONG_FIELD_NAME = "longitude";
	public final static String RADIUS_FIELD_NAME = "zone_radius";
	
	public final static double DEFAULT_RADIUS = 500;

	@DatabaseField(generatedId = true, columnName = ZONE_ID_FIELD_NAME)
	private int id;
	
	@DatabaseField(columnName = NAME_FIELD_NAME)
	private String name;	
	
	@DatabaseField(columnName = TYPE_FIELD_NAME)
	private ZoneType zoneType;
	
	@DatabaseField(columnName = LAT_FIELD_NAME)
	private double latitude;
	
	@DatabaseField(columnName = LONG_FIELD_NAME)
	private double longitude;
	
	@DatabaseField(columnName = RADIUS_FIELD_NAME)
	private double radius;
	
	
	public enum ZoneType{
		GRASSLAND(1), ROUGH(2), WATER(3), MOUNTAIN(4), SEA(5), URBAN(6), CAVE(7), FOREST(8);
		int value;
		private ZoneType(int val) {
			this.value = val;
		}
		public int getColourResource() {
			switch (this) {
			case GRASSLAND:
				return R.color.transparent_light_green;
			case WATER:
				return R.color.transparent_light_blue;
			case MOUNTAIN:
				return R.color.transparent_light_gray;
			case ROUGH:
				return R.color.transparent_light_brown;
			case SEA:
				return R.color.transparent_sea_blue;
			case URBAN:
				return R.color.transparent_lemon_chiffon;
			case CAVE:
				return R.color.transparent_purple;
			case FOREST:
				return R.color.transparent_dark_green;
			default:
				return R.color.white;
			}
		}
		public int getColourOutline() {
			switch (this) {
			case GRASSLAND:
				return R.color.green_apple;
			case WATER:
				return R.color.light_blue;
			case MOUNTAIN:
				return R.color.dark_gray;
			case ROUGH:
				return R.color.brown_bear;
			case SEA:
				return R.color.slate_blue;
			case URBAN:
				return R.color.dandelion;
			case CAVE:
				return R.color.dark_byzantium;
			case FOREST:
				return R.color.forest_green;
				
			default:
				return R.color.white;
			}
		}
	}
	
	
	public Zone() {}
	
	public Zone (String name, ZoneType type, double latitude, double longitude, double radius) {
		this.name = name;
		this.zoneType = type;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ZoneType getType() {
		return zoneType;
	}

	public void setType(ZoneType type) {
		this.zoneType = type;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public double getLat() {
		return latitude;
	}

	public void setLat(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLong() {
		return longitude;
	}

	public void setLong(double longitude) {
		this.longitude = longitude;
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name + " - " + zoneType;
	}
}
