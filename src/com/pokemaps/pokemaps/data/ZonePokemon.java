package com.pokemaps.pokemaps.data;

import com.j256.ormlite.field.DatabaseField;

public class ZonePokemon {

	public final static String ZONE_ID_FIELD_NAME = "zone_id";
	public final static String POKEMON_ID_FIELD_NAME = "pokemon_id";
	
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreign = true, columnName = ZONE_ID_FIELD_NAME)
	private Zone zone;
	@DatabaseField(foreign = true, columnName = POKEMON_ID_FIELD_NAME)
	private Pokemon pokemon;
	
	public ZonePokemon() {
		
	}
	
	public ZonePokemon(Zone zone, Pokemon pokemon) {
		this.zone = zone;
		this.pokemon = pokemon;
	}
}
