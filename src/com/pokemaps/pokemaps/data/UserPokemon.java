package com.pokemaps.pokemaps.data;

import com.j256.ormlite.field.DatabaseField;

public class UserPokemon {

	public final static String USER_ID_FIELD_NAME = "user_id";
	public final static String POKEMON_ID_FIELD_NAME = "pokemon_id";
	
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreign = true, columnName = USER_ID_FIELD_NAME)
	private User user;
	@DatabaseField(foreign = true, columnName = POKEMON_ID_FIELD_NAME)
	private Pokemon pokemon;
	
	public UserPokemon() {
		
	}
	
	public UserPokemon(User user, Pokemon pokemon) {
		this.user = user;
		this.pokemon = pokemon;
	}
}
